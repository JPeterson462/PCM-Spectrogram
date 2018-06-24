package com.digiturtle.pcm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SpectrogramGraph extends Application {

	private Spectrogram spectrogram;
	
	public void start(Stage stage) {
		final int rWidth = 100, rHeight = 49;
		float width = rWidth * Spectrogram.SAMPLE_WIDTH, height = rHeight * Spectrogram.SAMPLE_WIDTH, max = 700_000;
		Paint background = Color.BLACK, foreground = Color.LIME;
		spectrogram = new Spectrogram((int) (width / Spectrogram.SAMPLE_WIDTH));
		Group root = new Group();
		Scene scene = new Scene(root, width, height, background);
		stage.setTitle("Spectrogram");
		stage.setScene(scene);
		stage.show();
		Rectangle[][] rectangles = new Rectangle[rWidth][rHeight];
		for (int i = 0; i < rWidth; i++) {
			for (int j = 0; j < rHeight; j++) {
				Rectangle r = new Rectangle(i * Spectrogram.SAMPLE_WIDTH + 1, j * Spectrogram.SAMPLE_WIDTH + 1, 
						Spectrogram.SAMPLE_WIDTH - 2, Spectrogram.SAMPLE_WIDTH - 2);
				r.setFill(background);
				root.getChildren().add(r);
				rectangles[i][j] = r;
			}
		}
		final AudioFileReader reader;
		try {
			reader = new AudioFileReader(new File("01. Critical Acclaim.wav"), new FFTBlockStream(), spectrogram);
		} catch (UnsupportedAudioFileException | IOException e) {
			throw new IllegalStateException(e);
		}
		final long DELTA = 5;
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						try {
							reader.readBlock(4096);
						} catch (IOException e) {
							e.printStackTrace();
						}
						spectrogram.update(DELTA * (reader.getSampleRate() / 1000));
						ArrayList<Float> samples = spectrogram.getStream();
						for (int i = 0; i < rWidth && i < samples.size(); i++) {
							int height = Math.abs(Math.max(1, (int) ((samples.get(i) / max) * rHeight)));
							System.out.println(samples.get(i));
							int offset = Math.max(0, (rHeight - height) / 2);
							height = Math.min(height, rHeight);
							for (int j = 0; j < offset && j < rHeight; j++) {
								rectangles[i][j].setFill(background);
							}
							for (int j = offset; j < height + offset && j < rHeight; j++) {
								rectangles[i][j].setFill(foreground);
							}
							for (int j = height + offset; j < rHeight && j < rHeight; j++) {
								rectangles[i][j].setFill(background);
							}
						}
						for (int i = samples.size(); i < rWidth; i++) {
							int height = 1;
							int offset = (rHeight - height) / 2;
							for (int j = 0; j < offset && j < rHeight; j++) {
								rectangles[i][j].setFill(background);
							}
							for (int j = offset; j < height + offset && j < rHeight; j++) {
								rectangles[i][j].setFill(foreground);
							}
							for (int j = rHeight - offset; j < rHeight && j < rHeight; j++) {
								rectangles[i][j].setFill(background);
							}
						}
					}
				});
			}
		}, 0, DELTA, TimeUnit.MILLISECONDS);
	}
	
}
