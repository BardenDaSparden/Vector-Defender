package com.vecdef.ai;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;

public class ChaserBehaviour extends Behaviour{
	
	float speed = 4.0F;
	float maxSpeed = 12.0F;
	float random = 0.3f + FastMath.random() * 0.7f;
	
	Timer activateTimer = new Timer(20);
	boolean active = false;
	
	public ChaserBehaviour(Scene scene, Enemy enemy){
		super(scene, enemy);
		activateTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				active = true;
			}
		});
		activateTimer.start();
	}
	
	@Override
	public void create(){
		
	}
	
	@Override
	public void update(){
		activateTimer.tick();
		if(!active)
			return;
		
		  if (speed < maxSpeed)
			  speed += 0.005F;
			
		  Player player = scene.getNearestPlayer(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y);
		  if(player.isAlive()){
			  float angleToPlayer = player.getTransform().getTranslation().sub(self.getTransform().getTranslation()).direction();
			  self.getVelocity().set(new Vector2f(FastMath.cosd(angleToPlayer) * speed, FastMath.sind(angleToPlayer) * speed));
		  }
	  }

	@Override
	public void destroy() {
		
	}
}