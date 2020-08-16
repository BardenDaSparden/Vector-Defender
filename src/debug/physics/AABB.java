package debug.physics;

import org.javatroid.math.Vector2f;

public class AABB {

	private Vector2f min, max, center;
	
	public AABB(float x, float y, float width, float height){
		this.center = new Vector2f(x, y);
		this.min = new Vector2f(x - width / 2, y - height / 2);
		this.max = new Vector2f(x + width / 2, y + height / 2);
	}
	
	public AABB(Vector2f min, Vector2f max){
		this.center = min.add(max.sub(min).scale(0.5f));
		this.min = min;
		this.max = max;
	}
	
	public boolean intersects(AABB other){
		if(min.x >= other.max.x || max.x < other.min.x)
			return false;
		
		if(min.y >= other.max.y || max.y < other.min.y)
			return false;
		
		return true;
	}
	
	public boolean contains(Vector2f point){
		
		if(point.x <= min.x || max.x < point.x)
			return false;
		
		if(point.y <= min.y || max.y < point.y)
			return false;
		
		return true;
		
	}
	
	public void move(Vector2f translation){
		this.min = min.add(translation);
		this.max = max.add(translation);
	}
	
	public Vector2f getMin(){
		return min.clone();
	}
	
	public Vector2f getMax(){
		return max.clone();
	}
	
	public float getWidth(){
		return max.x - min.x;
	}
	
	public float getHeight(){
		return max.y - min.y;
	}
	
	public Vector2f getCenter(){
		return center;
	}
	
}
