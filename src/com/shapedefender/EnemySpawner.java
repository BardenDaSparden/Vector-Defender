package com.shapedefender;


import java.util.ArrayList;
import java.util.Random;

import org.javatroid.core.Window;
import org.javatroid.math.Vector2f;

import com.shapedefender.objects.Enemy;
import com.shapedefender.objects.Entity;
import com.shapedefender.objects.EntityManager;
import com.shapedefender.objects.Player;

public class EnemySpawner{
	
	static final int MAX_ENEMIES = 246;
	static final int MAX_BLACK_HOLES = 4;
	
	static final float START_SPAWN_CHANCE = 60.0f;
	static final float MIN_SPAWN_CHANCE = 16.0f;
	
	ArrayList<Enemy> spawnedBlackHoles = new ArrayList<Enemy>();
	Random random = new Random();
	float basicUnitSpawnChance = START_SPAWN_CHANCE;
	float stalkerUnitSpawnChance = 300.0F;
	float specialUnitSpawnChance = 500.0f;
	float blackholeSpawnChance = 1200.0F;
	
	public void trySpawn(int score){
		int entityCount = EntityManager.getEntities(Enemy.class).size();
		Player player = Entity.getScene().getPlayer();
		
		if((!player.isDead()) && (entityCount < MAX_ENEMIES))
	    {
			
	      if (random.nextInt((int)basicUnitSpawnChance) == 0) {
	        EntityManager.add(Enemy.createWanderer(getSpawnPosition()));
	      }
	      
	      if ((score >= 5000) && 
	        (random.nextInt((int)basicUnitSpawnChance) == 0)) {
	        EntityManager.add(Enemy.createSeeker(getSpawnPosition()));
	      }

	      if (score >= 10000){
	    	  if (random.nextInt((int)stalkerUnitSpawnChance) == 0) {
		          EntityManager.add(Enemy.createFollower(getSpawnPosition()));
		        }
	      }
	      
	      if(score >= 20000){
	    	  if(random.nextInt((int)specialUnitSpawnChance) == 0){
		    	  EntityManager.add(Enemy.createPrototype(getSpawnPosition()));
		      }if(random.nextInt((int)specialUnitSpawnChance) == 0){
		    	  EntityManager.add(Enemy.createZoomer(getSpawnPosition()));
		      }
	      }
	      
	      if ((score >= 30000 && basicUnitSpawnChance > 30.0f)){
		      
	    	  if((random.nextInt((int)blackholeSpawnChance) == 0)){
	    		  for(int i = 0; i < spawnedBlackHoles.size(); i++){
	    			  Enemy e = spawnedBlackHoles.get(i);
	    			  if(e.isExpired())
	    				  spawnedBlackHoles.remove(i);
	    		  }
	    		  if(spawnedBlackHoles.size() < MAX_BLACK_HOLES){
			    	  Enemy enemy = Enemy.createBlackHole(getSpawnPosition());
			    	  EntityManager.add(enemy);
			    	  spawnedBlackHoles.add(enemy);
			      }
	    	  }
	    	  
		      
	      }

	      if (basicUnitSpawnChance > MIN_SPAWN_CHANCE)
	        basicUnitSpawnChance -= 0.004F;
	    }
	}
	
	Vector2f getSpawnPosition(){
		Player player = Entity.getScene().getPlayer();
		
		Vector2f position;
	    Vector2f playerPosition = player.getTransform().getTranslation();
		do
	      position = new Vector2f(-Window.getWidth() / 2 + 40 + random.nextInt(Window.getWidth() - 80), -Window.getHeight() / 2 + 40 + random.nextInt(Window.getHeight() - 80));
	    while (
	      position.sub(playerPosition).lengthSquared() < 40000.0F);

	    return position;
	}
	
	public void reset(){
		 basicUnitSpawnChance = START_SPAWN_CHANCE;
	}
}