package debug.physics;

import org.javatroid.math.Vector2f;

public class Circle extends Volume{

	private float radius;
	private float area;
	
	public Circle(Vector2f position, float radius){
		setPosition(position);
		this.radius = radius;
		this.area = (float)(Math.PI * radius * radius);
	}
	
	public void move(Vector2f translation) {
		this.position = position.add(translation);
	}

	public void rotate(float angle) {
		this.orientation += angle;
	}
	
	public AABB getAABB(){
		return new AABB(position.sub(new Vector2f(radius, radius)), position.add(new Vector2f(radius, radius)));
	}
	
	public VolumeType getVolumeType(){
		return VolumeType.CIRCLE;
	}
	
	public float getArea(){
		return area;
	}
	
	public float getRadius(){
		return radius;
	}
	
}
