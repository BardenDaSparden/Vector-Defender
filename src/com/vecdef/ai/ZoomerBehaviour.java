package com.vecdef.ai;

import org.javatroid.core.Window;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;

public class ZoomerBehaviour extends Behavior{

	float speed = 7;
	float direction = FastMath.random() * 360f;
	boolean isVelocitySet = false;
	
	public ZoomerBehaviour(Scene scene){
		super(scene);
	}
	
	@Override
	public void create(Entity self){
		
	}
	
	@Override
	public void update(Entity self) {
		if(!isVelocitySet){
			isVelocitySet = true;
			self.getVelocity().set(new Vector2f(FastMath.cosd(direction) * speed, FastMath.sind(direction) * speed));
			self.setAngularVelocity(speed);
		}
		
		if ((self.getTransform().getTranslation().x < -Window.getWidth() / 2) || (self.getTransform().getTranslation().x > Window.getWidth() / 2)) {
			self.getTransform().getTranslation().x = FastMath.clamp(-Window.getWidth() / 2 + 1, Window.getWidth() / 2 - 1, self.getTransform().getTranslation().x);
			self.getVelocity().x *= -1.0F;
	    }

	    if ((self.getTransform().getTranslation().y < -Window.getHeight() / 2) || (self.getTransform().getTranslation().y > Window.getHeight() / 2)) {
	    	self.getTransform().getTranslation().y = FastMath.clamp(-Window.getHeight() / 2 + 1, Window.getHeight() / 2 - 1, self.getTransform().getTranslation().y);
	    	self.getVelocity().y *= -1.0F;
	    }
		
	}

	@Override
	public void destroy(Entity object) {
		// TODO Auto-generated method stub
		
	}
}
