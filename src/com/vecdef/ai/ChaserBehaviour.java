package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Player;

public class ChaserBehaviour implements Behavior{
	
	float speed = 1.0F;
	float maxSpeed = 15.0F;
	float random = 0.3f + FastMath.random() * 0.7f;
	
	public void onUpdate(Entity object, Grid grid){
		  if (speed < maxSpeed)
			  speed += 0.1F;
			
		  Player player = Entity.getScene().getPlayer();
		  float angleToPlayer = player.getTransform().getTranslation().sub(object.getTransform().getTranslation()).direction();
		  object.getVelocity().set(new Vector2f(FastMath.cosd(angleToPlayer) * speed, FastMath.sind(angleToPlayer) * speed));
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