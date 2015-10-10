package com.shapedefender.objects;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.shapedefender.model.Transform2D;

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
	
	public void updatePhysics(float dt){
		for(int i = 0; i < objects.size(); i++){
			Entity object = objects.get(i);
			Transform2D transform = object.getTransform();
			
			object.angularVelocity += object.torque * dt;
			transform.setOrientation(transform.getOrientation() + object.angularVelocity);
			
			object.velocity.addi(object.acceleration.scale(dt));
			transform.setTranslation(transform.getTranslation().addi(object.velocity));
			
			object.setAcceleration(new Vector2f(0, 0));
			object.setTorque(0f);
		}
	}
	
	public static void applyForce(Vector2f force){
		
		for(int i = 0; i < objects.size(); i++){
			Entity object = objects.get(i);
			float mass = object.getMass();
			
			object.acceleration = force.scale(1.0f / mass);
		}
	}
	
}
