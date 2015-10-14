package com.vecdef.ai;

import org.javatroid.core.Window;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;

public class WandererBehavior extends Behavior{
	
	float speed = 2f;
	boolean vIsSet = false;
	
	public WandererBehavior(Scene scene){
		super(scene);
	}
	
	public void update(Entity object){
		
		if(!vIsSet){
			float a = FastMath.random() * 360;
			object.getVelocity().set(new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed));
			vIsSet = true;
		}
		
		scene.getGrid().applyExplosiveForce(10, new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0), 100);
		
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
	public void destroy(Entity self) {
		// TODO Auto-generated method stub
		
	}
	
}