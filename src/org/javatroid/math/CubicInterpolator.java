package org.javatroid.math;

public class CubicInterpolator extends BaseInterpolator{

	private BezierCurve curve;
	
	public CubicInterpolator(Vector2f c1, Vector2f c2){
		curve = new BezierCurve();
		curve.setControlPoints(new Vector2f(0, 0), c1, c2, new Vector2f(1, 1));
	}
	
	public float interpolate(float start, float end, float weight){
		float w = curve.calculateBezierPoint(weight).y;
		return start + (end - start) * w;
	}
	
}
