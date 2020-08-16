package debug.physics;

public class Collisions{
	
	static CollisionCallback[][] callback = {{CallbackCircleCircle.instance,  CallbackCirclePolygon.instance}, 
											 {CallbackPolygonCircle.instance, CallbackPolygonPolygon.instance}};
	
	public static void solve(Manifold m, Volume A, Volume B){
		int typeA = A.getVolumeType().ordinal();
		int typeB = B.getVolumeType().ordinal();
		
		callback[typeA][typeB].solve(m, A, B);
		
	}
	
}