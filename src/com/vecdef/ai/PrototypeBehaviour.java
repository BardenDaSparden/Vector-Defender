package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;

public class PrototypeBehaviour extends Behaviour{
	
	final float MAX_SPEED = 7.0f;
	float speed = 4.0f;
	
	EnemySpawnEffect spawnEffect;
	
	public PrototypeBehaviour(Scene scene, Enemy enemy){
		super(scene, enemy);
		spawnEffect = new EnemySpawnEffect(scene, enemy);
		scene.add(spawnEffect);
	}
	
	@Override
	public void create(){
		
	}
	
	public void update(){	
		scene.getGrid().applyExplosiveForce(25, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 75);
		
		Player player = scene.getPlayer();
		Vector2f toPlayer = player.getTransform().getTranslation().sub(self.getTransform().getTranslation());

		self.getTransform().setOrientation(self.getVelocity().direction());
		self.getVelocity().set(new Vector2f(FastMath.cosd(toPlayer.direction()) * speed, FastMath.sind(toPlayer.direction()) * speed));

	    if(speed + 0.0025f < MAX_SPEED)
	    	speed += 0.0025F;
	}
	
	@Override
	public void destroy() {
		spawnEffect.destroy();
	}
}