package com.vecdef.physics;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform;

public interface IPhysics {
	
	public Transform getTransform();
	
	public Vector2f getVelocity();
	public Vector2f getAcceleration();
	
	public float getAngularVelocity();
	public void setAngularVelocity(float av);
	
	public float getTorque();
	public void setTorque(float t);
}
