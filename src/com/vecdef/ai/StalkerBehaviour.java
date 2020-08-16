package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;

public class StalkerBehaviour extends Behaviour{
	
	float speed = 2.0F;
	float angleSpeed = 7;
	float time = 0;
	
	public StalkerBehaviour(Scene scene, Enemy enemy){
		super(scene, enemy);
	}
	
	@Override
	public void create(){
		
	}
	
	@Override
	public void update(){
		
		scene.getGrid().applyExplosiveForce(5 * self.getVelocity().length(), new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 50);
		
		time += 3.75f;
		Vector2f position = self.getTransform().getTranslation();
		Player player = scene.getNearestPlayer(position.x, position.y);
		float toPlayer = player.getTransform().getTranslation().sub(self.getTransform().getTranslation()).direction();
		
		self.getVelocity().set(new Vector2f(FastMath.cosd(time) * angleSpeed, FastMath.sind(time) * angleSpeed));
		self.getVelocity().set(self.getVelocity().add(new Vector2f(FastMath.cosd(toPlayer) * speed, FastMath.sind(toPlayer) * speed)));
		self.getTransform().setOrientation(self.getVelocity().direction());
	    
	}

	@Override
	public void destroy() {
		
	}
}