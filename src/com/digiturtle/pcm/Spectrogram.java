package com.digiturtle.pcm;

import java.util.ArrayList;

public class Spectrogram implements AudioFileReader.FFTOutput {
	
	private ArrayList<Float> stream = new ArrayList<>();
	
	public static final int SAMPLE_WIDTH = 10;
	
	public Spectrogram(int samples) {
		for (int i = 0; i < samples; i++) {
			stream.add(0f);
		}
	}
	
	public ArrayList<Float> getStream() {
		return stream;
	}
	
	@Override
	public void write(float[] data) {
		for (int i = 0; i < data.length; i++) {
			stream.add(data[i]);
		}
	}
	
	public void update(long delta) {
		for (int i = 0; i < delta; i++) {
			stream.remove(0);
		}
	}

}
