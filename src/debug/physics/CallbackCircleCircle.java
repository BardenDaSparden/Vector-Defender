package debug.physics;

import org.javatroid.math.Vector2f;

public class CallbackCircleCircle extends CollisionCallback{

	public static final CallbackCircleCircle instance = new CallbackCircleCircle();
	
	public void solve(Manifold m, Volume circle, Volume circle2) {
		
		Circle A = (Circle)circle;
		Circle B = (Circle)circle2;
		
		Vector2f dPos = B.getPosition().sub(A.getPosition());
		float radiusSquared = (A.getRadius() + B.getRadius()) * (A.getRadius() + B.getRadius());
		
		if(dPos.lengthSquared() - 0.1f > radiusSquared){
			m.overlap = false;
			return;
		}
		
		float length = (A.getRadius() + B.getRadius()) - dPos.length();
		Vector2f normal = null;
		
		if(length <= 0.01f)
			normal = new Vector2f(0, 1);
		else 
			normal = dPos.normalize().negate();
		
		m.overlap = true;
		m.normal = normal;
		m.penetration = length;
		m.contacts[0] = A.getPosition().add(normal.negate().scale(A.getRadius()));
		
		renderPoints.add(m.contacts[0]);
		
	}

}
