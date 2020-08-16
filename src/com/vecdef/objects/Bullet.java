package com.vecdef.objects;

import org.javatroid.math.Vector3f;

import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.rendering.HUDRenderer;
import com.vecdef.util.Masks;

public class Bullet extends Entity {
	
	int timeActive = 0;
	int groupMask = Masks.Collision.BULLET_P1;
	ContactEventListener listener;
	
	public Bullet(Scene scene){
		super(scene);
	    super.radius = 12;
	    listener = new ContactEventListener(){
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				if((other.getGroupMask() & Masks.Collision.ENEMY) == Masks.Collision.ENEMY)
					destroy();
			}
		};
	}
	
	@Override
	public void update(){
		timeActive++;
		
		scene.getGrid().applyExplosiveForce(velocity.length() * 3, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 50.0f);
	    
		if (velocity.lengthSquared() > 0.0F) 
	      transform.setOrientation(velocity.direction());
	    
	    if(timeActive > 340)
	    	destroy();
	    
	}
	
	@Override
	public void destroy(){	     
	     if(!super.isRecycled())
	    	 scene.getBulletPool().recycle(this);
	}
	
	@Override
	public void reuse(){
		super.reuse();
		addContactListener(listener);
	}
	
	@Override
	public void recycle(){
		super.recycle();
		timeActive = 0;
		velocity.set(0, 0);
		acceleration.set(0, 0);
		angularVelocity = 0;
		torque = 0;
		removeContactListener(listener);
	}
	
	public void setPlayerID(int playerID){
		groupMask = Masks.Collision.BULLET_P1;
		groupMask <<= playerID;
		
		if(playerID == 0)
			overrideColor.set(HUDRenderer.P1_COLOR);
		if(playerID == 1)
			overrideColor.set(HUDRenderer.P2_COLOR);
		if(playerID == 2)
			overrideColor.set(HUDRenderer.P3_COLOR);
		if(playerID == 3)
			overrideColor.set(HUDRenderer.P4_COLOR);
	}
	
	@Override
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