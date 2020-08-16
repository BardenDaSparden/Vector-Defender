package debug.physics;

import org.javatroid.math.Vector2f;

public class CallbackPolygonCircle extends CollisionCallback{

	public static final CallbackPolygonCircle instance = new CallbackPolygonCircle();

	public void solve(Manifold m, Volume polygon, Volume circle){
		
		Polygon A = ((Polygon)polygon);
		Circle B = ((Circle)circle);
		
		Vector2f aToB = B.getPosition().sub(A.getPosition());
		
		Edge e = CollisionMath.getFeature(A.getVertices(), aToB);
		renderFeatures.add(e);
		
		Vector2f edgeDirection = e.end.sub(e.start).normalize();
		
		float min = Vector2f.dot(edgeDirection, e.start);
		float p = Vector2f.dot(edgeDirection, B.getPosition());
		
		float min2 = Vector2f.dot(edgeDirection.negate(), e.end);
		float p2 = Vector2f.dot(edgeDirection.negate(), B.getPosition());
		
		boolean doVertexTest = !(p > min && p2 > min2);
		
		if(doVertexTest){
			//Vertex Collision
			Vector2f[] vertices = A.getVertices();
			for(Vector2f v : vertices){
				Vector2f dPos = B.getPosition().sub(v);
				if(dPos.lengthSquared() <= B.getRadius() * B.getRadius() + 0.1f){
					m.overlap = true;
					m.normal = dPos.normalize().negate();
					m.penetration = B.getRadius() - dPos.length();
					m.contacts[0] = B.getPosition().add(m.normal.scale(B.getRadius()));
					renderPoints.add(m.contacts[0]);
					
					return;
				}
			}
		} else {
			//Edge Collision
			Vector2f edgeNormal = Vector2f.cross(edgeDirection, -1.0f).negate().normalize();
			Projection pA = CollisionMath.project(new Vector2f[]{e.start, e.end}, edgeNormal);
			Projection pB = CollisionMath.project(new Vector2f[]{B.getPosition().sub(edgeNormal.scale(B.getRadius())), B.getPosition().add(edgeNormal.scale(B.getRadius()))}, edgeNormal);
			if(!pA.overlap(pB))
				return;
			
			m.overlap = true;
			m.normal = edgeNormal.negate();
			m.penetration = pA.getOverlap(pB);
			m.contacts[0] = B.getPosition().add(edgeNormal.negate().scale(B.getRadius()));
			
			renderPoints.add(m.contacts[0]);
			
		}
	}
}
