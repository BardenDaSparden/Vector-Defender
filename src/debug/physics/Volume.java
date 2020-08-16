package debug.physics;

import org.javatroid.math.Vector2f;

public abstract class Volume {

	public enum VolumeType {
		CIRCLE(), POLYGON()
	}
	
	protected Vector2f position;
	protected float orientation;
	
	public Volume(){
		this.position = new Vector2f();
		this.orientation = 0;
	}
	
	public abstract void move(Vector2f translation);
	public abstract void rotate(float angle);
	public abstract float getArea();
	public abstract AABB getAABB();
	public abstract VolumeType getVolumeType();
	
	public Vector2f getPosition(){
		return position;
	}
	
	public void setPosition(Vector2f newPosition){
		move(newPosition.sub(position));
	}
	
	public float getOrientation(){
		return orientation;
	}
	
	public void setOrientation(float newOrientation){
		rotate(newOrientation - orientation);
	}
	
}
