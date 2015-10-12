package com.vecdef.ai;

import org.javatroid.core.Window;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;

public class ZoomerBehaviour implements Behavior{

	float speed = 7;
	float direction = FastMath.random() * 360f;
	boolean isVelocitySet = false;
	
	@Override
	public void onUpdate(Entity object, Grid grid) {
		if(!isVelocitySet){
			isVelocitySet = true;
			object.getVelocity().set(new Vector2f(FastMath.cosd(direction) * speed, FastMath.sind(direction) * speed));
			object.setAngularVelocity(speed);
		}
		
		if ((object.getTransform().getTranslation().x < -Window.getWidth() / 2) || (object.getTransform().getTranslation().x > Window.getWidth() / 2)) {
	    	object.getTransform().getTranslation().x = FastMath.clamp(-Window.getWidth() / 2 + 1, Window.getWidth() / 2 - 1, object.getTransform().getTranslation().x);
	    	object.getVelocity().x *= -1.0F;
	    }

	    if ((object.getTransform().getTranslation().y < -Window.getHeight() / 2) || (object.getTransform().getTranslation().y > Window.getHeight() / 2)) {
	    	object.getTransform().getTranslation().y = FastMath.clamp(-Window.getHeight() / 2 + 1, Window.getHeight() / 2 - 1, object.getTransform().getTranslation().y);
	    	object.getVelocity().y *= -1.0F;
	    }
		
	}

	@Override
	public void onCollision(Entity object, Entity other) {
		if(other instanceof Enemy){
			other.destroy();
			
			speed += 1;
			isVelocitySet = false;
			
		}
	}

	@Override
	public void onDestroy(Entity object) {
		// TODO Auto-generated method stub
		
	}

	
	
}
