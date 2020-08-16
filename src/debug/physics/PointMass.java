package debug.physics;

import org.javatroid.math.Vector3f;

public class PointMass {

	Vector3f position = new Vector3f();
	Vector3f velocity = new Vector3f();
	Vector3f acceleration = new Vector3f();
	
	float damping = 0.95f;
	
	float invMass;
	
	public PointMass(Vector3f position, float invMass){
		this.position = position;
		this.invMass = invMass;
	}
	
	public void applyForce(Vector3f force){
		acceleration = acceleration.add(force.scale(invMass));
	}
	
	public void increaseDamping(float factor){
		damping *= factor;
	}
	
	public void update(){
		
		velocity = velocity.add(acceleration);
		position = position.add(velocity);
		
		acceleration.x = 0;
		acceleration.y = 0;
		acceleration.z = 0;
		
		if(velocity.lengthSquared() < 0.01f * 0.01f){
			velocity = new Vector3f();
		}
		
		velocity = velocity.scale(damping);
		damping = 0.98f;
		
		position.z *= 0.9f;
		
		if(position.z < 0.01f)
			position.z = 0;
		
		
	}
	
	public Vector3f getPosition(){
		return position;
	}
	
}
