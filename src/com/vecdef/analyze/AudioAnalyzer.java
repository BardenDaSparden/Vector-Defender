package com.vecdef.analyze;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.audio.MusicPlayer;
import com.vecdef.audio.TrackChangeEvent;
import com.vecdef.audio.TrackEventListener;
import com.vecdef.gamestate.ShapeRenderer;
import com.vecdef.model.Primitive.DrawType;

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
	}
	
	public void analyze(){
		
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
