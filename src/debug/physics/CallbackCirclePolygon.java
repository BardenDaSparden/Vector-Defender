package debug.physics;

public class CallbackCirclePolygon extends CollisionCallback{
	
	public static final CallbackCirclePolygon instance = new CallbackCirclePolygon();
	
	public void solve(Manifold m, Volume circle, Volume polygon){
		CallbackPolygonCircle.instance.solve(m, polygon, circle);
		m.normal = m.normal.negate();
	}
	
}
