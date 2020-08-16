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
	
	BulletPool bulletPool;
	EnemyPool enemyPool;
	MultiplierPool multiplierPool;
	ParticlePool particlePool;
	SpawnEffectPool effectPool;
	
	private EnemyFactory enemyFactory;
	private WaveSpawner enemySpawner;
	
	public Scene(int width, int height, InputSystem input){
		this.input = input;
		player1 = new Player(this, 0);
		player2 = new Player(this, 1);
		player3 = new Player(this, 2);
		player4 = new Player(this, 3);
		grid = new Grid(width, height, 25, 25);
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		collision = new CollisionSystem();
		physics = new PhysicsSystem();
		
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
				
			}
		};
		
		bulletPool = new BulletPool(this);
		enemyPool = new EnemyPool(this);
		multiplierPool = new MultiplierPool(this);
		particlePool = new ParticlePool(this);
		effectPool = new SpawnEffectPool(this);
		
		enemyFactory = new EnemyFactory(this, enemyPool);
		enemySpawner = new WaveSpawner(this, enemyFactory);
		
		input.getJoystick(1).addListener(p2Listener);
		input.getJoystick(2).addListener(p3Listener);
		input.getJoystick(3).addListener(p4Listener);
	}
	
	public void initialize(){
		addPlayer(player1, 0, 0);
		
		if(player2.hasJoined())
			addPlayer(player2, 0, -150);
		
		if(player3.hasJoined())
			addPlayer(player3, -150, 0);
		
		if(player4.hasJoined())
			addPlayer(player4, 150, 0);
		
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
			if((entity.getEntityType() & mask) == 0x00 || entity.isRecycled())
				continue;
			list.add(entity);
		}
	}
	
	public void getEntitiesByType(Vector2f position, int radius, int mask, ArrayList<Entity> list){
		int n = entities.size();
		for(int i = 0; i < n; i++){
			Entity entity = entities.get(i);
			if((entity.getEntityType() & mask) == 0x00 || entity.isRecycled())
				continue;
			
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
		enemySpawner.update();
		physics.integrate();
		collision.checkCollision();
		
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			if(entity.isRecycled())
				continue;
			entity.update();
		}
		
		grid.update();
	}
	
	public void destroy(){
		input.getJoystick(1).removeListener(p2Listener);
		input.getJoystick(2).removeListener(p3Listener);
		input.getJoystick(3).removeListener(p4Listener);
	}
	
	public void reset(){
		
		
		player1.reset();
		player2.reset();
		player3.reset();
		player4.reset();
		
		
		player1.getTransform().getTranslation().set(0, 0);
		player2.getTransform().getTranslation().set(0, 0);
		player3.getTransform().getTranslation().set(0, 0);
		player4.getTransform().getTranslation().set(0, 0);
	}
	
	public Player getNearestPlayer(float x, float y){
		float minDist = Float.MAX_VALUE;
		Player player = null;
		Vector2f translation = player1.getTransform().getTranslation();
		float dx = (x - translation.x);
		float dy = (y - translation.y);
		float lenSqr = dx * dx + dy * dy;
		if(lenSqr  < minDist){
			player = player1;
			minDist = lenSqr;
		}
		
		if(player2.hasJoined() && player2.isAlive()){
			translation = player2.getTransform().getTranslation();
			dx = (x - translation.x);
			dy = (y - translation.y);
			lenSqr = dx * dx + dy * dy;
			if(lenSqr  < minDist){
				player = player2;
				minDist = lenSqr;
			}
		}
		
		if(player3.hasJoined() && player3.isAlive()){
			translation = player3.getTransform().getTranslation();
			dx = (x - translation.x);
			dy = (y - translation.y);
			lenSqr = dx * dx + dy * dy;
			if(lenSqr  < minDist){
				player = player3;
				minDist = lenSqr;
			}
		}
		
		if(player4.hasJoined() && player4.isAlive()){
			translation = player4.getTransform().getTranslation();
			dx = (x - translation.x);
			dy = (y - translation.y);
			lenSqr = dx * dx + dy * dy;
			if(lenSqr  < minDist){
				player = player4;
				minDist = lenSqr;
			}
		}
		
		return player;
		
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
	
	public InputSystem getInputSystem(){
		return input;
	}
	
	public BulletPool getBulletPool(){
		return bulletPool;
	}
	
	public EnemyPool getEnemyPool(){
		return enemyPool;
	}
	
	public MultiplierPool getMultiplierPool(){
		return multiplierPool;
	}
	
	public ParticlePool getParticlePool(){
		return particlePool;
	}
	
	public SpawnEffectPool getSpawnEffectPool(){
		return effectPool;
	}
	
	public ArrayList<Entity> getAllEntities(){
		return entities;
	}
	
//	public int getPhysicsObjectCount(){
//		return physics.numObjects();
//	}
//	
//	public int getCollisionObjectCount(){
//		return collision.numObjects();
//	}
//	
//	public int getEntityCount(){
//		return entities.size();
//	}
}
