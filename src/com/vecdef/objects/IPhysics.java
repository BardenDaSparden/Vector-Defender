package com.vecdef.objects;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform2D;

public interface IPhysics {
	
	public Transform2D getTransform();
	
	default public Vector2f getVelocity(){
		return new Vector2f(0, 0);
	}
	public void setVelocity(Vector2f velocity);
	
	default public Vector2f getAcceleration(){
		return new Vector2f(0, 0);
	}
	public void setAcceleration(Vector2f acceleration);
	
	default public float getAngularVelocity(){
		return 0;
	}
	public void setAngularVelocity(float a);
	
	default public float getTorque(){
		return 0;
	}
	public void setTorque(float t);
	
	default public float getMass(){
		return 1;
	}
	
}
