package com.digiturtle.pcm;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFileReader {
	
	@FunctionalInterface
	public interface FFTOutput {
		public void write(float[] data);
	}
	
	@FunctionalInterface
	public interface BlockStream {
		public void block(AudioFormat format, byte[] bytes, int length, FFTOutput output);
	}
	
	private AudioInputStream ais;
	
	private BlockStream stream;
	
	private FFTOutput output;
	
	public AudioFileReader(File file, BlockStream stream, FFTOutput output) throws UnsupportedAudioFileException, IOException {
		ais = AudioSystem.getAudioInputStream(file);
		this.stream = stream;
		this.output = output;
	}
	
	public int getSampleRate() {
		return (int) ais.getFormat().getSampleRate();
	}
	
	public int getSampleSize() {
		return ais.getFormat().getSampleSizeInBits();
	}
	
	public void readBlock(int blockSize) throws IOException {
		byte[] data = new byte[blockSize];
		int bytesRead = ais.read(data, 0, data.length);
		if (bytesRead >= 0) {
			stream.block(ais.getFormat(), data, bytesRead, output);
		}
	}

}
