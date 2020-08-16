package com.vecdef.collision;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.vecdef.model.Transform;
import com.vecdef.objects.IPoolable;
import com.vecdef.util.Masks;

public class CollisionSystem {

	ArrayList<ICollidable> collidables;
	ArrayList<ICollidable> nearbyCollidables;
	LooseGrid broadphase;
	
	public CollisionSystem(){
		collidables = new ArrayList<ICollidable>();
		nearbyCollidables = new ArrayList<ICollidable>();
		broadphase = new LooseGrid(2000, 2000, 100, 100);
	}
	
	public void add(ICollidable collidable){
		collidables.add(collidable);
	}
	
	public void remove(ICollidable collidable){
		collidables.remove(collidable);
	}
	
	public void checkCollision(){
		
		broadphase.clear();
		for(int i = 0; i < collidables.size(); i++){
			ICollidable collidable = collidables.get(i);
			IPoolable poolable = (IPoolable) collidable;
			if(!poolable.isRecycled())
				broadphase.insert(collidable);
				
		}
		
		ICollidable A = null;
		ICollidable B = null;
		
		IPoolable poolableA = null;
		IPoolable poolableB = null;
		
		for(int i = 0; i < collidables.size(); i++){
			A = collidables.get(i);
			poolableA = (IPoolable)A;
			
			if(A.getCollisionMask() == Masks.NONE || poolableA.isRecycled() || A.getRadius() <= 0.001f)
				continue;
			
			Vector2f position = A.getTransform().getTranslation();
			nearbyCollidables.clear();
			broadphase.query(position.x, position.y, nearbyCollidables);
			//getNearbyCollidables(A.getTransform().getTranslation(), (A.getRadius() + 10) * 2, nearbyCollidables, A);
			
			
			for(int j = 0; j < nearbyCollidables.size(); j++){
				
				B = nearbyCollidables.get(j);
				poolableB = (IPoolable)B;
				
				if(B.getCollisionMask() == Masks.NONE || poolableB.isRecycled())
					continue;
				
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
	
	void getNearbyCollidables(Vector2f position, int radius, ArrayList<ICollidable> list, ICollidable self){
		int n = collidables.size();
		for(int i = 0; i < n; i++){
			ICollidable collidable = collidables.get(i);
			IPoolable poolable = (IPoolable) collidable;
			if(collidable.getCollisionMask() == Masks.NONE || poolable.isRecycled() || collidable.getRadius() <= 0.001f)
				continue;
			Vector2f dPos = collidable.getTransform().getTranslation().sub(position);
			if(dPos.lengthSquared() <= radius * radius)
				if(!self.equals(collidable))
					list.add(collidable);
		}
	}
	
	boolean testIntersection(ICollidable A, ICollidable B){
		Transform ta = A.getTransform();
		Transform tb = B.getTransform();
		
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
