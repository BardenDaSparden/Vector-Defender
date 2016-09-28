package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import com.vecdef.ai.Behaviour;
import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.util.Masks;

public class Enemy extends Entity {
	
	Vector4f baseColor;
	ArrayList<Behaviour> behaviors;
	boolean bCreate;
	int health;
	int killValue;
	int energyValue;
	
	int groupMask;
	int collisionMask;
	int radius;
	
	public Enemy(Scene scene){
		super(scene);
		
		baseColor = new Vector4f(1, 1, 1, 1);
		behaviors = new ArrayList<Behaviour>();
		bCreate = false;
		health = 1;
		killValue = 1;
		energyValue = 5;
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
				} else if((otherGroup & Masks.Collision.PLAYER) == Masks.Collision.PLAYER){
					Enemy.this.expire();
				}  else if((otherGroup & Masks.Collision.ABILITY) == Masks.Collision.ABILITY){
					Enemy.this.expire();
				}
			}
		});
	}

	public void update(){
		int n = behaviors.size();
		if(!bCreate){
			for(int i = 0; i < n; i++){
				Behaviour behavior = behaviors.get(i);
				behavior.create();
			}
			bCreate = true;
		}
		
		for(int i = 0; i < n; i++){
			Behaviour behavior = behaviors.get(i);
			behavior.update();
		}
	}
	
	
	
	public void destroy(){
	     int numPieces = FastMath.randomi(2, 4);
	     final int numParticles = 50;
	     float speed = 3;
	     
	     for(int i = 0; i < numPieces; i++){
	    	 MultiplierPiece piece = new MultiplierPiece(transform.getTranslation(), scene);
	    	 float a = FastMath.random() * 360; 
	    	 piece.velocity = new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed);
	    	 scene.add(piece);
	     }
	     
	     
	     Vector2f position = getTransform().getTranslation();
	     Vector2f offset = new Vector2f();
	    // float radius = 0;
	     float pSpeed = 8.25f;
	     
	     for(int i = 0; i < numParticles; i++){
	    	 
	    	 float angle = (float) (((float)i / (float)numParticles) * Math.PI * 2);
//	    	 offset.x = (float)Math.cos(angle) * radius;
//	    	 offset.y = (float)Math.sin(angle) * radius;
	    	 
	    	 Particle particle = new Particle(position.x, position.y, baseColor, scene);
	    	 
	    	 particle.setMaxLife(45);
	    	 
	    	 particle.getTransform().setOrientation((float)Math.toDegrees(angle));
	    	 particle.getTransform().getTranslation().addi(offset);
	    	 particle.getTransform().getScale().set(0.4f + (float)Math.random() * 0.1f, 0.2f + (float)Math.random() * 0.3f);
	    	 
	    	 Vector2f pVel = particle.getVelocity();
	    	 
	    	 pVel.set((float)Math.cos(angle) * (float)(pSpeed + Math.random() * 3.0f), (float)Math.sin(angle) * (float)(pSpeed + Math.random() * 3.0f));
	    	 
	    	 
	    	 scene.add(particle);
	     }
	     
	     //scene.getGrid().applyImplosiveForce(1200, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 30), 200);
	     
	     int n = behaviors.size();
	     for(int i = 0; i < n; i++){
			Behaviour behavior = behaviors.get(i);
			behavior.destroy();
	     }
	}
	
	public void addBehavior(Behaviour behavior){
		behaviors.add(behavior);
	}
	
	public int getKillValue(){
		return killValue;
	}

	public int getEnergyValue(){
		return energyValue;
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
		return collisionMask | Masks.Collision.ABILITY;
	}
	
	@Override
	public int getRadius() {
		return radius;
	}
}