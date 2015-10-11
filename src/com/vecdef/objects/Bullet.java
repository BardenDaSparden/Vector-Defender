package com.vecdef.objects;

import org.javatroid.core.Window;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

public class Bullet extends Entity{
	
	private static final float EXPIRY_PADDING = 450;
	
	public Bullet(Vector2f position, Vector2f velocity){
	    transform.setTranslation(position);
	    this.velocity = velocity;
	    transform.setOrientation(velocity.direction());
	    this.radius = 8.0F;
	}
	
	public void onCollision(Entity other){
		if(other instanceof Enemy){
			onDestroy();
		}
	}

	public void onDestroy(){
		new DestroyEffect(transform.getTranslation(), 25, 8, getBaseColor(), 8, 25);
	    bExpired = true;
	}
	
	public void onUpdate(Grid grid, float dt){
		
		grid.applyExplosiveForce(velocity.length(), new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 80.0F);

	    if (velocity.lengthSquared() > 0.0F) {
	      transform.setOrientation(velocity.direction());
	    }
	    
	    if ((transform.getTranslation().x < -Window.getWidth() / 2 - EXPIRY_PADDING) || (transform.getTranslation().x > Window.getWidth() / 2 + EXPIRY_PADDING) || (transform.getTranslation().y < -Window.getHeight() / 2 - EXPIRY_PADDING) || (transform.getTranslation().y > Window.getHeight() / 2 + EXPIRY_PADDING))
	      bExpired = true;
	}
	
}