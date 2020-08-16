package debug.physics;

import org.javatroid.math.Vector2f;

import debug.physics.Volume.VolumeType;

public class RigidBody {

	private Volume volume;
	private Material material;
	
	private Vector2f position;
	private Vector2f velocity;
	private Vector2f force;
	
	private float mass, invMass;
	
	private float inertia, invInertia;
	
	private float orientation, angularVelocity, torque;
	
	private boolean isStatic = false;
	
	public RigidBody(Volume volume, Material material){
		this.volume = volume;
		this.material = material;
		
		setPosition(new Vector2f(0, 0));
		setVelocity(new Vector2f(0, 0));
		this.force = new Vector2f(0, 0);
		
		this.mass = volume.getArea() * material.getDensity();
		this.invMass = (mass == 0) ? 0 : 1.0f / mass;
		
		if(volume.getVolumeType() == VolumeType.POLYGON){
			this.inertia = mass * mass;
			this.inertia /= 16;
			this.invInertia = 1.0f / inertia;
		} else {
			Circle circle = (Circle)volume;
			float r = circle.getRadius();
			this.inertia = (float) ((Math.PI / 2) * r * r * r * r);
			this.invInertia = 1.0f / inertia;
		}
		
		this.invInertia = 1.0f / inertia;
		
		this.orientation = 0;
		this.angularVelocity = 0;
		this.torque = 0;
	}
	
	public void update(float dt){
		Vector2f vel = velocity.add(force.scale(invMass));
		Vector2f pos = position.add(velocity);
		
		setVelocity(vel);
		setPosition(pos);
		setOrientation(orientation + angularVelocity);
		
		force.x = 0;
		force.y = 0;
	}
	
	public void applyForce(Vector2f f){
		force = force.add(f);
	}
	
	public void applyImpulse(Vector2f impulse, Vector2f contact){
		velocity = velocity.add(impulse.scale(invMass));
		angularVelocity += Vector2f.cross(contact, impulse) * invInertia;
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public void setPosition(Vector2f position){
		this.position = position;
		this.volume.setPosition(position);
	}
	
	public float getOrientation(){
		return orientation;
	}
	
	public void setOrientation(float orientation){
		
		this.volume.rotate(orientation - this.orientation);
		this.orientation = orientation;
	}
	
	public float getAngularVelocity(){
		return angularVelocity;
	}
	
	public void setAngularVelocity(float omega){
		this.angularVelocity = omega;
	}
	
	public float getTorque(){
		return torque;
	}
	
	public void setTorque(float t){
		this.torque = t;
	}
	
	public Vector2f getVelocity(){
		return velocity;
	}
	
	public void setVelocity(Vector2f velocity){
		this.velocity = velocity;
	}
	
	public Vector2f getForce(){
		return force;
	}
	
	public void setForce(Vector2f f){
		this.force = f;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public Volume getVolume(){
		return volume;
	}
	
	public float getMass(){
		return mass;
	}
	
	public float getInvMass(){
		return invMass;
	}
	
	public float getInertia() {
		return inertia;
	}

	public float getInvInertia() {
		return invInertia;
	}

	public void setStatic(){
		this.mass = Float.MAX_VALUE;
		this.invMass = 0;
		this.inertia = Float.MAX_VALUE;
		this.invInertia = 0;
		this.isStatic = true;
	}
	
	public boolean isStatic(){
		return isStatic;
	}
	
}
