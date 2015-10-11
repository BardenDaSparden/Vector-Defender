package com.shapedefender.behaviours;

import com.shapedefender.objects.Entity;
import com.shapedefender.objects.Grid;
import com.shapedefender.objects.Player;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

public class ChaserBehaviour implements Behavior{
	
	float speed = 1.0F;
	float maxSpeed = 15.0F;
	float random = 0.3f + FastMath.random() * 0.7f;
	
	public void onUpdate(Entity object, Grid grid, float dt){
		  if (speed < maxSpeed)
			  speed += 0.1F;
			
		  Player player = Entity.getScene().getPlayer();
		  float angleToPlayer = player.getTransform().getTranslation().sub(object.getTransform().getTranslation()).direction();
		  object.setVelocity(new Vector2f(FastMath.cosd(angleToPlayer) * speed, FastMath.sind(angleToPlayer) * speed));
		  object.setAngularVelocity(object.getVelocity().length() * random);
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