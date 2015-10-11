package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Player;

public class StalkerBehaviour implements Behavior{
	
	float speed = 3.0F;
	float angleSpeed = 4;
	
	int time = 0;
	
	public void onUpdate(Entity object, Grid grid, float dt){
		
		grid.applyDirectedForce(new Vector3f(object.getVelocity().x, object.getVelocity().y, 0), new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0), 100);
		
		time += 5;
		Player player = Entity.getScene().getPlayer();
		float toPlayer = player.getTransform().getTranslation().sub(object.getTransform().getTranslation()).direction();
		
	    object.setVelocity(new Vector2f(FastMath.cosd(time) * angleSpeed, FastMath.sind(time) * angleSpeed));
	    object.setVelocity(object.getVelocity().add(new Vector2f(FastMath.cosd(toPlayer) * speed, FastMath.sind(toPlayer) * speed)));
	    object.getTransform().setOrientation(object.getVelocity().direction());
	    
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