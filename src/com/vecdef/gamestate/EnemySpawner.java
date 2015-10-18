package com.vecdef.gamestate;


import java.util.ArrayList;
import java.util.Random;

import org.javatroid.math.Vector2f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Masks;
import com.vecdef.objects.Player;

public class EnemySpawner{
	
	static final int MAX_ENEMIES = 246;
	static final int MAX_BLACK_HOLES = 4;
	
	static final float START_SPAWN_CHANCE = 60.0f;
	static final float MIN_SPAWN_CHANCE = 16.0f;
	
	Random random = new Random();
	
	ArrayList<Entity> allEnemies;
	ArrayList<Entity> allBlackholes;
	
	float basicUnitSpawnChance = START_SPAWN_CHANCE;
	float stalkerUnitSpawnChance = 300.0F;
	float specialUnitSpawnChance = 500.0f;
	float blackholeSpawnChance = 1200.0F;
	
	Scene scene;
	Player player;
	
	public EnemySpawner(Scene scene){
		this.scene = scene;
		player = scene.getPlayer();
		allEnemies = new ArrayList<Entity>();
		allBlackholes = new ArrayList<Entity>();
	}
	
	public void trySpawn(){
		if(player.isDead())
			return;
		
		int score = scene.getPlayer().getStats().getScore();
		
		allEnemies.clear();
		scene.getEntitiesByType(Masks.Entities.ENEMY, allEnemies);
		int enemyCount = allEnemies.size();
		
		allBlackholes.clear();
		scene.getEntitiesByType(Masks.Entities.BLACK_HOLE, allBlackholes);
		int blackholeCount = allBlackholes.size();
		
		if(enemyCount >= MAX_ENEMIES)
			return;
		
		if(blackholeCount < MAX_BLACK_HOLES){
			if(canSpawn(blackholeSpawnChance)){
				Enemy enemy = Enemy.createBlackHole(getSpawnPosition(), scene);
				scene.add(enemy);
			}
		}
		
		if(canSpawn(basicUnitSpawnChance)){
			Enemy enemy = Enemy.createWanderer(getSpawnPosition(), scene);
			scene.add(enemy);
		}
		
		if(score >= 5000 && canSpawn(basicUnitSpawnChance)){
			Enemy enemy = Enemy.createSeeker(getSpawnPosition(), scene);
			scene.add(enemy);
		}
		
		if(score >= 15000 && canSpawn(stalkerUnitSpawnChance)){
			Enemy enemy = Enemy.createFollower(getSpawnPosition(), scene);
			scene.add(enemy);
		}
		
		if(score >= 30000){
			if(canSpawn(specialUnitSpawnChance)){
				Enemy enemy = Enemy.createPrototype(getSpawnPosition(), scene);
				scene.add(enemy);
			}
			if(canSpawn(specialUnitSpawnChance)){
				Enemy enemy = Enemy.createZoomer(getSpawnPosition(), scene);
				scene.add(enemy);
			}
		}
		
		if(blackholeCount < MAX_BLACK_HOLES){
			if(score > 45000 && basicUnitSpawnChance > 30.0f){
				if(canSpawn(blackholeSpawnChance)){
					Enemy enemy = Enemy.createBlackHole(getSpawnPosition(), scene);
					scene.add(enemy);
				}
			}
		}
		
		if(basicUnitSpawnChance > MIN_SPAWN_CHANCE)
			basicUnitSpawnChance -= 0.004f;
	}
	
	Vector2f getSpawnPosition(){
		Player player = scene.getPlayer();
		Grid grid = scene.getGrid();
		
		int regionWidth = grid.getWidth();
		int regionHeight = grid.getHeight();
		
		Vector2f position;
	    Vector2f playerPosition = player.getTransform().getTranslation();
		do
	      position = new Vector2f(-regionWidth / 2 + 40 + random.nextInt(regionWidth - 80), -regionHeight / 2 + 40 + random.nextInt(regionHeight - 80));
	    while (
	      position.sub(playerPosition).lengthSquared() < 40000.0F);

	    return position;
	}
	
	boolean canSpawn(float chance){
		return random.nextInt((int)chance) == 0;
	}
	
	public void reset(){
		 basicUnitSpawnChance = START_SPAWN_CHANCE;
	}
}