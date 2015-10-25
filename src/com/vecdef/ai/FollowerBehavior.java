package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Player;

public class FollowerBehavior extends Behavior{
	
	final float MAX_SPEED = 1.8f;
	int time = 0;
	
	EnemySpawnEffect spawnEffect;
	
	public FollowerBehavior(Scene scene, Enemy enemy){
		super(scene, enemy);
		spawnEffect = new EnemySpawnEffect(scene, enemy, 250, 250);
		scene.add(spawnEffect);
	}
  
	@Override
	public void create(){
		
	}
	
	public void update(){
		time += 6;
	  
		float scaleX = 1 + FastMath.cosd(time + 90) * 0.2f;
		float scaleY = 1 + FastMath.sind(time) * 0.2f;
		self.getTransform().getScale().set(scaleX, scaleY);
	  
		scene.getGrid().applyExplosiveForce(5 * self.getVelocity().length(), new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 50);
	  
		Player player = scene.getPlayer();
		Vector2f dPos = player.getTransform().getTranslation().sub(self.getTransform().getTranslation()).normalize();
		Vector2f acceleration = self.getAcceleration();
		acceleration.x += dPos.x;
		acceleration.y += dPos.y;
	  
		Vector2f velocity = self.getVelocity();
		velocity.x = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.x);
		velocity.y = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.y);
	  
	}

	@Override
	public void destroy() {
		spawnEffect.destroy();
	}
}