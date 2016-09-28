package com.vecdef.objects;


import java.util.ArrayList;
import java.util.Random;

import org.javatroid.audio.AudioPlayer;
import org.javatroid.audio.Sound;
import org.javatroid.core.Resources;
import org.javatroid.math.Vector2f;

import com.vecdef.util.Masks;

public class EnemySpawner{
	
	static final int MAX_ENEMIES = 246;
	static final int MAX_BLACK_HOLES = 4;
	
	static final float START_SPAWN_CHANCE = 120.0f;
	static final float MIN_SPAWN_CHANCE = 35.0f;
	
	Random random = new Random();
	
	ArrayList<Entity> allEnemies;
	ArrayList<Entity> allBlackholes;
	
	float basicUnitSpawnChance = START_SPAWN_CHANCE;
	float stalkerUnitSpawnChance = 300.0F;
	float specialUnitSpawnChance = 500.0f;
	float blackholeSpawnChance = 1200.0F;
	
	EnemyFactory factory;
	Scene scene;
	Player player;
	Sound spawnSound;
	boolean bSpawned = false;
	
	public EnemySpawner(EnemyFactory factory, Scene scene){
		this.factory = factory;
		this.scene = scene;
		this.player = scene.getPlayer();
		allEnemies = new ArrayList<Entity>();
		allBlackholes = new ArrayList<Entity>();
		spawnSound = Resources.getSound("spawn1");
	}
	
	public void trySpawn(){
		if(player.isDead())
			return;
		
		bSpawned = false;
		
		long score = player.getStats().getScore();
		
		allEnemies.clear();
		scene.getEntitiesByType(Masks.Entities.ENEMY, allEnemies);
		int enemyCount = allEnemies.size();
		
		allBlackholes.clear();
		scene.getEntitiesByType(Masks.Entities.BLACK_HOLE, allBlackholes);
		int blackholeCount = allBlackholes.size();
		
		if(enemyCount >= MAX_ENEMIES)
			return;
		
		if(canSpawn(basicUnitSpawnChance)){
			factory.createWanderer(getSpawnPosition());
			bSpawned = true;
		}
		
//		if(canSpawn(blackholeSpawnChance)){
//			factory.createBlackHole(getSpawnPosition());
//		}
		
		if(score >= 5000 && canSpawn(basicUnitSpawnChance)){
			factory.createSeeker(getSpawnPosition());
			if(canSpawn(blackholeSpawnChance)){
				factory.createBlackHole(getSpawnPosition());
				bSpawned = true;
			}
		}
		
		if(score >= 15000 && canSpawn(stalkerUnitSpawnChance)){
			factory.createFollower(getSpawnPosition());
			bSpawned = true;
		}
		
		if(score >= 30000){
			if(canSpawn(specialUnitSpawnChance)){
				factory.createPrototype(getSpawnPosition());
				bSpawned = true;
			}
		}
		
		if(blackholeCount < MAX_BLACK_HOLES){
			if(score > 50000){
				if(canSpawn(blackholeSpawnChance)){
					factory.createBlackHole(getSpawnPosition());
					bSpawned = true;
				}
			}
		}
		
		if(bSpawned)
			AudioPlayer.instance().play(spawnSound);
		
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
	      position.sub(playerPosition).lengthSquared() < 90000.0F);

	    return position;
	}
	
	boolean canSpawn(float chance){
		return random.nextInt((int)chance) == 0;
	}
	
	public void reset(){
		 basicUnitSpawnChance = START_SPAWN_CHANCE;
	}
}