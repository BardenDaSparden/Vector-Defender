package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Player;

public class StalkerBehaviour extends Behavior{
	
	float speed = 2.0F;
	float angleSpeed = 4;
	int time = 0;
	
	EnemySpawnEffect spawnEffect;
	
	public StalkerBehaviour(Scene scene, Enemy enemy){
		super(scene, enemy);
		spawnEffect = new EnemySpawnEffect(scene, enemy, 250, 250);
		scene.add(spawnEffect);
	}
	
	@Override
	public void create(){
		
	}
	
	@Override
	public void update(){
		
		scene.getGrid().applyDirectedForce(new Vector3f(self.getVelocity().x, self.getVelocity().y, 0), new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 100);
		
		time += 5;
		Player player = scene.getPlayer();
		float toPlayer = player.getTransform().getTranslation().sub(self.getTransform().getTranslation()).direction();
		
		self.getVelocity().set(new Vector2f(FastMath.cosd(time) * angleSpeed, FastMath.sind(time) * angleSpeed));
		self.getVelocity().set(self.getVelocity().add(new Vector2f(FastMath.cosd(toPlayer) * speed, FastMath.sind(toPlayer) * speed)));
		self.getTransform().setOrientation(self.getVelocity().direction());
	    
	}

	@Override
	public void destroy() {
		spawnEffect.destroy();
	}
}