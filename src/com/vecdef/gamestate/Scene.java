package com.vecdef.gamestate;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.lwjgl.opengl.Display;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.analyze.AudioAnalyzer;
import com.vecdef.objects.CollisionSystem;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.ICollidable;
import com.vecdef.objects.IPhysics;
import com.vecdef.objects.IRenderable;
import com.vecdef.objects.PhysicsSystem;
import com.vecdef.objects.Player;
import com.vecdef.objects.RenderSystem;

public class Scene {
	
	Renderer renderer;
	
	Gamepad gamepad;
	Player player;
	Grid grid;
	ArrayList<Entity> entities;
	ArrayList<Entity> entitiesToRemove;
	
	protected CollisionSystem collision;
	protected PhysicsSystem physics;
	protected RenderSystem renders;
	
	public Scene(Gamepad gamepad, AudioAnalyzer analyzer){
		player = new Player(this, gamepad);
		grid = new Grid(Display.getWidth() + 300, Display.getHeight() + 300, 30, 30, analyzer);
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		collision = new CollisionSystem();
		physics = new PhysicsSystem();
		renders = new RenderSystem();
		
		add(player);
		grid.applyDirectedForce(new Vector3f(0, 0, -400), new Vector3f(0, 0, 0), 500);
	}
	
	public void add(Entity entity){
		collision.add(entity);
		physics.add(entity);
		renders.add(entity);
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
	
	public void addRenderable(IRenderable renderable){
		renders.add(renderable);
	}
	
	public void removeRenderable(IRenderable renderable){
		renders.remove(renderable);
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
		collision.checkCollision();
		physics.integrate();
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			if(entity.isExpired()){
				entity.destroy();
				remove(entity);
			} else {
				entity.update();
			}
		}
		grid.update();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Grid getGrid(){
		return grid;
	}
}
