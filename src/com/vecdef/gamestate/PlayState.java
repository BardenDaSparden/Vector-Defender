package com.vecdef.gamestate;

import java.util.ArrayList;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import org.javatroid.graphics.GLUtil;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.lwjgl.opengl.Display;

import com.vecdef.objects.Bullet;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EntityManager;
import com.vecdef.objects.MultiplierPiece;
import com.vecdef.objects.Reticle;

public class PlayState{

	enum State{
		PLAYING, PAUSED
	}
	
	Renderer renderer;
	OrthogonalCamera camera;
	
	Reticle reticle;
	Vector2f mousePosition;
	
	State state = State.PLAYING;
	
	Scene scene;
	
	EnemySpawner spawner;
	HUDController hudController;
	
	public void initialize() {
		renderer = new Renderer();
		camera = new OrthogonalCamera(Display.getWidth(), Display.getHeight());
		
		reticle = new Reticle(new Vector4f(1, 1, 1, 1));
		mousePosition = new Vector2f();
	    
	    scene = new Scene(renderer);
	    spawner = new EnemySpawner(scene);
	    hudController = new HUDController(scene, Display.getWidth(), Display.getHeight());
	    
	    EntityManager.add(scene.getPlayer());
	    EntityManager.add(reticle);
	    
	    //scene.add(player);
	    //scene.add(reticle);
	}
	
	public void update() {
		if(Input.isKeyPressed(Input.KEY_ESCAPE)){
			if(state == State.PLAYING)
				state = State.PAUSED;
			else if(state == State.PAUSED)
				state = State.PLAYING;
		}
		
		switch(state){
			case PLAYING:
				spawner.trySpawn();
				scene.getPlayer().look(mousePosition);
				EntityManager.update(scene.getGrid());
				break;
			
			case PAUSED:
				break;
		}
		
		scene.getGrid().update();
		
		Vector2f position = scene.getPlayer().getTransform().getTranslation();
		camera.getTranslation().set(position.x, position.y);
		camera.update();
		
		mousePosition.x = Input.getMouseX() * Display.getWidth() / 2 + camera.getTranslation().x;
		mousePosition.y = Input.getMouseY() * Display.getHeight() / 2 + camera.getTranslation().y;
		reticle.getTransform().getTranslation().set(mousePosition);
	}
	
	public void draw(){
		GLUtil.clear(true, false, false, false);
		SpriteBatch sb = renderer.SpriteBatch();
		ShapeRenderer sr = renderer.ShapeRenderer();
		
	    renderer.setCamera(camera);
	    scene.getGrid().draw(sr);
	    EntityManager.draw(sr);
	    hudController.draw(renderer);
	    
	    if(state == State.PAUSED){
	    	sb.setColor(0, 0, 0, 0.5f);
	    	sb.begin();
	    	sb.draw(0, 0, Display.getWidth(), Display.getHeight(), 0, Resources.getTexture("blank"));
	    	sb.end();
	    	sb.setColor(1, 1, 1, 1);
	    }
	}
	
	public void destroy() {
		
	}
	
	@SuppressWarnings("unchecked")
	void resetGame(){
		state = State.PLAYING;
		
		ArrayList<Bullet> bullets = 			(ArrayList<Bullet>) EntityManager.getEntities(Bullet.class);
		ArrayList<Enemy> enemies = 				(ArrayList<Enemy>) EntityManager.getEntities(Enemy.class);
		ArrayList<MultiplierPiece> pieces = 	(ArrayList<MultiplierPiece>) EntityManager.getEntities(MultiplierPiece.class);
		
		for(Bullet b : bullets)
			b.destroy();
		
		for(Enemy e : enemies){
			e.destroy(); 
		}
		
		for(MultiplierPiece p : pieces)
			p.destroy();
		
		spawner.reset();
		scene.getPlayer().reset ();
		EntityManager.add(reticle);
	}
	
}
