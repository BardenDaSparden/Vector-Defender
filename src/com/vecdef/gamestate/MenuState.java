package com.vecdef.gamestate;

import javax.sound.sampled.AudioFormat;

import org.javatroid.core.Resources;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.Color;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.text.BitmapFont;
import org.lwjgl.opengl.Display;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.analyze.AudioAnalyzer;
import com.vecdef.core.MinimFileHandler;

import ddf.minim.AudioListener;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class MenuState extends GState{

	public MenuState(GameState gamestate) {
		super(gamestate);
	}

	MinimFileHandler fHandler;
	Minim minim;
	AudioPlayer player;
	AudioAnalyzer analyzer;
	
	int sampleRate;
	int bufferSize;
	float[] dataStream;

	SpriteBatch batch;
	OrthogonalCamera camera;
	
	Texture title;
	BitmapFont font;
	float time;
	Color color;
	
	@Override
	public void initialize() {
		fHandler = new MinimFileHandler();
		minim = new Minim(fHandler);
		player = minim.loadFile("astro.wav");
		AudioFormat format = player.getFormat();
		sampleRate = (int) format.getSampleRate();
		bufferSize = player.bufferSize();
		dataStream = new float[bufferSize];
		
		player.addListener(new AudioListener() {
			
			@Override
			public void samples(float[] sampL, float[] sampR) {
				dataStream = sampL;
			}
			
			@Override
			public void samples(float[] samp) {
				dataStream = samp;
			}
		});
		
		player.loop();
		
		analyzer = new AudioAnalyzer(bufferSize, sampleRate);
		
		batch = new SpriteBatch(1000);
		camera = new OrthogonalCamera((int)Display.getWidth(), (int)Display.getHeight());
		batch.setCamera(camera);
		
		title = Resources.getTexture("title");
		font = Resources.getFont("imagine12");
		color = new Color(1, 1, 1, 1);
	}

	@Override
	public void update() {
		time++;
		color.w = (float)Math.abs(Math.sin(time * 0.1f));
		if(gamestate.gamepad.isButtonPressed(Gamepad.START_BUTTON)){
			gamestate.next();
		}
	}

	@Override
	public void draw() {
		analyzer.drawLogFreqSpectrumAvg(dataStream, batch);
		batch.begin(BlendState.ADDITIVE);
			batch.draw(0, 40, title.getWidth(), title.getHeight(), 0, title);
			
			batch.setColor(1, 1, 1, color.w);
			font.drawStringCentered(0, -20, "Press Start to Begin...", batch);
			batch.setColor(1, 1, 1, 1);
		batch.end();
	}

	@Override
	public void destroy() {
		player.close();
	}
}
