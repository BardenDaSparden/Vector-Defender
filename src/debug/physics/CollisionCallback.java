package debug.physics;

import java.util.ArrayList;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

public abstract class CollisionCallback {

	static ArrayList<Vector2f> renderPoints = new ArrayList<Vector2f>();
	static ArrayList<Edge> renderFeatures = new ArrayList<Edge>();
	
	public abstract void solve(Manifold m, Volume A, Volume B);
}

class Edge {

	public Vector2f start, end, max;
	
	public Edge(Vector2f start, Vector2f end, Vector2f max){
		this.start = start;
		this.end = end;
		this.max = max;
	}
	
	public Vector2f normalize(){
		return end.sub(start).normalize();
	}
	
}

class Projection {

	public float min, max;
	
	public Projection(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	public boolean overlap(Projection other){
		if(min >= other.max || max <= other.min){
			return false;
		} else {
			return true;
		}
	}
	
	public float getOverlap(Projection other){
		return FastMath.min((max - other.min), (other.max - min));
	}
	
}

