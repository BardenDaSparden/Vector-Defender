package com.vecdef.ai;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Scene;

public class ZoomerBehavior extends Behaviour {

	final float INITIAL_SPEED = 3.5f;
	final float MAX_SPEED = 7.0f;
	float speed = INITIAL_SPEED;
	Vector2f movementDirection;
	
	public ZoomerBehavior(Scene scene, Enemy enemy) {
		super(scene, enemy);
		float randAngle = FastMath.random() * 360.0f;
		movementDirection = new Vector2f(FastMath.cosd(randAngle) * speed, FastMath.sind(randAngle) * speed);
		movementDirection.normalizei();
	}
	
	
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		
		Grid grid = scene.getGrid();
		int gridWidth = grid.getWidth();
		int gridHeight = grid.getHeight();
		
		Vector2f position = self.getTransform().getTranslation();
		float radius = self.getRadius();
		
		//-X Boundary
		if(position.x - radius < -gridWidth / 2.0f){
			movementDirection.x *= -1;
		}
		
		//+X Boundary
		if(position.x + radius > gridWidth / 2.0f){
			movementDirection.x *= -1;
		}
		
		//-Y Boundary
		if(position.y - radius < -gridHeight / 2.0f){
			movementDirection.y *= -1;
		}
		
		//Y Boundary
		if(position.y + radius > gridHeight / 2.0f){
			movementDirection.y *= -1;
		}
		
		if(speed + 0.04 < MAX_SPEED)
			speed += 0.04f;
		
		Vector2f velocity = self.getVelocity();
		velocity.x = movementDirection.x * speed;
		velocity.y = movementDirection.y * speed;
		
		self.getTransform().setOrientation(velocity.direction());
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
