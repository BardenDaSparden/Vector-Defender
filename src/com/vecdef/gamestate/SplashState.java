package com.vecdef.gamestate;

import org.barden.input.Joystick;
import org.barden.input.JoystickListener;
import org.javatroid.core.Resources;
import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;

public class SplashState extends GameState {

	public SplashState(VectorDefender instance) {
		super(instance);
	}

	OrthogonalCamera camera;
	JoystickListener listener;
	Texture splash;
	final int FADE_IN_TIME = 30;
	final int IDLE_TIME = 120;
	final int FADE_OUT_TIME = 30;
	Timer fadeInTimer;
	Timer idleTimer;
	Timer fadeOutTimer;
	boolean isReady = false;
	boolean splashComplete = false;
	
	@Override
	public void initialize() {
		camera = new OrthogonalCamera(instance.settings.width, instance.settings.height);
		listener = new JoystickListener() {
			
			@Override
			public void onButtonRelease(int button) {
				
			}
			
			@Override
			public void onButtonPress(int button) {
				if(button == Joystick.BUTTON_A || button == Joystick.BUTTON_START){
					instance.musicPlayer.play();
					instance.setGameState(GameStates.MAIN_MENU);
				}
			}
			
		};
		splash = Resources.getTexture("Splash");
		fadeOutTimer = new Timer(FADE_OUT_TIME);
		fadeOutTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				if(!splashComplete){
					isReady = false;
					fadeOutTimer.reset();
					idleTimer.reset();
					fadeInTimer.reset();					
					fadeInTimer.start();
					splash = Resources.getTexture("Splash2");
					splashComplete = true;
				} else {
					instance.setGameState(GameStates.MAIN_MENU);
				}
			}
		});
		idleTimer = new Timer(IDLE_TIME);
		idleTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				fadeOutTimer.start();
			}
		});
		fadeInTimer = new Timer(FADE_IN_TIME);
		fadeInTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				isReady = true;
				instance.musicPlayer.play();
				idleTimer.start();
			}
		});
		instance.joystick1.addListener(listener);
		
		splashComplete = false;
		isReady = false;
		fadeOutTimer.reset();
		idleTimer.reset();
		fadeInTimer.reset();
		
		fadeInTimer.start();
	}

	@Override
	public void update() {
		fadeOutTimer.tick();
		idleTimer.tick();
		fadeInTimer.tick();
	}

	@Override
	public void render() {
		float opacity = (!isReady) ? fadeInTimer.percentComplete() : 1.0f - fadeOutTimer.percentComplete();
		
		SpriteBatch batch = instance.batch;
		batch.setColor(1, 1, 1, opacity);
		batch.setCamera(camera);
		batch.begin(BlendState.ALPHA);
			batch.draw(0, 0, splash.getWidth(), splash.getHeight(), 0, splash);
		batch.end();
	}

	@Override
	public void cleanUp() {
		instance.joystick1.removeListener(listener);
	}

}
