package com.vecdef.audio;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.rendering.ShapeRenderer;
import com.vecdef.rendering.ShapeRenderer.DrawType;

import ddf.minim.AudioListener;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import ddf.minim.analysis.WindowFunction;

public class AudioAnalyzer {
	
	int bufferSize;
	int sampleRate;
	float[] samples;
	FFT fft;
	WindowFunction function = FFT.HAMMING;
	
	BeatDetect detect;
	BeatDetect detect2;
	
	AudioPlayer currentTrack;
	
	ArrayList<Vector2f> positions;
	ArrayList<Vector4f> colors;
	final int NUM_VERTICES = 3000;
	int drawIdx = 0;
	
	public AudioAnalyzer(MusicPlayer player){
		bufferSize = player.getBufferSize();
		sampleRate = player.getSampleRate();
		samples = new float[bufferSize];
		fft = new FFT(bufferSize, sampleRate);
		fft.window(function);
		detect = new BeatDetect(bufferSize, sampleRate);
		detect.detectMode(BeatDetect.FREQ_ENERGY);
		detect2 = new BeatDetect(bufferSize, sampleRate);
		detect2.detectMode(BeatDetect.SOUND_ENERGY);
		currentTrack = player.getCurrentTrack();
		
		final AudioListener dataListener = new AudioListener() {
			@Override
			public void samples(float[] sampL, float[] sampR) {
				samples = sampL;
			}
			
			@Override
			public void samples(float[] samp) {
				samples = samp;
			}
		};
		currentTrack.addListener(dataListener);
		
		player.addListener(new TrackEventListener() {
			
			@Override
			public void process(TrackChangeEvent event){
				currentTrack.removeListener(dataListener);
				currentTrack = event.track;
				currentTrack.addListener(dataListener);
			}
		});
	
		positions = new ArrayList<Vector2f>();
		colors = new ArrayList<Vector4f>();
		for(int i = 0; i < NUM_VERTICES; i++){
			positions.add(new Vector2f());
			colors.add(new Vector4f());
		}
		
	}
	
	public void analyze(){
		fft.window(FFT.HAMMING);
		fft.forward(samples);
		//fft.linAverages(512);
		fft.logAverages(50, 40);
		System.out.println(fft.avgSize());
		//System.out.println(fft.specSize());
	}
	
	public void drawSpectrumH(float x, float y, float width, Vector4f color, ShapeRenderer renderer){
		
		renderer.begin(DrawType.LINES, BlendState.ADDITIVE);
		
		int n = fft.avgSize();
		for(int i = n - 1; i > -1; i--){
			float f = 1.0f - (float)i / (float)n;
			float bw = fft.getAverageBandWidth(i);
			float cf = fft.getAverageCenterFrequency(i);
			float lowBound = cf - bw / 2;
			float hiBound = cf + bw / 2;
			float avg = fft.calcAvg(lowBound, hiBound);
			Vector2f v0 = positions.get(drawIdx);
			Vector2f v1 = positions.get(drawIdx + 1);
			Vector4f c0 = colors.get(drawIdx);
			Vector4f c1 = colors.get(drawIdx + 1);
			drawIdx += 2;
			
			
			v0.set(x + f * width, y + -avg);
			v1.set(x + f * width, y + avg);
//			c0.set(0, 0.5f, 1, 0.4f);
//			c1.set(0.5f, 0, 1, 0.4f);
			c0.set(color);
			c1.set(color);
			renderer.draw(v0, c0);
			renderer.draw(v1, c1);
			
			Vector2f v2 = positions.get(drawIdx);
			Vector2f v3 = positions.get(drawIdx + 1);
			Vector4f c2 = colors.get(drawIdx);
			Vector4f c3 = colors.get(drawIdx + 1);
			drawIdx += 2;
			
			
			v2.set(x + -f * width, y + -avg);
			v3.set(x + -f * width, y + avg);
//			c2.set(0, 0.5f, 1, 0.4f);
//			c3.set(0.5f, 0, 1, 0.4f);
			c2.set(color);
			c3.set(color);
			renderer.draw(v2, c2);
			renderer.draw(v3, c3);
			
		}
		
		renderer.end();
		drawIdx = 0;
	}
	
	public void drawSpectrumV(float x, float y, float height, Vector4f color, ShapeRenderer renderer){
		
		renderer.begin(DrawType.LINES, BlendState.ADDITIVE);
		
		int n = fft.avgSize();
		for(int i = n - 1; i > -1; i--){
			float f = 1.0f - (float)i / (float)n;
			float bw = fft.getAverageBandWidth(i);
			float cf = fft.getAverageCenterFrequency(i);
			float lowBound = cf - bw / 2;
			float hiBound = cf + bw / 2;
			float avg = fft.calcAvg(lowBound, hiBound);
			Vector2f v0 = positions.get(drawIdx);
			Vector2f v1 = positions.get(drawIdx + 1);
			Vector4f c0 = colors.get(drawIdx);
			Vector4f c1 = colors.get(drawIdx + 1);
			drawIdx += 2;
			
			
			v0.set(x + -avg * 2f, y + f * height);
			v1.set(x + avg * 2f, y + f * height);
//			c0.set(0, 0.5f, 1, 0.4f);
//			c1.set(0.5f, 0, 1, 0.4f);
			c0.set(color);
			c1.set(color);
			renderer.draw(v0, c0);
			renderer.draw(v1, c1);
			
			Vector2f v2 = positions.get(drawIdx);
			Vector2f v3 = positions.get(drawIdx + 1);
			Vector4f c2 = colors.get(drawIdx);
			Vector4f c3 = colors.get(drawIdx + 1);
			drawIdx += 2;
			
			
			v2.set(x + -avg * 2f, y - f * height);
			v3.set(x + avg * 2f, y - f * height);
//			c2.set(0, 0.5f, 1, 0.4f);
//			c3.set(0.5f, 0, 1, 0.4f);
			c2.set(color);
			c3.set(color);
			renderer.draw(v2, c2);
			renderer.draw(v3, c3);
			
		}
		
		renderer.end();
		drawIdx = 0;
	}
	
	ArrayList<Vector2f> waveformPoints = new ArrayList<Vector2f>();
	public void drawWaveform(float x, float y, float spacingX, float scaleY, int averages, Vector4f color, ShapeRenderer renderer){
		waveformPoints.clear();
		
		
		int n = samples.length / averages;
		for(int i = 0; i < n; i+=averages){
			float sum = 0;
			for(int j = 0; j < averages; j++){
				sum += samples[i + j];
			}
			sum /= averages;
			waveformPoints.add(new Vector2f(i * spacingX, sum * scaleY));
		}
		
		float waveformWidth = waveformPoints.size() * spacingX - (2 * spacingX);
		Vector2f offset = new Vector2f(-(waveformWidth / 2.0f) + x, y);
		renderer.begin(DrawType.LINES, BlendState.ADDITIVE);
			for(int i = 0; i < waveformPoints.size() - 1; i++){
				Vector2f v0 = waveformPoints.get(i).add(offset);
				Vector2f v1 = waveformPoints.get((i + 1)).add(offset);
				renderer.draw(v0, color);
				renderer.draw(v1, color);
			}
		renderer.end();
	}
	
	public void drawWaveformH(float x, float y, float width, float height, Vector4f color, ShapeRenderer renderer){
		waveformPoints.clear();
		
		int n = samples.length;
		float spacingX = width / n;
		
		Vector2f offset = new Vector2f(-width / 2 + x, y);
		for(int i = 0; i < n; i++){
			float sample = samples[i];
			waveformPoints.add(new Vector2f(i * spacingX + offset.x, sample * height + offset.y));
		}
		
		Vector2f start = waveformPoints.get(0);
		Vector2f end = waveformPoints.get(waveformPoints.size() - 1);
		start.set(start.x, y);
		end.set(end.x, y);
		
		renderer.begin(DrawType.LINES, BlendState.ADDITIVE);
			for(int i = 0; i < waveformPoints.size() - 1; i++){
				Vector2f v0 = waveformPoints.get(i);
				Vector2f v1 = waveformPoints.get((i + 1));
				renderer.draw(v0, color);
				renderer.draw(v1, color);
			}
		renderer.end();
	}
	
	public void drawWaveformV(float x, float y, float width, float height, int averages, Vector4f color, ShapeRenderer renderer){
		waveformPoints.clear();
		
		int n = samples.length;
		float spacingY = height / n;
		
		Vector2f offset = new Vector2f(x, -height / 2 + y);
		for(int i = 0; i < n; i++){
			float sample = samples[i];
			waveformPoints.add(new Vector2f(sample * width + offset.x, i * spacingY + offset.y));
		}
		
		Vector2f start = waveformPoints.get(0);
		Vector2f end = waveformPoints.get(waveformPoints.size() - 1);
		start.set(x, start.y);
		end.set(x, end.y);
		
		renderer.begin(DrawType.LINES, BlendState.ADDITIVE);
			for(int i = 0; i < waveformPoints.size() - 1; i++){
				Vector2f v0 = waveformPoints.get(i);
				Vector2f v1 = waveformPoints.get((i + 1));
				renderer.draw(v0, color);
				renderer.draw(v1, color);
			}
		renderer.end();
	}
}
