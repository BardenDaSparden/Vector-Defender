package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Player;

public class PrototypeBehaviour implements Behavior{
	
	float currentOrientation = 0.0F;
	float targetOrientation = 0.0F;
	float speed = 7.6F;
	
	public void onUpdate(Entity object, Grid grid){
		
		
		
		grid.applyExplosiveForce(50, new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0), 100);
		
		Player player = Entity.getScene().getPlayer();
		Vector2f toPlayer = player.getTransform().getTranslation().sub(object.getTransform().getTranslation());

	    object.getTransform().setOrientation(object.getVelocity().direction());
	    object.getVelocity().set(new Vector2f(FastMath.cosd(toPlayer.direction()) * speed, FastMath.sind(toPlayer.direction()) * speed));

	    speed += 0.005F;
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