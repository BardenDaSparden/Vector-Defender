package com.vecdef.gamestate;

import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;

import org.javatroid.core.Resources;
import org.javatroid.graphics.GLUtil;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.math.Vector2f;
import org.lwjgl.opengl.Display;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.objects.EnemyFactory;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Masks;

public class PlayState extends GState{

	enum State{
		PLAYING, PAUSED
	}
	
	Renderer renderer;
	OrthogonalCamera camera;
	
	State state = State.PLAYING;
	Scene scene;
	
	SceneRenderer sceneRenderer;
	EnemyFactory factory;
	EnemySpawner spawner;
	HUD hudController;

	long frameCount;
	
	public PlayState(GameState gamestate){
		super(gamestate);
	}
	
	public void initialize() {
		
		renderer = new Renderer();
		camera = new OrthogonalCamera(Display.getWidth(), Display.getHeight());
		
	    scene = new Scene(gamestate.gamepad, gamestate.analyzer);
	    sceneRenderer = new SceneRenderer(scene, renderer);
	    factory = new EnemyFactory(scene);
	    spawner = new EnemySpawner(factory, scene);
	    hudController = new HUD(this, scene, renderer, Display.getWidth(), Display.getHeight());
	    
	    gamestate.player.nextTrack();
	    scene.getPlayer().reset();
	}
	
	public void update() {
		if(gamestate.gamepad.isButtonPressed(Gamepad.START_BUTTON)){
			if(state == State.PLAYING)
				state = State.PAUSED;
			else if(state == State.PAUSED)
				state = State.PLAYING;
		}
		
		switch(state){
			case PLAYING:
				
				Vector2f position = scene.getPlayer().getTransform().getTranslation();
				camera.getTranslation().set(position.x, position.y);
				camera.update();
				
				spawner.trySpawn();
				scene.update();
				frameCount++;
				int liveCount = scene.getPlayer().getStats().getLiveCount();
				if(liveCount < 0){
					scene.getPlayer().getStats().writeHighscore();
					frameCount = 0;
					gamestate.previous();
				}
				break;
			
			case PAUSED:
				break;
		}
	}
	
	public void draw(){
		GLUtil.clear(true, false, false, false);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	    
	    renderer.setCamera(camera);
	    sceneRenderer.draw();
	    hudController.draw();
	    
	    if(state == State.PAUSED){
	    	SpriteBatch sb = renderer.SpriteBatch();
	    	sb.setColor(0, 0, 0, 0.5f);
	    	sb.begin();
	    	sb.draw(0, 0, Display.getWidth(), Display.getHeight(), 0, Resources.getTexture("blank"));
	    	sb.end();
	    	sb.setColor(1, 1, 1, 1);
	    }
	}
	
	public void destroy(){
		sceneRenderer.destroy();
	}
	
	ArrayList<Entity> objectsToDestroy = new ArrayList<Entity>();
	void resetGame(){
		state = State.PLAYING;
		
		scene.getEntitiesByType(Masks.Entities.BULLET, objectsToDestroy);
		scene.getEntitiesByType(Masks.Entities.ENEMY, objectsToDestroy);
		scene.getEntitiesByType(Masks.Entities.MULTIPLIER, objectsToDestroy);
		
		int n = objectsToDestroy.size();
		for(int i = 0; i < n; i++){
			Entity entity = objectsToDestroy.get(i);
			entity.expire();
		}
		objectsToDestroy.clear();
		
		spawner.reset();
		scene.getPlayer().reset();
	}
	
	public long getFrameCount(){
		return frameCount;
	}
	
}
