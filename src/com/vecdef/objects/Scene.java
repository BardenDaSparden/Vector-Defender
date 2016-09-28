package com.vecdef.objects;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.barden.input.Joystick;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.collision.CollisionSystem;
import com.vecdef.collision.ICollidable;
import com.vecdef.physics.IPhysics;
import com.vecdef.physics.PhysicsSystem;

public class Scene {
	
	static DecimalFormat FORMATTER = new DecimalFormat("##.##");
	
	protected Player player;
	protected Grid grid;
	protected ArrayList<Entity> entities;
	protected ArrayList<Entity> entitiesToRemove;
	
	protected CollisionSystem collision;
	protected PhysicsSystem physics;
	
	private EnemyFactory enemyFactory;
	private EnemySpawner enemySpawner;
	
	public Scene(int width, int height, Joystick joystick){
		player = new Player(this, joystick);
		grid = new Grid(width, height, 20, 20);
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		collision = new CollisionSystem();
		physics = new PhysicsSystem();
		enemyFactory = new EnemyFactory(this);
		enemySpawner = new EnemySpawner(enemyFactory, this);
		
		add(player);
		grid.applyDirectedForce(new Vector3f(0, 0, -400), new Vector3f(0, 0, 0), 500);
	}
	
	public void add(Entity entity){
		collision.add(entity);
		physics.add(entity);
		entities.add(entity);
	}
	
	public void addCollidable(ICollidable collidable){
		collision.add(collidable);
	}
	
	public void removeCollidable(ICollidable collidable){
		collision.remove(collidable);
	}
	
	public void addPhysics(IPhysics physicsObj ){
		physics.add(physicsObj);
	}
	
	public void removePhysics(IPhysics physicsObj ){
		physics.remove(physicsObj);
	}
	
	public void remove(Entity entity){
		collision.remove(entity);
		physics.remove(entity);
		entities.remove(entity);
	}
	
	public void getEntitiesByType(int mask, ArrayList<Entity> list){
		int n = entities.size();
		for(int i = 0; i < n; i++){
			Entity entity = entities.get(i);
			if((entity.getEntityType() & mask) == 0x00)
				continue;
			list.add(entity);
		}
	}
	
	public void getEntitiesByType(Vector2f position, int radius, int mask, ArrayList<Entity> list){
		int n = entities.size();
		for(int i = 0; i < n; i++){
			Entity entity = entities.get(i);
			if((entity.getEntityType() & mask) == 0x00)
				continue;
			
			//sqrt((dx * dx) + (dy * dy)) = radius
			//(dx * dx) + (dy * dy) = radius^2
			Vector2f ePosition = entity.getTransform().getTranslation();
			float dx = ePosition.x - position.x;
			float dy = ePosition.y - position.y;
			float lenSqr = (dx * dx) + (dy * dy);
			float rSqr = radius * radius;
			if(lenSqr > rSqr)
				continue;
			
			list.add(entity);
		}
	}
	
	public void update(){
		long startTime = System.nanoTime();
		enemySpawner.trySpawn();
		long endTime = System.nanoTime();
		double millis = (endTime - startTime) / 1000000D;
		//System.out.println("Enemy Spawner : " +  FORMATTER.format(millis) + "ms");
		
		startTime = System.nanoTime();
		collision.checkCollision();
		endTime = System.nanoTime();
		millis = (endTime - startTime) / 1000000D;
		//System.out.println("Collision Solver : " +  FORMATTER.format(millis) + "ms");
		
		startTime = System.nanoTime();
		physics.integrate();
		endTime = System.nanoTime();
		millis = (endTime - startTime) / 1000000D;
		//System.out.println("Physics : " +  FORMATTER.format(millis) + "ms");
		
		startTime = System.nanoTime();
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			if(entity.isExpired()){
				entity.destroy();
				remove(entity);
			} else {
				entity.update();
			}
		}
		endTime = System.nanoTime();
		millis = (endTime - startTime) / 1000000D;
		//System.out.println("Entity Updates : " +  FORMATTER.format(millis) + "ms");
		
		startTime = System.nanoTime();
		grid.update();
		endTime = System.nanoTime();
		millis = (endTime - startTime) / 1000000D;
		//System.out.println("Grid Update : " +  FORMATTER.format(millis) + "ms");
	}
	
	public void reset(){
		enemySpawner.reset();
		player.reset();
		player.getTransform().getTranslation().set(0, 0);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public ArrayList<Entity> getAllEntities(){
		return entities;
	}
	
	public int getPhysicsObjectCount(){
		return physics.numObjects();
	}
	
	public int getCollisionObjectCount(){
		return collision.numObjects();
	}
	
	public int getEntityCount(){
		return entities.size();
	}
}
