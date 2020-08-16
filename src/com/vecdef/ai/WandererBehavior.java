package com.vecdef.ai;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Scene;

public class WandererBehavior extends Behaviour{
	
	final float MAX_SPEED = 7.0f;
	float speed = 2f;	
	int gridWidth;
	int gridHeight;
	
	//time in frames
	final int ACTIVATION_TIME = 20;
	Timer activationTimer;
	boolean active;
	
	public WandererBehavior(Scene scene, Enemy enemy){
		super(scene, enemy);
		Grid grid = scene.getGrid();
		gridWidth = grid.getWidth();
		gridHeight = grid.getHeight();
		active = false;
	}
	
	@Override
	public void create(){
		activationTimer = new Timer(ACTIVATION_TIME);
		activationTimer.setCallback(new TimerCallback() {
			
			@Override
			public void execute(Timer timer) {
				int sign = (FastMath.random() > 0.5f) ? -1 : 1;
				float av = FastMath.randomi(3, 4) * sign;
				self.setAngularVelocity(av);
				float a = FastMath.random() * 360;
				self.getVelocity().set(new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed));
			}
		});
		activationTimer.start();
	}
	
	public void update(){
		if(!activationTimer.isComplete()){
			activationTimer.tick();
			return;
		}
		
		if(Math.abs(self.getVelocity().x) > MAX_SPEED){
			self.getVelocity().x = MAX_SPEED * Math.signum(self.getVelocity().x);
		}
		
		if(Math.abs(self.getVelocity().y) > MAX_SPEED){
			self.getVelocity().y = MAX_SPEED * Math.signum(self.getVelocity().y);
		}
		
		//scene.getGrid().applyExplosiveForce(5, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 100);
	    
	    if ((self.getTransform().getTranslation().x < -gridWidth / 2) || (self.getTransform().getTranslation().x > gridWidth / 2)) {
	    	self.getTransform().getTranslation().x = FastMath.clamp(-gridWidth / 2 + 1, gridWidth / 2 - 1, self.getTransform().getTranslation().x);
	    	self.getVelocity().x *= -1.0F;
	    }

	    if ((self.getTransform().getTranslation().y < -gridHeight / 2) || (self.getTransform().getTranslation().y > gridHeight / 2)) {
	    	self.getTransform().getTranslation().y = FastMath.clamp(-gridHeight / 2 + 1, gridHeight / 2 - 1, self.getTransform().getTranslation().y);
	    	self.getVelocity().y *= -1.0F;
	    }
	    
	    scene.getGrid().applyExplosiveForce(5 * self.getVelocity().length(), new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 50);
	    
	}

	@Override
	public void destroy() {
		
	}
}