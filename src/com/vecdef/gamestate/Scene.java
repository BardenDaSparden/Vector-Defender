package com.vecdef.gamestate;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.vecdef.objects.CollisionSystem;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.PhysicsSystem;
import com.vecdef.objects.Player;
import com.vecdef.objects.RenderSystem;

public class Scene {

	final float TIME_STEP = 1.0f / 60.0f;
	
	Renderer renderer;
	
	Player player;
	Grid grid;
	ArrayList<Entity> entities;
	ArrayList<Entity> entitiesToRemove;
	CollisionSystem collision;
	PhysicsSystem physics;
	RenderSystem renders;
	
	public Scene(Renderer renderer){
		this.renderer = renderer;
		player = new Player(this);
		grid = new Grid(1920, 1080, 40, 40);
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		collision = new CollisionSystem();
		physics = new PhysicsSystem();
		renders = new RenderSystem();
		
		add(player);
	}
	
	public void add(Entity entity){
		collision.add(entity);
		physics.add(entity);
		renders.add(entity);
		entities.add(entity);
	}
	
	public void remove(Entity entity){
		collision.remove(entity);
		physics.remove(entity);
		renders.remove(entity);
		entities.remove(entity);
	}
	
	public void getEntitiesByType(int mask, ArrayList<Entity> list){
		int n = entities.size();
		for(int i = 0; i < n; i++){
			Entity entity = entities.get(i);
			if((entity.getEntityType() & mask) == 0x01)
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
		
		collision.checkCollision();
		
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			entity.update();
		}
			
		physics.integrate(TIME_STEP);
		
		grid.update();
		
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			if(entity.isExpired()){
				entity.destroy();
				remove(entity);
			}
		}
		
//		System.out.println("Collision: " + collision.numObjects());
//		System.out.println("Physics: " + physics.numObjects());
//		System.out.println("Renderer: " + renders.numObjects());
//		System.out.println("==================================");
//		System.out.println("Total Entities: " + entities.size());
	}
	
	public void draw(){
		ShapeRenderer sRenderer = renderer.ShapeRenderer();
		grid.draw(sRenderer);
		renders.draw(sRenderer);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Grid getGrid(){
		return grid;
	}
}
