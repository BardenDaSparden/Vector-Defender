package com.vecdef.objects;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.javatroid.math.Vector4f;

public class Bullet extends Entity{
	
	int timeActive = 0;
	
	public Bullet(Vector2f pos, Vector2f vel){
	    transform.setTranslation(pos);
	    transform.setOrientation(vel.direction());
	    velocity = vel;
	}
	
	public void collision(Entity other){
		if(other instanceof Enemy){
			destroy();
		}
	}

	public void destroy(){
		new DestroyEffect(transform.getTranslation(), 25, 8, new Vector4f(1, 0, 1, 1), 8, 25);
	    isExpired = true;
	}
	
	public void update(Grid grid){
		timeActive++;
		grid.applyExplosiveForce(velocity.length(), new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 80.0F);
	    if (velocity.lengthSquared() > 0.0F) {
	      transform.setOrientation(velocity.direction());
	    }
	    if(timeActive > 400)
	    	isExpired = true;
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