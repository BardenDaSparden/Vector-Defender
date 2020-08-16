package debug.physics;

import org.javatroid.math.Vector3f;

public class Spring {

	PointMass start, end;
	
	float targetLength;
	float stiffness;
	float damping;
	
	public Spring(PointMass start, PointMass end, float stiffness, float damping){
		this.start = start;
		this.end = end;
		this.stiffness = stiffness;
		this.damping = damping;
		this.targetLength = end.position.sub(start.position).length() * 0.95f;
	}
	
	public void update(){
		Vector3f x = start.position.sub(end.position);
		
		float length = x.length();
		
		//ensure pulling, not pushing
		if(length > targetLength){
			
			x = x.normalize().scale(length - targetLength);
			Vector3f dv = end.velocity.sub(start.velocity);
			Vector3f force = x.scale(stiffness).sub(dv.scale(damping));
			
			start.applyForce(force.negate());
			end.applyForce(force);
		}
	}
	
	public PointMass getStart(){
		return start;
	}
	
	public PointMass getEnd(){
		return end;
	}
	
}
