package com.vecdef.physics;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform;
import com.vecdef.objects.IPoolable;

public class PhysicsSystem {

	ArrayList<IPhysics> objects;
	
	public PhysicsSystem(){
		objects = new ArrayList<IPhysics>();
	}
	
	public void add(IPhysics object){
		objects.add(object);
	}
	
	public void remove(IPhysics object){
		objects.remove(object);
	}
	
	public void integrate(){
		for(int i = 0; i < objects.size(); i++){
			IPhysics object = objects.get(i);
			IPoolable poolable = (IPoolable)object;
			
			if(poolable.isRecycled())
				continue;
			
			Transform transform = object.getTransform();
			//Vector2f position = transform.getTranslation();
			
			float newAngularVelocity = object.getAngularVelocity() + (object.getTorque());
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
