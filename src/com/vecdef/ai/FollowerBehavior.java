package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Player;

public class FollowerBehavior extends Behavior{
	
	private static final float MAX_SPEED = 2.5f;
	
	float speed = 3.5f;
	int time = 0;
  
	public FollowerBehavior(Scene scene){
		super(scene);
	}
  
	public void update(Entity object){
		time += 4;
	  
		float scaleX = 1 + FastMath.cosd(time + 90) * 0.3f;
		float scaleY = 1 + FastMath.sind(time) * 0.3f;
		object.getTransform().getScale().set(scaleX, scaleY);
	  
		scene.getGrid().applyExplosiveForce(5 * object.getVelocity().length(), new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0), 50);
	  
		Player player = scene.getPlayer();
		Vector2f dPos = player.getTransform().getTranslation().sub(object.getTransform().getTranslation()).normalize();
		Vector2f acceleration = object.getAcceleration();
		acceleration.x += dPos.x;
		acceleration.y += dPos.y;
	  
		Vector2f velocity = object.getVelocity();
		velocity.x = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.x);
		velocity.y = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.y);
	  
	}

	@Override
	public void destroy(Entity object) {
		// TODO Auto-generated method stub
	}
}