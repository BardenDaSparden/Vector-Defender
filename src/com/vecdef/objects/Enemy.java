package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import com.vecdef.ai.Behavior;
import com.vecdef.gamestate.Scene;

public class Enemy extends Entity{
	
	Vector4f baseColor;
	ArrayList<Behavior> behaviors;
	boolean bCreate;
	int health;
	int killValue;
	
	int groupMask;
	int collisionMask;
	int radius;
	
	public Enemy(Scene scene){
		super(scene);
		
		baseColor = new Vector4f(1, 1, 1, 1);
		behaviors = new ArrayList<Behavior>();
		bCreate = false;
		health = 1;
		killValue = 1;
	    radius = 16;
	    groupMask = Masks.Collision.ENEMY;
	    collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
	    
	    addContactListener(new ContactEventListener() {
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				int otherGroup = other.getGroupMask();
				
				if((otherGroup & Masks.Collision.BULLET) == Masks.Collision.BULLET){
					health -= 1;
					 if(health <= 0){
						expire();
						Player player = scene.getPlayer();
						player.registerBulletKill(Enemy.this);
					 }
				} else if((otherGroup & Masks.Collision.BLACK_HOLE) == Masks.Collision.BLACK_HOLE){
					Enemy.this.expire();
				}
			}
		});
	}

	public void update(){
		int n = behaviors.size();
		if(!bCreate){
			for(int i = 0; i < n; i++){
				Behavior behavior = behaviors.get(i);
				behavior.create();
			}
			bCreate = true;
		}
		
		for(int i = 0; i < n; i++){
			Behavior behavior = behaviors.get(i);
			behavior.update();
		}
	}
	
	
	
	public void destroy(){
	     int numPieces = FastMath.randomi(1, 4);
	     final int numParticles = 30;
	     float speed = 2;
	     
	     for(int i = 0; i < numPieces; i++){
	    	 MultiplierPiece piece = new MultiplierPiece(transform.getTranslation(), scene);
	    	 float a = FastMath.random() * 360; 
	    	 piece.velocity = new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed);
	    	 scene.add(piece);
	     }
	     
	     
	     Vector2f position = getTransform().getTranslation();
	     Vector2f offset = new Vector2f();
	     float radius = 32;
	     float pSpeed = 6.5f;
	     
//	     for(int i = 0; i < numParticles; i++){
//	    	 
//	    	 float angle = (float) (((float)i / (float)numParticles) * Math.PI * 2);
//	    	 offset.x = (float)Math.cos(angle) * radius;
//	    	 offset.y = (float)Math.sin(angle) * radius;
//	    	 
//	    	 Particle particle = new Particle(position.x, position.y, baseColor, scene);
//	    	 
//	    	 particle.setMaxLife(30);
//	    	 
//	    	 particle.getTransform().setOrientation((float)Math.toDegrees(angle));
//	    	 particle.getTransform().getTranslation().addi(offset);
//	    	 
//	    	 particle.getVelocity().set((float)Math.cos(angle) * pSpeed, (float)Math.sin(angle) * pSpeed);
//	    	 particle.addBehavior(new Behavior(scene, this) {
//				
//	    		 @Override
//    			public void create(){
//    				
//    			}
//	    		 
//				@Override
//				public void update() {
//					Vector2f velocity = self.getVelocity();
//					self.getTransform().setOrientation((float)Math.toDegrees(Math.atan2(velocity.y, velocity.x)));
//				}
//				
//				@Override
//				public void destroy() {
//					
//				}
//			});
//	    	 
//	    	 scene.add(particle);
//	     }
	     
	     int n = behaviors.size();
	     for(int i = 0; i < n; i++){
			Behavior behavior = behaviors.get(i);
			behavior.destroy();
	     }
	}
	
	public void addBehavior(Behavior behavior){
		behaviors.add(behavior);
	}
	
	public int getKillValue(){
		return killValue;
	}

	public int getEntityType(){
		return Masks.Entities.ENEMY;
	}
	
	@Override
	public int getGroupMask() {
		return groupMask;
	}

	@Override
	public int getCollisionMask() {
		return collisionMask;
	}
	
	@Override
	public int getRadius() {
		return radius;
	}
}