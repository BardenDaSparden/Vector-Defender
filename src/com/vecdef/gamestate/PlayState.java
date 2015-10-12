package com.vecdef.gamestate;

import java.util.ArrayList;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
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
import com.vecdef.objects.Grid;
import com.vecdef.objects.MultiplierPiece;
import com.vecdef.objects.Player;
import com.vecdef.objects.Reticle;

public class PlayState{

	enum State{
		PLAYING, PAUSED, GAMEOVER
	}
	
	Renderer renderer;
	OrthogonalCamera mainCamera;
	OrthogonalCamera hudCamera;
	BitmapFont headerFont;
	BitmapFont hudFont;
	Reticle reticle;
	Vector2f mousePosition;
	
	int GAMEOVER_TIME = 240;
	Timer gameoverTimer;
	
	State state = State.PLAYING;
	
	Scene scene;
	Player player;
	
	EnemySpawner spawner;
	HUDController hudController;
	
	Grid grid;
	
	public void initialize() {
		renderer = new Renderer();
		mainCamera = new OrthogonalCamera(Display.getWidth(), Display.getHeight());
		hudCamera = new OrthogonalCamera(Display.getWidth(), Display.getHeight());
		headerFont = Resources.getFont("imagine18");
		hudFont = Resources.getFont("imagine12");
		
		reticle = new Reticle(new Vector4f(1, 1, 1, 1));
		mousePosition = new Vector2f();
	    grid = new Grid(1920, 1080, 50, 50);
	    
	    gameoverTimer = new Timer(GAMEOVER_TIME);
	    gameoverTimer.setCallback(new TimerCallback() {
			public void execute(Timer timer) {
				timer.reset();
				resetGame();
			}
		});
	    
	    scene = new Scene();
	    Entity.setScene(scene);
	    
	    player = scene.getPlayer();
	    spawner = new EnemySpawner();
	    hudController = new HUDController(player);
	    
	    EntityManager.add(player);
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
				spawner.trySpawn(player.getStats().getScore());
				player.lookAtMouse(mousePosition);
				EntityManager.update(grid, 1);
				if(player.getStats().getLiveCount() <= 0){
					state = State.GAMEOVER;
					gameoverTimer.start();
				}
				break;
			
			case PAUSED:
				break;
			
			case GAMEOVER:
				gameoverTimer.tick();
				break;
		}
		
		grid.update();
		
		//update camera translation based on player position
		Vector2f position = player.getTransform().getTranslation();
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
	    grid.draw(sr);
	    EntityManager.draw(sr);
	    
	    //Draw HUD
	    sb.setCamera(hudCamera);
	    hudController.draw(sb);
	    
	    if(state == State.PAUSED){
	    	drawPauseOverlay(sb);
	    } else if(state == State.GAMEOVER){
	    	drawGameoverOverlay(sb);
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
	
	void drawGameoverOverlay(SpriteBatch renderer){
		renderer.setColor(1, 1, 1, 1);
		renderer.begin();
    	
    	String s = "Game Over";
  		hudFont.drawString(-hudFont.getWidth(s) / 2.0F, 30.0F, s, renderer);

  		s = "Your Score : " + player.getStats().getScore();
  		hudFont.drawString(-hudFont.getWidth(s) / 2.0F, 0.0F, s, renderer);
  		
  		renderer.end();
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
		player.reset ();
		EntityManager.add(reticle);
	}
	
}
