package com.vecdef.objects;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;

public class Bullet extends Entity{
	
	int timeActive = 0;
	
	public Bullet(Vector2f pos, Vector2f vel, Scene scene){
		super(scene);
	    transform.setTranslation(pos);
	    transform.setOrientation(vel.direction());
	    velocity = vel;
	    
	    addContactListener(new ContactEventListener() {
			@Override
			public void process(ContactEvent event) {
				Bullet.this.expire();
			}
		});
	}

	public void destroy(){
		
	}
	
	public void update(){
		timeActive++;
		scene.getGrid().applyExplosiveForce(velocity.length(), new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 80.0F);
	    if (velocity.lengthSquared() > 0.0F) {
	      transform.setOrientation(velocity.direction());
	    }
	    if(timeActive > 340)
	    	expire();
	}
	
	public int getEntityType(){
		return Masks.Entities.BULLET;
	}

	@Override
	public int getRadius() {
		return 6;
	}

	@Override
	public int getGroupMask() {
		return Masks.Collision.BULLET;
	}

	@Override
	public int getCollisionMask() {
		return Masks.Collision.ENEMY;
	}
	
}