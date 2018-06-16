package com.digiturtle.pcm;

import java.util.ArrayList;

public class Spectrogram implements AudioFileReader.FFTOutput {
	
	private ArrayList<Float> stream = new ArrayList<>();
	
	private Point[] points;
	
	private float height, max;
	
	private final int SAMPLE_WIDTH = 10;
	
	public Spectrogram(float width, float height, float max) {
		points = new Point[(int) (width / SAMPLE_WIDTH)];
		for (int i = 0 ; i < points.length; i++) {
			stream.add(0f);
			points[i] = new Point();
			points[i].x = ((float) i / (float) points.length) * width;
			points[i].y = 0;
		}
		this.height = height;
		this.max = max;
	}
	
	private void updatePoints() {
		for (int i = 0; i < points.length; i++) {
			points[i].y = ((float) stream.get(i) / max) * height;
		}
	}
	
	public float getSampleWidth() {
		return SAMPLE_WIDTH;
	}
	
	@Override
	public void write(float[] data) {
		for (int i = 0; i < data.length; i++) {
			stream.add(data[i]);
		}
	}
	
	public Point[] getPoints() {
		return points;
	}
	
	public void update(long delta) {
		for (int i = 0; i < delta; i++) {
			stream.remove(0);
		}
		updatePoints();
	}

}
