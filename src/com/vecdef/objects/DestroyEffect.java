package com.vecdef.objects;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class DestroyEffect {

	public DestroyEffect(Vector2f position, int particleCount, float radius, Vector4f startColor, float startSpeed, int maxLife){
		for(int i = 0; i < particleCount; i++){
			float angle = (float)(i) / (float)(particleCount) * 360;
			
			float x = position.x + FastMath.cosd(angle) * (radius - 3 + FastMath.random() * 12);
			float y = position.y + FastMath.sind(angle) * (radius - 3 + FastMath.random() * 12);
			
			float vx = FastMath.cosd(angle) * (startSpeed + FastMath.random() * 2);
			float vy = FastMath.sind(angle) * (startSpeed + FastMath.random() * 2);
			
			Particle p = new Particle(x, y, startColor);
			
			p.getTransform().setOrientation(new Vector2f(vx, vy).direction());
			
			p.maxLife = maxLife;
			
			p.velocity.x = vx;
			p.velocity.y = vy;
			
			EntityManager.add(p);
		}
		
		for(int i = 0; i < particleCount / 4; i++){
			float angle = (float)(i) / (float)(particleCount / 4f) * 360;
			
			float x = position.x + FastMath.cosd(angle + FastMath.randomf(-10, 10)) * (radius / 4f + FastMath.random() * 4);
			float y = position.y + FastMath.sind(angle + FastMath.randomf(-10, 10)) * (radius / 4f + FastMath.random() * 4);
			
			float vx = FastMath.cosd(angle) * (startSpeed * 1.8f);
			float vy = FastMath.sind(angle) * (startSpeed * 1.8f);
			
			Particle p = new Particle(x, y, startColor);
			
			p.getTransform().setOrientation(new Vector2f(vx, vy).direction());
			
			p.maxLife = maxLife / 2;
			
			p.angularVelocity = FastMath.randomf(-FastMath.random() * 5, FastMath.random() * 5);
			p.velocity.x = vx;
			p.velocity.y = vy;
			
			EntityManager.add(p);
		}
		
	}
	
}
