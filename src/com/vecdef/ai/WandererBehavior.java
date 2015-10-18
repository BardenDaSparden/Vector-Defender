package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;

public class WandererBehavior extends Behavior{
	
	float speed = 3f;	
	int gridWidth;
	int gridHeight;
	
	public WandererBehavior(Scene scene){
		super(scene);
		Grid grid = scene.getGrid();
		gridWidth = grid.getWidth();
		gridHeight = grid.getHeight();
	}
	
	@Override
	public void create(Entity self){
		float a = FastMath.random() * 360;
		self.getVelocity().set(new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed));
	}
	
	public void update(Entity self){
		
		scene.getGrid().applyExplosiveForce(10, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 100);
		
		self.setAngularVelocity(-3);
	    
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
	public void destroy(Entity self) {
		
	}
}