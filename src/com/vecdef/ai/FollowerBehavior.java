package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;

public class FollowerBehavior extends Behaviour{
	
	final float MAX_SPEED = 2.3f;
	int time = 0;
	
	EnemySpawnEffect spawnEffect;
	
	public FollowerBehavior(Scene scene, Enemy enemy){
		super(scene, enemy);
		spawnEffect = new EnemySpawnEffect(scene, enemy);
		scene.add(spawnEffect);
	}
  
	@Override
	public void create(){
		
	}
	
	public void update(){
		time += 6;
	  
		self.getTransform().scale(FastMath.cosd(time + 90) * 0.02f, FastMath.sind(time) * 0.02f);
	  
		scene.getGrid().applyExplosiveForce(5 * self.getVelocity().length(), new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 50);
	  
		Vector2f position = self.getTransform().getTranslation();
		Player player = scene.getNearestPlayer(position.x, position.y);
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