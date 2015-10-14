package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform2D;

public class CollisionSystem {

	ArrayList<ICollidable> collidables;
	
	public CollisionSystem(){
		collidables = new ArrayList<ICollidable>();
	}
	
	public void add(ICollidable collidable){
		collidables.add(collidable);
	}
	
	public void remove(ICollidable collidable){
		collidables.remove(collidables);
	}
	
	public void checkCollision(){
		int n = collidables.size();
		
		ICollidable A = null;
		ICollidable B = null;
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				A = collidables.get(i);
				B = collidables.get(j);
				
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
					ContactEvent eventBA = new ContactEvent(B, A);
					
					A.onContact(eventAB);
					B.onContact(eventBA);
				}	
			}
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
	
}
