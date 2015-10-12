package com.vecdef.objects;

import java.util.ArrayList;

import com.vecdef.model.Transform2D;

public class PhysicsManager {

	private static ArrayList<Entity> objects = new ArrayList<Entity>();
	
	public PhysicsManager(){
		
	}
	
	public void add(Entity object){
		objects.add(object);
	}
	
	public void addAll(ArrayList<Entity> objects){
		objects.addAll(objects);
	}
	
	public void remove(Entity object){
		objects.remove(object);
	}
	
	public void clear(){
		objects.clear();
	}
	
	public void integrate(float dt){
		for(int i = 0; i < objects.size(); i++){
			Entity object = objects.get(i);
			Transform2D transform = object.getTransform();
			
			object.angularVelocity += object.torque * dt;
			transform.setOrientation(transform.getOrientation() + object.angularVelocity);
			
			object.velocity.addi(object.acceleration.scale(dt));
			transform.setTranslation(transform.getTranslation().addi(object.velocity));
			
			object.getAcceleration().set(0, 0);
			object.setTorque(0f);
		}
	}	
}
