package com.shapedefender.behaviours;

import com.shapedefender.objects.Entity;
import com.shapedefender.objects.Grid;
import com.shapedefender.objects.Player;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

public class FollowerBehavior implements Behavior{
	
	private static final float MAX_SPEED = 2.5f;
	
	  float speed = 3.5f;
	  int time = 0;
	  
	  public void onUpdate(Entity object, Grid grid, float dt){
		  time += 4;
		  
		  float scaleX = 1 + FastMath.cosd(time + 90) * 0.3f;
		  float scaleY = 1 + FastMath.sind(time) * 0.3f;
		  object.getTransform().getScale().set(scaleX, scaleY);
		  
		  grid.applyExplosiveForce(5 * object.getVelocity().length(), new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0), 50);
		  
		  Player player = Entity.getScene().getPlayer();
		  Vector2f dPos = player.getTransform().getTranslation().sub(object.getTransform().getTranslation()).normalize();
		  Vector2f acceleration = object.getAcceleration();
		  acceleration.x += dPos.x;
		  acceleration.y += dPos.y;
		  
		  Vector2f velocity = object.getVelocity();
		  velocity.x = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.x);
		  velocity.y = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.y);
		  
	  }

	@Override
	public void onCollision(Entity object, Entity other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy(Entity object) {
		// TODO Auto-generated method stub
		
	}
}