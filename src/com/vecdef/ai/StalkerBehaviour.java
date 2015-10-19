package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Player;

public class StalkerBehaviour extends Behavior{
	
	float speed = 2.0F;
	float angleSpeed = 4;
	int time = 0;
	
	public StalkerBehaviour(Scene scene){
		super(scene);
	}
	
	@Override
	public void create(Entity self){
		
	}
	
	public void update(Entity object){
		
		scene.getGrid().applyDirectedForce(new Vector3f(object.getVelocity().x, object.getVelocity().y, 0), new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0), 100);
		
		time += 5;
		Player player = scene.getPlayer();
		float toPlayer = player.getTransform().getTranslation().sub(object.getTransform().getTranslation()).direction();
		
	    object.getVelocity().set(new Vector2f(FastMath.cosd(time) * angleSpeed, FastMath.sind(time) * angleSpeed));
	    object.getVelocity().set(object.getVelocity().add(new Vector2f(FastMath.cosd(toPlayer) * speed, FastMath.sind(toPlayer) * speed)));
	    object.getTransform().setOrientation(object.getVelocity().direction());
	    
	}

	@Override
	public void destroy(Entity self) {
		
	}
}