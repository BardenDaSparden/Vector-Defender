package com.vecdef.gamestate;

import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import org.javatroid.graphics.GLUtil;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.math.Vector2f;
import org.lwjgl.opengl.Display;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.core.MinimFileHandler;
import com.vecdef.objects.EnemyFactory;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Masks;
import com.vecdef.objects.Reticle;

import ddf.minim.Minim;

public class PlayState{

	enum State{
		PLAYING, PAUSED
	}
	
	MinimFileHandler fileHandler;
	Minim minim;
	
	Renderer renderer;
	OrthogonalCamera camera;
	Gamepad gamepad;
	Reticle reticle;
	Vector2f mousePosition;
	State state = State.PLAYING;
	
	Scene scene;
	
	SceneRenderer sceneRenderer;
	EnemyFactory factory;
	EnemySpawner spawner;
	HUDController hudController;
	
	public void initialize() {
		fileHandler = new MinimFileHandler();
		minim = new Minim(fileHandler);
		
		renderer = new Renderer();
		camera = new OrthogonalCamera(Display.getWidth(), Display.getHeight());
		gamepad = new Gamepad();
		reticle = new Reticle(scene);
		mousePosition = new Vector2f();
	    
	    scene = new Scene(gamepad);
	    sceneRenderer = new SceneRenderer(scene, renderer, minim);
	    factory = new EnemyFactory(scene);
	    spawner = new EnemySpawner(factory, scene);
	    hudController = new HUDController(renderer, scene, Display.getWidth(), Display.getHeight());

	    scene.add(reticle);
	}
	
	public void update() {
		if(gamepad.isButtonPressed(Gamepad.START_BUTTON)){
			if(state == State.PLAYING)
				state = State.PAUSED;
			else if(state == State.PAUSED)
				state = State.PLAYING;
		}
		
		switch(state){
			case PLAYING:
				spawner.trySpawn();
				scene.getPlayer().look(mousePosition);
				scene.update();
				break;
			
			case PAUSED:
				break;
		}
		
		Vector2f position = scene.getPlayer().getTransform().getTranslation();
		camera.getTranslation().set(position.x, position.y);
		camera.update();
		
		mousePosition.x = Input.getMouseX() * Display.getWidth() / 2 + camera.getTranslation().x;
		mousePosition.y = Input.getMouseY() * Display.getHeight() / 2 + camera.getTranslation().y;
		reticle.getTransform().getTranslation().set(mousePosition);
		gamepad.poll();
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
	
}
