package debug.physics;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

public class CallbackPolygonPolygon extends CollisionCallback{

	public static final CallbackPolygonPolygon instance = new CallbackPolygonPolygon();
	
	public static final float SLOP = 0.02f;
	
	public void solve(Manifold m, Volume polygon, Volume polygon2){
		
		Polygon A = ((Polygon)polygon);
		Polygon B = ((Polygon)polygon2);
		
		ArrayList<Vector2f> normals = new ArrayList<Vector2f>();
		
		Vector2f aToB = B.getPosition().sub(A.getPosition());
		
		normals.addAll(CollisionMath.getNormals(A.getVertices()));
		normals.addAll(CollisionMath.getNormals(B.getVertices()));
		
		float minDepth = Float.MAX_VALUE;
		Vector2f normal = new Vector2f();
		
		for(Vector2f axis : normals){
			Projection pA = CollisionMath.project(A.getVertices(), axis);
			Projection pB = CollisionMath.project(B.getVertices(), axis);
			
			if(!pA.overlap(pB))
				return;
			
			float depth = pA.getOverlap(pB);
			
			if(depth < minDepth){
				minDepth = depth;
				normal = axis;
			}
			
		}
		
		if(!(minDepth >= 0.1f))
			return;
		
		m.overlap = true;
		m.penetration = minDepth;
		m.normal = normal;
		
		//aToB isnt co-directional with normal, flip normal
		if(Vector2f.dot(normal, aToB) < 0)
			m.normal = normal.negate();
		
		//Collision edges
		Edge e1 = CollisionMath.getFeature(A.getVertices(), m.normal);
		Edge e2 = CollisionMath.getFeature(B.getVertices(), m.normal.negate());
		
		renderFeatures.add(e1);
		renderFeatures.add(e2);
		
		Edge ref = null;
		Edge inc = null;
		boolean flip = false;
		
		float e1DotN = Math.abs(Vector2f.dot(e1.end.sub(e1.start), m.normal));
		float e2DotN = Math.abs(Vector2f.dot(e2.end.sub(e2.start), m.normal));
		
		if(e1DotN <= e2DotN){
			ref = e1;
			inc = e2;
		} else {
			ref = e2;
			inc = e1;
			flip = true;
		}
		
		Vector2f referenceDirection = ref.end.sub(ref.start).normalize();
		Vector2f referenceNormal = Vector2f.cross(referenceDirection, -1.0f);
		
		float origin = Vector2f.dot(referenceDirection, ref.start);
		ArrayList<Vector2f> points = CollisionMath.clip(inc.start, inc.end, referenceDirection, origin);
		if(points.size() < 2)
			return;
		
		origin = Vector2f.dot(referenceDirection, ref.end);
		ArrayList<Vector2f> points2 = CollisionMath.clip(points.get(0), points.get(1), referenceDirection.negate(), -origin);
		if(points2.size() < 2)
			return;
		
		m.contacts[0] = points2.get(0);
		m.contacts[1] = points2.get(1);
		
		if(flip)
			referenceNormal = referenceNormal.negate();
		
		float max = Vector2f.dot(referenceNormal, ref.max);
		boolean removeCP1 = (Vector2f.dot(referenceNormal, points2.get(0)) - max < 0);
		boolean removeCP2 = (Vector2f.dot(referenceNormal, points2.get(1)) - max < 0);
		
		if(removeCP1)
			m.contacts[0] = null;
		if(removeCP2)
			m.contacts[1] = null;
		
		m.normal = m.normal.negate();
		
		renderPoints.add(m.contacts[0]);
		renderPoints.add(m.contacts[1]);
		
	}
}
