package com.vecdef.objects;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform2D;

public interface IPhysics {
	
	public Transform2D getTransform();
	
	public Vector2f getVelocity();
	public Vector2f getAcceleration();
	
	public float getAngularVelocity();
	public void setAngularVelocity(float av);
	
	public float getTorque();
	public void setTorque(float t);
}
