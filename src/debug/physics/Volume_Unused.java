package debug.physics;

import org.javatroid.math.Vector2f;

public class Volume_Unused {

	Vector2f[] vertices;
	Vector2f[] normals;
	float area;
	AABB bound;
	
	Vector2f position;
	float orientation;
	
	//private Volume_Unused(){}
	
	public Volume_Unused(Vector2f...vertices){
		this.vertices = vertices;
		
		calculateNormals();
		calculateAABB();
		
		this.area = calculateArea();
		this.position = calculateCentroid();
		this.orientation = 0;
		
		move(position.negate());
	}
	
	private void calculateNormals(){
		this.normals = new Vector2f[vertices.length];
		for(int i = 0; i < vertices.length; i++){
			int i2 = (i + 1) % vertices.length;
			Vector2f v0 = vertices[i];
			Vector2f v1 = vertices[i2];
			normals[i] = Vector2f.cross(v1.sub(v0), -1.0f).normalize();
		}
	}
	
	private void calculateAABB(){
		Vector2f min = new Vector2f();
		Vector2f max = new Vector2f();
		
		min.x = Float.MAX_VALUE;
		min.y = Float.MAX_VALUE;
		
		max.x = -Float.MAX_VALUE;
		max.y = -Float.MAX_VALUE;
		
		for(int i = 0; i < vertices.length; i++){
			Vector2f v = vertices[i];
			
			if(v.x < min.x)
				min.x = v.x;
			
			if(v.y < min.y)
				min.y = v.y;
			
			if(v.x > max.x)
				max.x = v.x;
			
			if(v.y > max.y)
				max.y = v.y;
			
		}
		
		bound = new AABB(min, max);
	}
	
	private float calculateArea(){
		
		double area = 0;
		
		for(int i = 0; i < vertices.length; i++){
			
			int i2 = (i + 1) % vertices.length;
			Vector2f v0 = vertices[i];
			Vector2f v1 = vertices[i2];
			
			area += (v0.x * v1.y - v1.x * v0.y);
		}
		
		area /= 2;
		
		return (float) area;
		
	}
	
	private Vector2f calculateCentroid(){
		Vector2f centroid = new Vector2f();
		
		for(int i = 0; i < vertices.length; i++){
			
			int i2 = (i + 1) % vertices.length;
			Vector2f v0 = vertices[i];
			Vector2f v1 = vertices[i2];
			
			centroid.x += (v0.x + v1.x) * (v0.x * v1.y - v1.x * v0.y);
			centroid.y += (v0.y + v1.y) * (v0.x * v1.y - v1.x * v0.y);
		}
		
		centroid.x /= 6 * area;
		centroid.y /= 6 * area;
		
		return centroid;
		
	}
	
	public Projection project(Vector2f axis){
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		
		for(int i = 0; i < vertices.length; i++){
			Vector2f v = vertices[i];
			float dot = Vector2f.dot(v, axis);
			
			if(dot < min)
				min = dot;
			
			if(dot > max)
				max = dot;
		}
		
		return new Projection(min, max);
	}
	
	public void move(Vector2f translation){
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = vertices[i].add(translation);
		}
		position = position.add(translation);
		bound.move(translation);
		
	}
	
	public void orient(float angle){
		move(position.negate());
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = vertices[i].rotate(angle);
		}
		
		move(position);
		calculateNormals();
		calculateAABB();
	}
	
	public Vector2f[] getVertices(){
		return vertices;
	}
	
	public Vector2f[] getNormals(){
		return normals;
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public void setPosition(Vector2f newPosition){
		Vector2f dPos = newPosition.sub(position);
		move(dPos);
	}
	
	public void setOrientation(float orientation){
		this.orientation = orientation;
		orient(orientation);
	}
	
	public float getOrientation(){
		return orientation;
	}
	
	public float getArea(){
		return area;
	}
	
	public AABB getAABB(){
		return bound;
	}
	
	
//	public Volume clone(){
//		Volume volume = new Volume();
//		
//		Vector2f[] vertices = new Vector2f[this.vertices.length];
//		Vector2f[] normals = new Vector2f[this.normals.length];
//		
//		for(int i = 0; i < vertices.length; i++){
//			vertices[i] = this.vertices[i].clone();
//			normals[i] = this.normals[i].clone();
//		}
//		
//		Vector2f pos = position.clone();
//		float a = area;
//		AABB bound = new AABB(this.bound.getMin(), this.bound.getMax());
//		
//		volume.vertices = vertices;
//		volume.normals = normals;
//		volume.position = pos;
//		volume.area = a;
//		volume.bound = bound;
//		return volume;
//	}
//	
	
}
