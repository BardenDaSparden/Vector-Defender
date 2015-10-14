package com.vecdef.objects;

import java.util.ArrayList;
import org.javatroid.math.Vector2f;

import com.vecdef.gamestate.ShapeRenderer;

public class EntityManager{
	
	static ArrayList<Entity> entities = new ArrayList<Entity>();

	static RenderSystem renderer = new RenderSystem();
	static PhysicsSystem physicsWorld = new PhysicsSystem();
	
	public static void add(Entity entity){
		addEntity(entity);
	}
	
	private static void addEntity(Entity entity){
		entities.add(entity);
		physicsWorld.add(entity);
	}

	private static void removeEntity(Entity entity){
		entities.remove(entity);
		physicsWorld.remove(entity);
	}
	
	public static void update(Grid grid){
		for (int i = 0; i < entities.size(); i++){
			Entity entity = (Entity)entities.get(i);
		      if (entity.isExpired)
		        removeEntity(entity);
		      else {
		        entity.update(grid);
		      }
		}
		
		physicsWorld.integrate();
		
		handleCollisions();
	}
	
	@SuppressWarnings("unchecked")
	private static void handleCollisions(){
		
		ArrayList<Bullet> bullets = 					(ArrayList<Bullet>) getEntities(Bullet.class);
		ArrayList<Enemy>  enemies = 					(ArrayList<Enemy>) getEntities(Enemy.class);
		ArrayList<MultiplierPiece> pieces = 			(ArrayList<MultiplierPiece>) getEntities(MultiplierPiece.class);
		
		Enemy enemy;
		Enemy enemy2;
		
		Bullet bullet;
		Player player = Entity.getScene().getPlayer();
		
		for(int i = 0; i < pieces.size(); i++){
			MultiplierPiece piece = pieces.get(i);
			if(isColliding(piece, player)){
				piece.collision(player);
				player.collision(piece);
			}
		}
		
		for(int i = 0; i < pieces.size(); i++){
			MultiplierPiece piece = pieces.get(i);
			for(int j = 0; j < enemies.size(); j++){
				enemy = enemies.get(j);
				if(isColliding(piece, enemy)){
					piece.collision(enemy);
					enemy.collision(piece);
				}
			}
		}
		
		for(int i = 0; i < enemies.size(); i++){
			for(int j = 0; j < bullets.size(); j++){
				
				enemy = enemies.get(i);
				bullet = bullets.get(j);
				
				if(isColliding(enemy, bullet)){
					enemy.collision(bullet);
					bullet.collision(enemy);
				}
				
			}
		}
		
		
		for(int i = 0; i < enemies.size(); i++){
			enemy = enemies.get(i);
			
			if(enemy.isExpired()){
				if(isColliding(enemy, player)){
					enemy.collision(player);
					player.collision(enemy);
				}
			}
			
		}
		
		for(int i = 0; i < enemies.size(); i++){
			enemy = enemies.get(i);
			
			if(enemy.isExpired()){
				for(int j = 0; j < enemies.size(); j++){
					enemy2 = enemies.get(j);
					if(enemy2.isExpired()){
						if(enemy != enemy2){
							if(isColliding(enemy, enemy2)){
								enemy.collision(enemy2);
								enemy2.collision(enemy);
							}
						}
					}
				}
			}
			
		}
		
	}
	
	private static boolean isColliding(Entity a, Entity b){
		float radius = a.getRadius() + b.getRadius();
	    float ds = (a.getTransform().getTranslation().x - b.getTransform().getTranslation().x) * (a.getTransform().getTranslation().x - b.getTransform().getTranslation().x) + (a.getTransform().getTranslation().y - b.getTransform().getTranslation().y) * (a.getTransform().getTranslation().y - b.getTransform().getTranslation().y);
	    return (!a.isExpired) && (!b.isExpired) && (ds < radius * radius);
	}
	
	public static ArrayList<Entity> getNearbyEntities(Vector2f position, float radius){
		ArrayList<Entity> objects = new ArrayList<Entity>();
		
	    for (int i = 0; i < entities.size(); i++){
	    	Entity e = (Entity)entities.get(i);

		    float dx = e.getTransform().getTranslation().x - position.x;
		    float dy = e.getTransform().getTranslation().y - position.y;
		    
		    if (dx * dx + dy * dy < radius * radius){
		    	objects.add(e);
		    }
		    
	    }

	    return objects;
	}
	
	public static void draw(ShapeRenderer shapeRenderer){
		for(int i = 0; i < entities.size(); i++){
			renderer.add(entities.get(i));
		}
		
		renderer.draw(shapeRenderer);
		
	}
	
	public static void destroyAll(){
		
		for(Entity e : entities){
			e.destroy();
		}
		
		entities.clear();
	}
	
	public static ArrayList<? extends Entity> getEntities(Class<? extends Entity> entityType){
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for(int i = 0; i < EntityManager.entities.size(); i++){
			Entity e = EntityManager.entities.get(i);
			if(e.getClass() == entityType){
				entities.add(e);
			}
		}
		return entities;
	}
	
	public static int getEntityCount(){
		return entities.size();
	}
	
}