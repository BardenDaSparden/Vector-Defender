package com.vecdef.gamestate;

import static org.lwjgl.opengl.GL11.glViewport;

import java.text.DecimalFormat;

import org.barden.core.Application;
import org.barden.core.ApplicationSettings;
import org.barden.input.InputSystem;
import org.barden.input.Joystick;
import org.barden.input.JoystickListener;
import org.barden.input.Keyboard;
import org.barden.input.Mouse;
import org.barden.util.Debug;
import org.barden.util.GPUProfiler;
import org.javatroid.core.Resources;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.GLUtil;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.text.BitmapFont;

import com.vecdef.audio.AudioAnalyzer;
import com.vecdef.audio.MinimFileHandler;
import com.vecdef.audio.MusicPlayer;
import com.vecdef.rendering.Renderer;
import com.vecdef.rendering.ShapeRenderer;

import ddf.minim.Minim;

public class VectorDefender extends Application {

	static DecimalFormat FORMATTER = new DecimalFormat("##.##");
	
	//( App/Input ) settings
	protected ApplicationSettings settings;
	protected InputSystem input;
	
	//Used for audio analyzation and playback
	protected Minim minim;
	protected MusicPlayer musicPlayer;
	protected AudioAnalyzer musicAnalyzer;
	
	//Rendering
	protected Renderer renderer;
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRenderer;
	
	//Input
	protected Keyboard keyboard;
	protected Mouse mouse;
	protected Joystick joystick1;
	protected Joystick joystick2;
	protected Joystick joystick3;
	protected Joystick joystick4;
	JoystickListener listener;
	
	//Gamestate
	GameStateListener stateListener;
	GameState gameState;
	
	//Debug Overlay
	OrthogonalCamera overlayCamera;
	BitmapFont overlayFont;
	
	long frameCount = 0;
	
	public VectorDefender(){
		MinimFileHandler fHandler = new MinimFileHandler();
		minim = new Minim(fHandler);
		musicPlayer = new MusicPlayer(minim);
		musicAnalyzer = new AudioAnalyzer(musicPlayer);
		GameStates.initStates(this);
		gameState = GameStates.NONE;
		stateListener = new GameStateListener() {
			@Override
			public void onStateChange(GameState oldState, GameState newState) {
				oldState.cleanUp();
				gameState = newState;
				gameState.initialize();
			}
		};
	}
	
	protected void setGameState(GameState newState){
		stateListener.onStateChange(gameState, newState);
	}
	
	@Override
	public void initialize(){
		Resources.loadFont("fonts/imagine_16.fnt", "imagine12");
		Resources.loadFont("fonts/imagine_16.fnt", "imagine16");
		Resources.loadFont("fonts/Imagine.fnt", "imagine18");
		Resources.loadFont("fonts/tech18.fnt", "tech18");
		Resources.loadFont("fonts/tech30.fnt", "tech30");
		Resources.loadFont("fonts/tech36.fnt", "tech36");
		Resources.loadFont("fonts/square_16.fnt", "square16");
		Resources.loadFont("fonts/square18.fnt", "square18");
		Resources.loadFont("fonts/squarefont_36.fnt", "square36");
		
		Resources.loadTexture("textures/white.png", "blank");
		Resources.loadTexture("textures/menuTitle.png", "title");
		Resources.loadTexture("textures/energyBarOutline.png", "energyBar");
		Resources.loadTexture("textures/livesBarOutline.png", "livesBar");
		Resources.loadTexture("textures/life.png", "life");
		Resources.loadTexture("textures/splash1.png", "Splash");
		Resources.loadTexture("textures/splash2.png", "Splash2");
		Resources.loadTexture("textures/mainMenuTitle.png", "MainMenuTitle");
		Resources.loadTexture("textures/ui-player-stat-background.png", "PlayerStatBackground");
		
		Resources.loadSound("audio/ui_on_select.wav", "onSelect");
		Resources.loadSound("audio/ship_basic_fire.wav", "fire1");
		Resources.loadSound("audio/enemy_spawn.wav", "spawn1");
		Resources.loadSound("audio/ui_countdown_beep.wav", "CountdownBeep");
		Resources.loadSound("audio/ui_countdown_complete_beep.wav", "CountdownComplete");
		
		settings = task.getSettings();
		input = task.getInput();
		
		renderer = new Renderer(settings.width, settings.height);
		batch = renderer.SpriteBatch();
		shapeRenderer = renderer.ShapeRenderer();
		
	    joystick1 = input.getJoystick(0);	//Player 1 controller
	    joystick2 = input.getJoystick(1);	//Player 1 controller
	    joystick3 = input.getJoystick(2);	//Player 1 controller
	    joystick4 = input.getJoystick(3);	//Player 1 controller
	    
	    listener = new JoystickListener() {
			
			@Override
			public void onButtonRelease(int button) {
				
			}
			
			@Override
			public void onButtonPress(int button) {
				if(button == Joystick.BUTTON_LEFT_BUMPER)
					musicPlayer.previousTrack();
				if(button == Joystick.BUTTON_RIGHT_BUMPER)
					musicPlayer.nextTrack();
			}
		};
		
		joystick1.addListener(listener);
	    setGameState(GameStates.SPLASH);
	    
	    overlayCamera = new OrthogonalCamera(settings.width, settings.height);
		overlayFont = Resources.getFont("square16");
		
		Debug.logInfo("Input", "Gamepad 1 is " + ((joystick1.isConnected()) ? "connected" : "disconnected"));
		Debug.logInfo("Input", "Gamepad 2 is " + ((joystick2.isConnected()) ? "connected" : "disconnected"));
		Debug.logInfo("Input", "Gamepad 3 is " + ((joystick3.isConnected()) ? "connected" : "disconnected"));
		Debug.logInfo("Input", "Gamepad 4 is " + ((joystick4.isConnected()) ? "connected" : "disconnected"));
	}
	
	@Override
	public void update(){
		long startTime = System.nanoTime();
		
		musicPlayer.poll();
		gameState.update();
		
		long endTime = System.nanoTime();
		double millis = (endTime - startTime) / 1000000D;
		
		//System.out.println("[Update Frame: " + frameCount + "] : " +  FORMATTER.format(millis) + "ms");
		frameCount++;
	}
	
	@Override
	public void render(double interpolation){
		GPUProfiler profiler = GPUProfiler.get();
		profiler.beginFrame();
		{
			profiler.startTask("Vector Defender Render");
				renderer.startFrame();
				GLUtil.clear(true, false, false, false);
				glViewport(0, 0, settings.width, settings.height);
				gameState.render();
				SpriteBatch batch = renderer.SpriteBatch();
				batch.setCamera(overlayCamera);
				batch.begin(BlendState.ALPHA);
					batch.setColor(1, 1, 1, 0.45f);
					String buildStr = "PREVIEW BUILD 08-15-2020";
					String copyStr = "BardenDaSparden";
					overlayFont.drawString(-overlayFont.getWidth(buildStr) - 7, -settings.height / 2 + 20, buildStr, batch);
					overlayFont.drawString(7, -settings.height / 2 + 20, copyStr, batch);
				batch.end();
				renderer.endFrame();
		    profiler.endTask();
		}
		profiler.endFrame();
		
		//System.out.println(renderer.getBatchCounts());
		//profiler.getResults().dump();
	}
	
	@Override
	public void destroy(){
		minim.dispose();
		musicPlayer.destroy();
		gameState.cleanUp();
	}
	
	@Override
	public void resize(int width, int height){
		
	}
	
	public void quit(){
		task.stop();
	}
}
