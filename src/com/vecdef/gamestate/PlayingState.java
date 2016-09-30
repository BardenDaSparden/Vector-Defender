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

import com.vecdef.objects.HUD;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;
import com.vecdef.rendering.HUDRenderer;
import com.vecdef.rendering.Renderer;
import com.vecdef.rendering.SceneRenderer;

public class PlayingState extends GameState {

	public PlayingState(VectorDefender instance) {
		super(instance);
	}
	
	OrthogonalCamera camera;
	OrthogonalCamera overlayCamera;
	
	Scene scene;
	SceneRenderer sceneRenderer;
	
	HUD hud;
	HUDRenderer hudRenderer;
	
	CubicInterpolator interpolator = new CubicInterpolator(new Vector2f(0.35f, 0.0f), new Vector2f(0.75f, 1.0f));
	Texture white;
	final int FADE_IN_TIME = 30;
	Timer fadeInTimer;
	
	@Override
	public void initialize() {
		
		float width = instance.settings.width;
		float height = instance.settings.height;
		Renderer renderer = instance.renderer;
		
		camera = new OrthogonalCamera(width, height);
		overlayCamera = new OrthogonalCamera(width, height);
	    scene = new Scene((int)width, (int)height, instance.input);
	    sceneRenderer = new SceneRenderer((int)width, (int)height, scene, instance.renderer);
	    hud = new HUD(scene);
	    hudRenderer = new HUDRenderer((int)width, (int)height, renderer);
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
		if(scene.isMultiplayer()){
			Vector2f cameraPosition = camera.getTranslation();
			
			int playerCount = 1;
			float x = 0;
			float y = 0;
			
			Player p1 = scene.getPlayer();
			Player p2 = scene.getPlayer2();
			Player p3 = scene.getPlayer3();
			Player p4 = scene.getPlayer4();
			
			Vector2f p1Pos = p1.getTransform().getTranslation();
			x += p1Pos.x;
			y += p1Pos.y;
			
			if(p2.hasJoined() && p2.isAlive()){
				Vector2f p2Pos = p2.getTransform().getTranslation();
				x += p2Pos.x;
				y += p2Pos.y;
				playerCount++;
			}
			
			if(p3.hasJoined() && p3.isAlive()){
				Vector2f p3Pos = p3.getTransform().getTranslation();
				x += p3Pos.x;
				y += p3Pos.y;
				playerCount++;
			}
			
			if(p4.hasJoined() && p4.isAlive()){
				Vector2f p4Pos = p4.getTransform().getTranslation();
				x += p4Pos.x;
				y += p4Pos.y;
				playerCount++;
			}
			
			x /= (float)playerCount;
			y /= (float)playerCount;
			
			float camX = interpolator.interpolate(cameraPosition.x, x, 0.10f);
			float camY = interpolator.interpolate(cameraPosition.y, y, 0.10f);
			camera.getTranslation().set(camX, camY);
			
		} else {
			Vector2f cameraPosition = camera.getTranslation();
			Vector2f playerPosition = scene.getPlayer().getTransform().getTranslation();
			float newCameraX = interpolator.interpolate(cameraPosition.x, playerPosition.x, 0.10f);
			float newCameraY = interpolator.interpolate(cameraPosition.y, playerPosition.y, 0.10f);
			camera.getTranslation().set(newCameraX, newCameraY);
		}
		camera.update();
		
		scene.update();
		
//		int liveCount = scene.getPlayer().getStats().getLiveCount();
//		if(liveCount <= 0){
//			scene.getPlayer().getStats().writeHighscore();
//			scene.reset();
//			instance.setGameState(GameStates.MAIN_MENU);
//		}
		
		fadeInTimer.tick();
	}

	@Override
	public void render() {
		instance.renderer.setCamera(camera);
	    sceneRenderer.draw();
	    hudRenderer.draw(hud);
	    
	    float opacity = 1.0f - fadeInTimer.percentComplete();
	    SpriteBatch batch = instance.batch;
	    batch.setCamera(overlayCamera);
	    batch.setColor(0, 0, 0, opacity);
	    batch.begin(BlendState.ALPHA);
	    	batch.draw(0, 0, instance.settings.width, instance.settings.height, 0, white);
	    batch.end();
	    batch.setCamera(camera);
	}

	@Override
	public void cleanUp() {
		scene.destroy();
	}

	
	
}
