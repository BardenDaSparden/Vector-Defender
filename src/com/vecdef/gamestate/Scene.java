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

	Renderer renderer;
	
	Player player;
	Grid grid;
	ArrayList<Entity> entities;
	CollisionSystem collision;
	PhysicsSystem physics;
	RenderSystem renders;
	
	public Scene(Renderer renderer){
		this.renderer = renderer;
		
		player = new Player();
		grid = new Grid(1920, 1080, 40, 40);
		entities = new ArrayList<Entity>();
		collision = new CollisionSystem();
		physics = new PhysicsSystem();
		renders = new RenderSystem();
	}
	
	public void add(Entity entity){
		collision.add(entity);
		physics.add(entity);
		renders.add(entity);
	}
	
	public void remove(Entity entity){
		collision.remove(entity);
		physics.remove(entity);
		renders.remove(entity);
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
		int n = entities.size();
		for(int i = 0; i < n; i++){
			Entity entity = entities.get(i);
			if(entity.isExpired()){
				entity.destroy();
				remove(entity);
			} else {
				entity.update(grid);
			}
		}
		
		physics.integrate();
		collision.checkCollision();
	}
	
	public void draw(){
		ShapeRenderer sRenderer = renderer.ShapeRenderer();
		renders.draw(sRenderer);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Grid getGrid(){
		return grid;
	}
}
