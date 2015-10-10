package com.shapedefender.behaviours;

import com.shapedefender.objects.Entity;
import com.shapedefender.objects.Grid;

import org.javatroid.core.Window;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

public class WandererBehavior implements Behavior{
	
	float speed = 2f;
	boolean vIsSet = false;
	
	public void onUpdate(Entity object, Grid grid, float dt){
		
		if(!vIsSet){
			float a = FastMath.random() * 360;
			object.setVelocity(new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed));
			vIsSet = true;
		}
		
		grid.applyExplosiveForce(10, new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0), 100);
		
	    object.setAngularVelocity(-3);
	    
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy(Entity object) {
		// TODO Auto-generated method stub
		
	}
	
}