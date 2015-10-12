package com.vecdef.objects;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

public class Bullet extends Entity{
	
	int timeActive = 0;
	
	public Bullet(Vector2f position, Vector2f velocity){
	    transform.setTranslation(position);
	    transform.setOrientation(velocity.direction());
	    this.velocity = velocity;
	    this.radius = 8.0F;
	}
	
	public void collision(Entity other){
		if(other instanceof Enemy){
			destroy();
		}
	}

	public void destroy(){
		new DestroyEffect(transform.getTranslation(), 25, 8, getBaseColor(), 8, 25);
	    bExpired = true;
	}
	
	public void update(Grid grid, float dt){
		timeActive++;
		grid.applyExplosiveForce(velocity.length(), new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 80.0F);
	    if (velocity.lengthSquared() > 0.0F) {
	      transform.setOrientation(velocity.direction());
	    }
	    if(timeActive > 400)
	    	bExpired = true;
	}
	
}