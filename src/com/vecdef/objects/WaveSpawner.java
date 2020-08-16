package com.vecdef.objects;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

public class WaveSpawner {
	
	Grid grid;
	EnemyFactory factory;
	int spawnTime = 240;
	Timer spawnTimer = new Timer(spawnTime);
	
	int spawnType = EnemyFactory.ZOOMER;
	Vector2f SPAWN_TL = new Vector2f();
	Vector2f SPAWN_BL = new Vector2f();
	Vector2f SPAWN_BR = new Vector2f();
	Vector2f SPAWN_TR = new Vector2f();
	
	public WaveSpawner(Scene scene, EnemyFactory enemyFactory){
		grid = scene.getGrid();
		factory = enemyFactory;
		spawnTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				timer.restart();
				spawnType = FastMath.randomi(EnemyFactory.WANDERER, EnemyFactory.BLACK_HOLE + 1);
				spawn(spawnType);
				if(spawnTime > 120){
					spawnTime--;
					timer.setDuration(spawnTime);
				}
			}
		});
		spawnTimer.start();
		
		int gridWidth = grid.getWidth();
		int gridHeight = grid.getHeight();
		int padding = 200;
		SPAWN_TL.set(-gridWidth / 2.0f + padding, gridHeight / 2.0f - padding);
		SPAWN_BL.set(-gridWidth / 2.0f + padding, -gridHeight / 2.0f + padding);
		SPAWN_BR.set(gridWidth / 2.0f - padding, -gridHeight / 2.0f + padding);
		SPAWN_TR.set(gridWidth / 2.0f - padding, gridHeight / 2.0f - padding);
		//spawn(spawnType);
	}
	
	void spawn(int type){
		factory.create(type, SPAWN_TL);
		factory.create(type, SPAWN_BL);
		factory.create(type, SPAWN_TR);
		factory.create(type, SPAWN_BR);
	}
	
	public void update(){
		spawnTimer.tick();
	}
	
}