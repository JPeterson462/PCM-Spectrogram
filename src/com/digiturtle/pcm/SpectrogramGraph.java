package com.digiturtle.pcm;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SpectrogramGraph extends Application {

	private Spectrogram spectrogram;
	
	public void start(Stage stage) {
		float width = 1000, height = 500, max = 300_000;
		spectrogram = new Spectrogram(width, height / 2, max);
		Group root = new Group();
		Scene scene = new Scene(root, 1000, 500, Color.BLACK);
		stage.setTitle("Spectrogram");
		stage.setScene(scene);
		stage.show();
		for (int i = 0 ; i < spectrogram.getPoints().length; i++) {
			Rectangle r = new Rectangle(spectrogram.getPoints()[i].x + 1, spectrogram.getPoints()[i].y, spectrogram.getSampleWidth() - 2, 10);
			r.setFill(Color.GREEN);
			root.getChildren().add(r);
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
						spectrogram.update(DELTA);
						Point[] points = spectrogram.getPoints();
						for (int i = 0; i < points.length; i++) {
							float h = points[i].y + 10;
							Rectangle r = (Rectangle) root.getChildren().get(i);
							r.setY((height - h) / 2);
							r.setHeight(h);
						}
					}
				});
			}
		}, 0, DELTA, TimeUnit.MILLISECONDS);
	}
	
}
