package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform2D;

public class CollisionSystem {

	ArrayList<ICollidable> collidables;
	ArrayList<ICollidable> nearbyCollidables;
	ArrayList<ContactEvent> eventStack;
	
	public CollisionSystem(){
		collidables = new ArrayList<ICollidable>();
		nearbyCollidables = new ArrayList<ICollidable>();
	}
	
	public void add(ICollidable collidable){
		collidables.add(collidable);
	}
	
	public void remove(ICollidable collidable){
		collidables.remove(collidable);
	}
	
	public void checkCollision(){
		
		ICollidable A = null;
		ICollidable B = null;
		
		int n = collidables.size();
		for(int i = 0; i < n; i++){
			A = collidables.get(i);
			
			if(A.getCollisionMask() == Masks.NONE)
				continue;
			
			nearbyCollidables.clear();
			getNearbyCollidables(A.getTransform().getTranslation(), 300, nearbyCollidables, A);
			
			for(int j = 0; j < nearbyCollidables.size(); j++){
				
				B = nearbyCollidables.get(j);
				
				//If and A and B are the same references
				if(A.equals(B)){
					continue;
				}
				
				int collisionMask = A.getCollisionMask();
				int otherGroupMask = B.getGroupMask();
				
				//If A collides with B
				if((collisionMask & otherGroupMask) == 0x00){
					continue;
				}
				
				boolean intersect = testIntersection(A, B);
				if(intersect){
					ContactEvent eventAB = new ContactEvent(A, B);
					A.onContact(eventAB);
				}	
			}
		}
	}
	
	void getNearbyCollidables(Vector2f position, int radius, ArrayList<ICollidable> list, ICollidable self){
		int n = collidables.size();
		for(int i = 0; i < n; i++){
			ICollidable collidable = collidables.get(i);
			Vector2f dPos = collidable.getTransform().getTranslation().sub(position);
			if(dPos.lengthSquared() <= radius * radius)
				if(!self.equals(collidable))
					list.add(collidable);
		}
	}
	
	boolean testIntersection(ICollidable A, ICollidable B){
		Transform2D ta = A.getTransform();
		Transform2D tb = B.getTransform();
		
		Vector2f pa = ta.getTranslation();
		Vector2f pb = tb.getTranslation();
		Vector2f pc = pb.sub(pa);
		
		int ra = A.getRadius();
		int rb = B.getRadius();
		
		//sqrt((dx * dx + dy * dy)) <= ra + rb;
		//(dx * dx) + (dy * dy) <= (ra + rb)^2;
		
		float lenSqr = (pc.x * pc.x) + (pc.y * pc.y);
		float rSqr = (float)Math.pow(ra + rb, 2);
		
		return lenSqr <= rSqr;
	}
	
	public int numObjects(){
		return collidables.size();
	}
	
}
