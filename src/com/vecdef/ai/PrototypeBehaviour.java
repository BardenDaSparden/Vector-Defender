package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Player;

public class PrototypeBehaviour extends Behavior{
	
	float speed = 5.0f;
	
	EnemySpawnEffect spawnEffect;
	
	public PrototypeBehaviour(Scene scene, Enemy enemy){
		super(scene, enemy);
		spawnEffect = new EnemySpawnEffect(scene, enemy, 250, 250);
		scene.add(spawnEffect);
	}
	
	@Override
	public void create(){
		
	}
	
	public void update(){	
		scene.getGrid().applyExplosiveForce(50, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 100);
		
		Player player = scene.getPlayer();
		Vector2f toPlayer = player.getTransform().getTranslation().sub(self.getTransform().getTranslation());

		self.getTransform().setOrientation(self.getVelocity().direction());
		self.getVelocity().set(new Vector2f(FastMath.cosd(toPlayer.direction()) * speed, FastMath.sind(toPlayer.direction()) * speed));

	    speed += 0.005F;
	}
	
	@Override
	public void destroy() {
		spawnEffect.destroy();
	}
}