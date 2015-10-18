package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;

public class ZoomerBehaviour extends Behavior{

	float speed = 7;
	int gridWidth;
	int gridHeight;
	
	public ZoomerBehaviour(Scene scene){
		super(scene);
		gridWidth = scene.getGrid().getWidth();
		gridHeight = scene.getGrid().getHeight();
	}
	
	@Override
	public void create(Entity self){
		float direction = FastMath.random() * 360f;
		self.getVelocity().set(new Vector2f(FastMath.cosd(direction) * speed, FastMath.sind(direction) * speed));
		self.setAngularVelocity(speed);
	}
	
	@Override
	public void update(Entity self) {
		
		if ((self.getTransform().getTranslation().x < -gridWidth / 2) || (self.getTransform().getTranslation().x > gridWidth / 2)) {
			self.getTransform().getTranslation().x = FastMath.clamp(-gridWidth / 2 + 1, gridWidth / 2 - 1, self.getTransform().getTranslation().x);
			self.getVelocity().x *= -1.0F;
	    }

	    if ((self.getTransform().getTranslation().y < -gridHeight / 2) || (self.getTransform().getTranslation().y > gridHeight / 2)) {
	    	self.getTransform().getTranslation().y = FastMath.clamp(-gridHeight / 2 + 1, gridHeight / 2 - 1, self.getTransform().getTranslation().y);
	    	self.getVelocity().y *= -1.0F;
	    }
		
	}

	@Override
	public void destroy(Entity object) {
		
	}
}
