package org.javatroid.collision;

import org.javatroid.math.FastMath;
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
		if(min.x > other.max.x || max.x < other.min.x)
			return false;
		
		if(min.y > other.max.y || max.y < other.min.y)
			return false;
		
		return true;
	}
	
	public boolean contains(float x, float y){
		return contains(new Vector2f(x, y));
	}
	
	public boolean contains(Vector2f point){
		
		if(point.x <= min.x || max.x < point.x)
			return false;
		
		if(point.y <= min.y || max.y < point.y)
			return false;
		
		return true;
		
	}
	
	public void move(Vector2f translation){
		this.center = center.add(translation);
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
	
	public static AABB join(AABB a, AABB b){
		float minX = FastMath.min(a.getMin().x, b.getMin().x);
		float minY = FastMath.min(a.getMin().y, b.getMin().y);
		float maxX = FastMath.max(a.getMax().x, b.getMax().x);
		float maxY = FastMath.max(a.getMax().y, b.getMax().y);
		return new AABB(new Vector2f(minX, minY), new Vector2f(maxX, maxY));
	}
	
}
