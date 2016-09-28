package com.vecdef.gamestate;

import org.javatroid.core.Resources;
import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.math.CubicInterpolator;
import org.javatroid.math.Vector2f;

import com.vecdef.objects.HUDController;
import com.vecdef.objects.Scene;
import com.vecdef.rendering.SceneRenderer;

public class PlayingState extends GameState {

	public PlayingState(VectorDefender instance) {
		super(instance);
	}
	
	OrthogonalCamera camera;
	OrthogonalCamera hudCamera;
	SceneRenderer sceneRenderer;
	
	Scene scene;
	HUDController hudController;
	CubicInterpolator interpolator = new CubicInterpolator(new Vector2f(0.0f, 0.5f), new Vector2f(0.5f, 1.0f));
	
	Texture white;
	final int FADE_IN_TIME = 30;
	Timer fadeInTimer;
	
	@Override
	public void initialize() {
		camera = new OrthogonalCamera(instance.settings.width, instance.settings.height);
		hudCamera = new OrthogonalCamera(instance.settings.width, instance.settings.height);
	    scene = new Scene(instance.settings.width, instance.settings.height, instance.joystick1);
	    sceneRenderer = new SceneRenderer(instance.settings.width, instance.settings.height, scene, instance.renderer);
	    hudController = new HUDController(scene, instance.renderer, instance.settings.width, instance.settings.height);
	    white = Resources.getTexture("blank");
	    fadeInTimer = new Timer(FADE_IN_TIME);
	    fadeInTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				// TODO Auto-generated method stub
				
			}
		});
	    fadeInTimer.start();
	}

	@Override
	public void update() {
		Vector2f cameraPosition = camera.getTranslation();
		Vector2f playerPosition = scene.getPlayer().getTransform().getTranslation();
		float newCameraX = interpolator.interpolate(cameraPosition.x, playerPosition.x, 0.12f);
		float newCameraY = interpolator.interpolate(cameraPosition.y, playerPosition.y, 0.12f);
		camera.getTranslation().set(newCameraX, newCameraY);
		camera.update();
		
		scene.update();
		
		int liveCount = scene.getPlayer().getStats().getLiveCount();
		if(liveCount < 0){
			scene.getPlayer().getStats().writeHighscore();
			scene.reset();
			instance.setGameState(GameStates.MAIN_MENU);
		}
		
		fadeInTimer.tick();
	}

	@Override
	public void render() {
		instance.renderer.setCamera(camera);
	    sceneRenderer.draw();
	    hudController.draw();
	    
	    float opacity = 1.0f - fadeInTimer.percentComplete();
	    SpriteBatch batch = instance.batch;
	    batch.setCamera(hudCamera);
	    batch.setColor(0, 0, 0, opacity);
	    batch.begin(BlendState.ALPHA);
	    	batch.draw(0, 0, instance.settings.width, instance.settings.height, 0, white);
	    batch.end();
	    batch.setCamera(camera);
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	
	
}
