package com.vecdef.gamestate;

import java.util.ArrayList;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import org.javatroid.graphics.GLUtil;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.javatroid.text.BitmapFont;
import org.lwjgl.opengl.Display;

import com.vecdef.objects.Bullet;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.Entity;
import com.vecdef.objects.EntityManager;
import com.vecdef.objects.MultiplierPiece;
import com.vecdef.objects.Reticle;

public class PlayState{

	enum State{
		PLAYING, PAUSED
	}
	
	Renderer renderer;
	OrthogonalCamera mainCamera;
	OrthogonalCamera hudCamera;
	BitmapFont headerFont;
	BitmapFont hudFont;
	Reticle reticle;
	Vector2f mousePosition;
	
	State state = State.PLAYING;
	
	Scene scene;
	
	EnemySpawner spawner;
	HUDController hudController;
	
	public void initialize() {
		renderer = new Renderer();
		mainCamera = new OrthogonalCamera(Display.getWidth(), Display.getHeight());
		hudCamera = new OrthogonalCamera(Display.getWidth(), Display.getHeight());
		headerFont = Resources.getFont("imagine18");
		hudFont = Resources.getFont("imagine12");
		
		reticle = new Reticle(new Vector4f(1, 1, 1, 1));
		mousePosition = new Vector2f();
	    
	    scene = new Scene();
	    Entity.setScene(scene);
	    spawner = new EnemySpawner();
	    hudController = new HUDController(scene.getPlayer());
	    
	    EntityManager.add(scene.getPlayer());
	    EntityManager.add(reticle);
	}
	
	public void update() {
		if(Input.isKeyPressed(Input.KEY_RETURN)){
			//If game is playing, pause
			if(state == State.PLAYING)
				state = State.PAUSED;
			else if(state == State.PAUSED)
				state = State.PLAYING;
		}
		
		switch(state){
			case PLAYING:
				spawner.trySpawn(scene.getPlayer().getStats().getScore());
				scene.getPlayer().lookAtMouse(mousePosition);
				EntityManager.update(scene.getGrid(), 1);
				break;
			
			case PAUSED:
				break;
		}
		
		scene.getGrid().update();
		
		//update camera translation based on player position
		Vector2f position = scene.getPlayer().getTransform().getTranslation();
		mainCamera.getTranslation().set(position.x, position.y);
		mainCamera.update();
		
		//update mouse position
		mousePosition.x = Input.getMouseX() * Display.getWidth() / 2 + mainCamera.getTranslation().x;
		mousePosition.y = Input.getMouseY() * Display.getHeight() / 2 + mainCamera.getTranslation().y;
		reticle.setPosition(mousePosition);
	}
	
	public void draw(){
		
		SpriteBatch sb = renderer.SpriteBatch();
		ShapeRenderer sr = renderer.ShapeRenderer();
		GLUtil.clear(true, false, false, false);
	
		//Draw Grid and GameObjects
	    sb.setCamera(mainCamera);
	    sr.setCamera(mainCamera);
	    scene.getGrid().draw(sr);
	    EntityManager.draw(sr);
	    
	    //Draw HUD
	    sb.setCamera(hudCamera);
	    hudController.draw(sb);
	    
	    if(state == State.PAUSED){
	    	drawPauseOverlay(sb);
	    }
	}
	
	public void destroy() {
		
	}
	
	void drawPauseOverlay(SpriteBatch renderer){
		renderer.setColor(0, 0, 0, 0.5f);
		renderer.begin();
		renderer.draw(0, 0, Display.getWidth(), Display.getHeight(), 0, Resources.getTexture("blank"));
		renderer.end();
		renderer.setColor(1, 1, 1, 1);
	}
	
	@SuppressWarnings("unchecked")
	void resetGame(){
		state = State.PLAYING;
		
		ArrayList<Bullet> bullets = 			(ArrayList<Bullet>) EntityManager.getEntities(Bullet.class);
		ArrayList<Enemy> enemies = 				(ArrayList<Enemy>) EntityManager.getEntities(Enemy.class);
		ArrayList<MultiplierPiece> pieces = 	(ArrayList<MultiplierPiece>) EntityManager.getEntities(MultiplierPiece.class);
		
		for(Bullet b : bullets)
			b.onDestroy();
		
		for(Enemy e : enemies){
			e.onDestroy(); 
		}
		
		for(MultiplierPiece p : pieces)
			p.onDestroy();
		
		spawner.reset();
		scene.getPlayer().reset ();
		EntityManager.add(reticle);
	}
	
}
