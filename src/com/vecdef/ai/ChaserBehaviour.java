package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Player;

public class ChaserBehaviour extends Behavior{
	
	float speed = 1.0F;
	float maxSpeed = 15.0F;
	float random = 0.3f + FastMath.random() * 0.7f;
	
	public ChaserBehaviour(Scene scene){
		super(scene);
	}
	
	@Override
	public void create(Entity self){
		
	}
	
	public void update(Entity self){
		  if (speed < maxSpeed)
			  speed += 0.1F;
			
		  Player player = scene.getPlayer();
		  float angleToPlayer = player.getTransform().getTranslation().sub(self.getTransform().getTranslation()).direction();
		  self.getVelocity().set(new Vector2f(FastMath.cosd(angleToPlayer) * speed, FastMath.sind(angleToPlayer) * speed));
		  self.setAngularVelocity(self.getVelocity().length() * random);
	  }

	@Override
	public void destroy(Entity self) {
		
	}
}