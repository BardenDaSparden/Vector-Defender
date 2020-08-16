package debug.physics;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import debug.physics.Volume.VolumeType;

public class CollisionMath {
	
	public static Vector2f getForceContact(Volume volume, Vector2f axis){
		if(volume.getVolumeType() == VolumeType.CIRCLE){
			
			Circle circle = ((Circle) volume);
			Vector2f position = circle.getPosition();
			float radius = circle.getRadius();
			return position.add(axis.scale(radius));
			
		} else if(volume.getVolumeType() == VolumeType.POLYGON){
			
			Polygon polygon = ((Polygon)volume);
			Edge e = getFeature(polygon.getVertices(), axis);
			return e.max;
			
		}
		
		return null;
	}
	
	public static Vector2f getMax(Vector2f[] vertices, Vector2f axis){
		int index = 0;
		float max = Vector2f.dot(vertices[0], axis);
		
		for(int i = 1; i < vertices.length; i++){
			Vector2f v = vertices[i];
			float dot = Vector2f.dot(v, axis);
			if(dot > max){
				index = i;
				max = dot;
			}
		}
		
		return vertices[index];
	}
	
	public static Edge getFeature(Vector2f[] vertices, Vector2f normal){
		int index = 0;
		double max = Vector2f.dot(vertices[0], normal);
		
		for(int i = 1; i < vertices.length; i++){
			double projection = Vector2f.dot(vertices[i], normal);
			if(projection > max){
				max = projection;
				index = i;
			}
		}
		
		int i2 = 0;
		if(index == 0){
			i2 = vertices.length - 1;
		} else {
			i2 = (index - 1) % vertices.length;
		}
		
		Vector2f furthest = vertices[index];
		Vector2f vNext = vertices[(index + 1) % vertices.length];
		Vector2f vPrev = vertices[i2];
		
		Vector2f lN = Vector2f.cross(vNext.sub(furthest), -1.0f);
		Vector2f rN = Vector2f.cross(furthest.sub(vPrev), -1.0f);
		
		float rd = Vector2f.dot(rN, normal);
		float ld = Vector2f.dot(lN, normal);
		
		if(rd > ld){
			return new Edge(furthest, vNext, furthest);
		} else {
			
			return new Edge(vPrev, furthest, furthest);
		}
		
	}
	
	public static Projection project(Vector2f[] vertices, Vector2f axis){
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
	
	public static ArrayList<Vector2f> clip(Vector2f v1, Vector2f v2, Vector2f normal, float origin){
		ArrayList<Vector2f> points = new ArrayList<Vector2f>();
		
		float d1 = Vector2f.dot(normal, v1) - origin;
		float d2 = Vector2f.dot(normal, v2) - origin;

		if(d1 >= 0.0)
			points.add(v1);
		if(d2 >= 0.0)
			points.add(v2);
		
		if(d1 * d2 < 0.0){
			Vector2f e = v2.sub(v1);
			float u = (d1) / (d1 - d2);
			e = e.scale(u);
			e = e.add(v1);
			points.add(e);
		}
		
		return points;
		
	}
	
	public static ArrayList<Vector2f> getNormals(Vector2f[] vertices){
		ArrayList<Vector2f> normals = new ArrayList<Vector2f>(vertices.length);
		
		for(int i = 0; i < vertices.length; i++){
			Vector2f v0 = vertices[i];
			Vector2f v1 = vertices[(i + 1) % vertices.length];
			Vector2f normal = Vector2f.cross(v1.sub(v0), -1.0f).normalize();
			normals.add(normal);
		}
		
		return normals;
	}
	
}
