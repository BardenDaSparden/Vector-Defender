package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform2D;

public class PhysicsSystem {

	ArrayList<IPhysics> objects = new ArrayList<IPhysics>();
	
	public PhysicsSystem(){
		
	}
	
	public void add(IPhysics object){
		objects.add(object);
	}
	
	public void remove(IPhysics object){
		objects.remove(object);
	}
	
	public void integrate(float dt){
		for(int i = 0; i < objects.size(); i++){
			IPhysics object = objects.get(i);
			
			Transform2D transform = object.getTransform();
			//Vector2f position = transform.getTranslation();
			
			float newAngularVelocity = object.getAngularVelocity() + object.getTorque();
			float newOrientation = transform.getOrientation() + newAngularVelocity;
			transform.setOrientation(newOrientation);
			
			//System.out.println("PHYSICS UPDATE->object.acceleration: " + object.getAcceleration());
			
			Vector2f newVelocity = object.getVelocity().add(object.getAcceleration());
			Vector2f newPosition = object.getTransform().getTranslation().add(newVelocity);
			object.getVelocity().set(newVelocity);
			transform.getTranslation().set(newPosition);
			
			object.getAcceleration().set(0, 0);
			object.setTorque(0f);
		}
	}	
	
	public int numObjects(){
		return objects.size();
	}
}
