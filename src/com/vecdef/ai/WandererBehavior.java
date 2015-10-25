package com.vecdef.ai;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Grid;

public class WandererBehavior extends Behavior{
	
	float speed = 2f;	
	int gridWidth;
	int gridHeight;
	
	//time in frames
	final int ACTIVATION_TIME = 20;
	Timer activationTimer;
	boolean active;
	
	EnemySpawnEffect spawnEffect;
	
	public WandererBehavior(Scene scene, Enemy enemy){
		super(scene, enemy);
		Grid grid = scene.getGrid();
		gridWidth = grid.getWidth();
		gridHeight = grid.getHeight();
		active = false;
		spawnEffect = new EnemySpawnEffect(scene, enemy, 250, 100);
		scene.add(spawnEffect);
	}
	
	@Override
	public void create(){
		activationTimer = new Timer(ACTIVATION_TIME);
		activationTimer.setCallback(new TimerCallback() {
			
			@Override
			public void execute(Timer timer) {
				self.setAngularVelocity(-3);
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
		
		scene.getGrid().applyExplosiveForce(10, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 100);
	    
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
	public void destroy() {
		spawnEffect.expire();
	}
}