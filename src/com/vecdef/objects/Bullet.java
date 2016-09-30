package com.vecdef.objects;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.rendering.HUDRenderer;
import com.vecdef.util.Masks;

public class Bullet extends Entity {
	
	int timeActive = 0;
	int groupMask = Masks.Collision.BULLET_P1;
	
	public Bullet(Vector2f pos, Vector2f vel, int playerID, Scene scene){
		super(scene);
		groupMask <<= playerID;
		if(playerID == 0)
			overrideColor.set(HUDRenderer.P1_COLOR);
		if(playerID == 1)
			overrideColor.set(HUDRenderer.P2_COLOR);
		if(playerID == 2)
			overrideColor.set(HUDRenderer.P3_COLOR);
		if(playerID == 3)
			overrideColor.set(HUDRenderer.P4_COLOR);
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
		scene.getGrid().applyExplosiveForce(velocity.length() * 3, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 50.0f);
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
		return groupMask;
	}

	@Override
	public int getCollisionMask() {
		return Masks.Collision.ENEMY;
	}
	
	@Override
	public boolean useOverrideColor(){
		return true;
	}
	
}