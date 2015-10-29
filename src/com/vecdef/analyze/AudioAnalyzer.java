package com.vecdef.analyze;

import org.javatroid.core.Resources;
import org.javatroid.graphics.SpriteBatch;
import org.lwjgl.opengl.Display;

import ddf.minim.analysis.FFT;
import ddf.minim.analysis.WindowFunction;

public class AudioAnalyzer {

	FFT fft;
	WindowFunction function = FFT.HAMMING;
	
	public AudioAnalyzer(int bufferSize, int sampleRate){
		fft = new FFT(bufferSize, sampleRate);
		fft.window(function);
	}
	
	public void drawLogFreqSpectrum(float[] buffer, SpriteBatch batch){
		final int BAR_WIDTH = 2;
		final int BAR_SPACING = 1;
		
		fft.forward(buffer);
		int numBins = fft.specSize();
		int spectrumWidth = (numBins) * (BAR_WIDTH + BAR_SPACING);
		float startX = -spectrumWidth / 2 - BAR_WIDTH / 2;
		
		batch.begin();
		batch.setColor(0.35f, 1, 0.35f, 0.1f);
		for(int i = 0; i < numBins; i++){
			int offsetX = i * (BAR_WIDTH + BAR_SPACING);
			float amplitude = fft.getBand(i) * (float)Math.log10(fft.getBandWidth() * i) * 2;
			batch.draw(startX + offsetX, 0, BAR_WIDTH, amplitude, 0, Resources.getTexture("blank"));
		}
		batch.end();
		batch.setColor(1, 1, 1, 1);
	}
	
	public void drawLogFreqSpectrumAvg(float[] buffer, SpriteBatch batch){
		final int BAR_WIDTH = 30;
		final int BAR_SPACING = 1;
		final int AVERAGES = 40;
		
		fft.linAverages(AVERAGES);
		fft.forward(buffer);
		
		float posX = 0;
		float posY = -Display.getHeight() / 2;
		
		int numBins = AVERAGES;
		int spectrumWidth = (numBins) * (BAR_WIDTH + BAR_SPACING);
		float startX = -spectrumWidth / 2 - BAR_WIDTH / 2;
		float offsetY = 0;
		
		batch.begin();
		batch.setColor(0, 1, 0.5f, 0.75f);
		for(int i = 0; i < fft.avgSize(); i++){
			int offsetX = i * (BAR_WIDTH + BAR_SPACING);
			float amplitude = fft.getAvg(i) * (float)Math.log10(fft.getAverageCenterFrequency(i)) * 2;
			offsetY = amplitude / 2;
			batch.draw(posX + startX + offsetX, posY + offsetY, BAR_WIDTH, amplitude, 0, Resources.getTexture("blank"));
		}
		batch.end();
		batch.setColor(1, 1, 1, 1);
	}
	
	public void test_analyze(){
		int numBins = fft.specSize();
		for(int i = 0; i < numBins; i++){
			System.out.print(fft.getBand(i) + " ");
		}
		System.out.println();
	}
	
}
