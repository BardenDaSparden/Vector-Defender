package org.javatroid.collision;

import org.javatroid.math.Vector2f;

public class Polygon extends Volume{

	Vector2f[] vertices;
	float area;
	
	public Polygon(Vector2f...verticesInObjectSpace){
		this.vertices = verticesInObjectSpace;
		this.area = calculateArea();
		this.position = calculateCentroid();
		move(position.negate());
	}
	
	private Vector2f calculateCentroid(){
		
		Vector2f centroid = new Vector2f();
		
		for(int i = 0; i < vertices.length; i++){
			Vector2f v = vertices[i];
			Vector2f v2 = vertices[(i + 1) % vertices.length];
			
			float partial = (v.x * v2.y - v2.x * v.y);
			
			centroid.x += (v.x + v2.x) * partial;
			centroid.y += (v.y + v2.y) * partial;
		}
		
		float area = getArea();
		centroid.x /= 6 * area;
		centroid.y /= 6 * area;
		
		return centroid;
	}
	
	protected float calculateArea(){
		float area = 0;
		
		for(int i = 0; i < vertices.length; i++){
			Vector2f v = vertices[i];
			Vector2f v2 = vertices[(i + 1) % vertices.length];
			area += (v.x * v2.y) - (v2.x * v.y);
		}
		
		area /= 2.0f;
		return area;
	}
	
	public void move(Vector2f translation){
		this.position = position.add(translation);
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = vertices[i].add(translation);
		}
	}
	
	public void rotate(float angle){
		Vector2f position = this.position.clone();
		move(position.negate());
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = vertices[i].rotate(angle);
		}
		
		move(position);
	}
	
	public AABB getAABB(){
		Vector2f min = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
		Vector2f max = new Vector2f(-Float.MAX_VALUE, -Float.MAX_VALUE);
		
		for(int i = 0; i < vertices.length; i++){
			Vector2f v = vertices[i];
			
			if(v.x < min.x)
				min.x = v.x;
			if(v.x > max.x)
				max.x = v.x;
			if(v.y < min.y)
				min.y = v.y;
			if(v.y > max.y)
				max.y = v.y;
		}
		
		return new AABB(min, max);
		
	}
	
	public VolumeType getVolumeType(){
		return VolumeType.POLYGON;
	}
	
	public float getArea(){
		return area;
	}
	
	public Vector2f[] getVertices(){
		return vertices;
	}
	
	public static Polygon createRectangle(float x, float y, float width, float height, float rot){
		Vector2f[] vertices = new Vector2f[4];
		vertices[0] = new Vector2f(-width / 2, -height / 2).rotate(rot).add(new Vector2f(x, y));
		vertices[1] = new Vector2f(width / 2, -height / 2).rotate(rot).add(new Vector2f(x, y));
		vertices[2] = new Vector2f(width / 2, height / 2).rotate(rot).add(new Vector2f(x, y));
		vertices[3] = new Vector2f(-width / 2, height / 2).rotate(rot).add(new Vector2f(x, y));
		return new Polygon(vertices);
	}
	
}
