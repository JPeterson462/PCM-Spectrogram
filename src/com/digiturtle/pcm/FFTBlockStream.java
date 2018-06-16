package com.digiturtle.pcm;

import javax.sound.sampled.AudioFormat;

import org.jtransforms.fft.FloatFFT_1D;

import com.digiturtle.pcm.AudioFileReader.FFTOutput;

public class FFTBlockStream implements AudioFileReader.BlockStream {

	@Override
	public void block(AudioFormat format, byte[] bytes, int length, FFTOutput output) {
		float[] samples = new float[bytes.length / format.getSampleSizeInBits()];
		for (int i = 0; i < samples.length; i++) {
			samples[i] = BitConverter.toSingle(bytes, i * format.getSampleSizeInBits() / 8, format.getSampleSizeInBits() / 8);
		}
		FloatFFT_1D floatFFT_1D = new FloatFFT_1D(samples.length);
		floatFFT_1D.realForward(samples);
		output.write(samples);
	}

}
