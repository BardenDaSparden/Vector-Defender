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
	
	int entityMask;
	int groupMask;
	int collisionMask;
	int radius;
	
	ContactEventListener listener;
	
	public Enemy(Scene scene){
		super(scene);
		baseColor = new Vector4f(1, 1, 1, 1);
		behaviors = new ArrayList<Behaviour>();
		bCreate = false;
		health = 1;
		killValue = 1;
		energyValue = 5;
	    radius = 16;
	    entityMask = Masks.Entities.ENEMY;
	    groupMask = Masks.Collision.ENEMY;
	    collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
	    
	    listener = new ContactEventListener(){
	    	@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				int otherGroup = other.getGroupMask();
				
				if((otherGroup & Masks.Collision.BULLET) != 0){
					health -= 1;
					if(health <= 0)
						destroy();
				}
				
				if((otherGroup & Masks.Collision.BULLET_P1) == Masks.Collision.BULLET_P1){
					if(health <= 0){
						Player player = scene.getPlayer();
						player.registerBulletKill(Enemy.this);
					}
				}
				
				if((otherGroup & Masks.Collision.BULLET_P2) == Masks.Collision.BULLET_P2){
					if(health <= 0){
						Player player = scene.getPlayer2();
						player.registerBulletKill(Enemy.this);
					}
				}
				
				if((otherGroup & Masks.Collision.BULLET_P3) == Masks.Collision.BULLET_P3){
					if(health <= 0){
						Player player = scene.getPlayer3();
						player.registerBulletKill(Enemy.this);
					}
				}
				
				if((otherGroup & Masks.Collision.BULLET_P4) == Masks.Collision.BULLET_P4){
					if(health <= 0){
						Player player = scene.getPlayer4();
						player.registerBulletKill(Enemy.this);
					}
				}
				
				if((otherGroup & Masks.Collision.PLAYER) == Masks.Collision.PLAYER)
					destroy();
				
				if((otherGroup & Masks.Collision.ABILITY) == Masks.Collision.ABILITY)
					destroy();
			}
	    };
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
	
	@Override
	public void reuse(){
		super.reuse();
		bCreate = false;
		isVisible = true;
		addContactListener(listener);
	}
	
	@Override
	public void recycle(){
		super.recycle();
		entityMask = Masks.Entities.ENEMY;
		behaviors.clear();
		bCreate = true;
		isVisible = false;
		behaviors.clear();
		transform.getTranslation().set(-9999, -9999);
		transform.getScale().set(1, 1);
		transform.setOrientation(0);
		velocity.set(0, 0);
		angularVelocity = 0;
		acceleration.set(0, 0);
		torque = 0;
		removeContactListener(listener);
	}
	
	public void destroy(){
	     int numPieces = FastMath.randomi(2, 4);
	     float speed = 3;
	     float enemyX = transform.getTranslation().x;
	     float enemyY = transform.getTranslation().y;
	     MultiplierPool mPool = scene.getMultiplierPool();
	     
	     for(int i = 0; i < numPieces; i++){
	    	 MultiplierPiece piece = (MultiplierPiece) mPool.getNext();
	    	 piece.getTransform().getTranslation().set(enemyX, enemyY);
	    	 float a = FastMath.random() * 360; 
	    	 piece.velocity.set(FastMath.cosd(a) * speed, FastMath.sind(a) * speed);
	     }
	     
	     
	     Vector2f position = getTransform().getTranslation();
	     Vector2f offset = new Vector2f();
	     final int numParticles = 50;
	     float pSpeed = 8.25f;
	     ParticlePool pool = scene.getParticlePool();
	     
	     for(int i = 0; i < numParticles; i++){
	    	 float angle = (float) (((float)i / (float)numParticles) * Math.PI * 2);
	    	 Particle particle = (Particle)pool.getNext();
	    	 particle.getTransform().getTranslation().set(position.x, position.y + 1);
	    	 particle.getOverrideColor().set(baseColor);
	    	 particle.setMaxLife(45);
	    	 particle.getTransform().setOrientation((float)Math.toDegrees(angle));
	    	 particle.getTransform().getTranslation().addi(offset);
	    	 particle.getTransform().getScale().set(0.4f + (float)Math.random() * 0.1f, 0.2f + (float)Math.random() * 0.3f);
	    	 Vector2f pVel = particle.getVelocity();
	    	 pVel.set((float)Math.cos(angle) * (float)(pSpeed + Math.random() * 3.0f), (float)Math.sin(angle) * (float)(pSpeed + Math.random() * 3.0f));
	     }
	     
	     int n = behaviors.size();
	     for(int i = 0; i < n; i++){
			Behaviour behavior = behaviors.get(i);
			behavior.destroy();
	     }
	     
	     if(!super.isRecycled())
	    	 scene.getEnemyPool().recycle(this);
	     
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
		return entityMask;
	}
	
	public void setEntityType(int mask){
		entityMask = mask;
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
	
	@Override
	public boolean useOverrideColor(){
		return true;
	}
	
	@Override
	public Vector4f getOverrideColor(){
		return baseColor;
	}
}