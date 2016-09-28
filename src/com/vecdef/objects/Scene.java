package com.vecdef.objects;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.barden.input.InputSystem;
import org.barden.input.Joystick;
import org.barden.input.JoystickListener;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.collision.CollisionSystem;
import com.vecdef.collision.ICollidable;
import com.vecdef.physics.IPhysics;
import com.vecdef.physics.PhysicsSystem;

public class Scene {
	
	static DecimalFormat FORMATTER = new DecimalFormat("##.##");
	
	JoystickListener p2Listener;
	JoystickListener p3Listener;
	JoystickListener p4Listener;
	
	InputSystem input;
	
	protected Player player1;
	protected Player player2;
	protected Player player3;
	protected Player player4;
	
	protected Grid grid;
	protected ArrayList<Entity> entities;
	protected ArrayList<Entity> entitiesToRemove;
	
	protected CollisionSystem collision;
	protected PhysicsSystem physics;
	
	private EnemyFactory enemyFactory;
	private EnemySpawner enemySpawner;
	
	public Scene(int width, int height, InputSystem input){
		this.input = input;
		player1 = new Player(this, input.getJoystick(0));
		player2 = new Player(this, input.getJoystick(1));
		player3 = new Player(this, input.getJoystick(2));
		player4 = new Player(this, input.getJoystick(3));
		grid = new Grid(width + 240, height + 160, 30, 30);
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		collision = new CollisionSystem();
		physics = new PhysicsSystem();
		enemyFactory = new EnemyFactory(this);
		enemySpawner = new EnemySpawner(enemyFactory, this);
		
		p2Listener = new JoystickListener() {
			
			@Override
			public void onButtonRelease(int button) {
				if(button == Joystick.BUTTON_START && !player2.hasJoined()){
					addPlayer(player2, 0, 0);
					player2.setJoined(true);
				}
			}
			
			@Override
			public void onButtonPress(int button) {
				// TODO Auto-generated method stub
				
			}
		};
		
		p3Listener = new JoystickListener() {
			
			@Override
			public void onButtonRelease(int button) {
				if(button == Joystick.BUTTON_START && !player3.hasJoined()){
					addPlayer(player3, 0, 0);
					player3.setJoined(true);
				}
			}
			
			@Override
			public void onButtonPress(int button) {
				// TODO Auto-generated method stub
				
			}
		};
		
		p4Listener = new JoystickListener() {
			
			@Override
			public void onButtonRelease(int button) {
				if(button == Joystick.BUTTON_START && !player4.hasJoined()){
					addPlayer(player4, 0, 0);
					player4.setJoined(true);
				}
			}
			
			@Override
			public void onButtonPress(int button) {
				// TODO Auto-generated method stub
				
			}
		};
		
		input.getJoystick(1).addListener(p2Listener);
		input.getJoystick(2).addListener(p3Listener);
		input.getJoystick(3).addListener(p4Listener);
		
		addPlayer(player1, 0, 0);
	}
	
	final Vector3f SPAWN_FORCE = new Vector3f(0, 0, -250);
	final float SPAWN_FORCE_RADIUS = 450;
	Vector3f spawnForcePosition = new Vector3f();
	void addPlayer(Player player, float x, float y){
		player.getTransform().getTranslation().set(x, y);
		spawnForcePosition.set(x, y, 0);
		add(player);
		grid.applyDirectedForce(SPAWN_FORCE, spawnForcePosition, SPAWN_FORCE_RADIUS);
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
	
	public void destroy(){
		input.getJoystick(1).removeListener(p2Listener);
		input.getJoystick(2).removeListener(p3Listener);
		input.getJoystick(3).removeListener(p4Listener);
	}
	
	public void reset(){
		enemySpawner.reset();
		player1.reset();
		player1.getTransform().getTranslation().set(0, 0);
	}
	
	public Player getNearestPlayer(float x, float y){
		//TODO implement
		return null;
	}
	
	public boolean isMultiplayer(){
		return (player2.hasJoined() || player3.hasJoined() || player4.hasJoined());
	}
	
	public Player getPlayer(){
		return player1;
	}
	
	public Player getPlayer2(){
		return player2;
	}
	
	public Player getPlayer3(){
		return player3;
	}
	
	public Player getPlayer4(){
		return player4;
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
