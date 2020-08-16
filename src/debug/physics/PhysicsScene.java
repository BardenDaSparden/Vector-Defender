package debug.physics;

import java.util.ArrayList;
import org.javatroid.math.Vector2f;

public class PhysicsScene {
	
	static final Vector2f GRAVITY = new Vector2f(0, -10f);
	
	QuadTree<RigidBody> quadTree;
	
	ArrayList<RigidBody> objects;
	ArrayList<RigidBody> others;
	ArrayList<ContactPair> pairs;
	ArrayList<Manifold> manifolds;
	
	boolean isDebugMode = false;
	
	int iterations;
	float dt;
	
	public PhysicsScene(int iterations, float dt){
		quadTree = new QuadTree<RigidBody>(new AABB(0, 0, 0, 0), 0);
		objects = new ArrayList<RigidBody>();
		pairs = new ArrayList<ContactPair>();
		others = new ArrayList<RigidBody>();
		manifolds = new ArrayList<Manifold>();
		this.iterations = iterations;
		this.dt = dt;
	}
	
	public void update(){
		quadTree.clear();
		others.clear();
		manifolds.clear();
		pairs.clear();
		
		//Insert objects into grid
		for(int i = 0; i < objects.size(); i++){
			RigidBody object = objects.get(i);
			quadTree.insert(object);
		}
		
		//Generate possible contact pairs
		for(int i = 0; i < objects.size(); i++){
			
			RigidBody object = objects.get(i);
			
			quadTree.get(object, others);
			
			for(int j = 0; j < others.size(); j++){
				
				RigidBody object2 = others.get(j);
				
				if(!(object == object2)){
					if(!(object.isStatic() && object2.isStatic())){
						
						AABB bound = object.getVolume().getAABB();
						AABB bound2 = object2.getVolume().getAABB();
						
						if(bound.intersects(bound2)){
							ContactPair pair = new ContactPair(object, object2);
							
							if(!pairs.contains(pair)){
								pairs.add(pair);
								manifolds.add(new Manifold(object, object2));
							}
						}
					}
				}				
			}
			
			others.clear();
			
		}
		
		for(int i = 0; i < objects.size(); i++){
			RigidBody body = objects.get(i);
			if(body.isStatic())
				continue;
			integrateForces(body);
		}
		
		for(int i = 0; i < manifolds.size(); i++){
			Manifold m = manifolds.get(i);
			m.solveCollision();
			if(m.overlap){
				m.applyMTV();
			}
		}
		
		for(int i = 0; i < manifolds.size(); i++){
			Manifold m = manifolds.get(i);
			m.initialize();
		}
		
		for(int j = 0; j < iterations; j++){
			for(int i = 0; i < manifolds.size(); i++){
				Manifold m = manifolds.get(i);
				m.newImpulse();
			}
		}
		
		for(int i = 0; i < objects.size(); i++){
			RigidBody body = objects.get(i);
			if(body.isStatic())
				continue;
			integrateVelocities(body);
		}
		
		for(int i = 0; i < manifolds.size(); i++){
			Manifold m = manifolds.get(i);
			m.positionCorrection();
		}
		
		for(int i = 0; i < objects.size(); i++){
			RigidBody body = objects.get(i);
			clearForces(body);
		}
		
	}
	
	public void integrateVelocities(RigidBody body){
		body.setPosition(body.getPosition().add(body.getVelocity()));
		body.setOrientation(body.getOrientation() + body.getAngularVelocity());
	}
	
	public void integrateForces(RigidBody body){
		body.setVelocity(body.getVelocity().add(body.getForce().scale(body.getInvMass()).scale(dt)).add(GRAVITY.scale(dt)));
		body.setAngularVelocity(body.getAngularVelocity() + body.getTorque() * body.getInvInertia() * (dt));
	}
	
	public void clearForces(RigidBody body){
		body.setForce(new Vector2f(0, 0));
		body.setTorque(0);
	}
	
	public void applyForce(Vector2f f){
		for(int i = 0; i < objects.size(); i++){
			RigidBody body = objects.get(i);
			if(body.isStatic())
				continue;
			body.applyForce(f);
		}
	}
	
	public void add(RigidBody object){
		objects.add(object);
	}
	
	public void remove(RigidBody object){
		objects.remove(object);
	}
	
	public void clear(){
		objects.clear();
		manifolds.clear();
		pairs.clear();
	}
	
	public int getSize(){
		return objects.size();
	}
	
	public void toggleDebugMode(){
		isDebugMode = !isDebugMode;
	}
	
}
