package org.javatroid.math;

//import org.javatroid.graphics.GeometryRenderer;

public class BezierCurve {

	private Vector2f control0;
	private Vector2f control1;
	private Vector2f control2;
	private Vector2f control3;
	
	//private int segments = 32;
	
	//private Vector2f scale = new Vector2f(1, 1);
	
	public BezierCurve(){
		this.control0 = new Vector2f(0, 0);
		this.control1 = new Vector2f(0, 0);
		this.control2 = new Vector2f(0, 0);
		this.control3 = new Vector2f(0, 0);
	}
	
	public BezierCurve(BezierCurve curve){
		Vector2f[] c = curve.getControlPoints();
		setControlPoints(c[0], c[1], c[2], c[3]);
	}
	
	public Vector2f calculateBezierPoint(float t){
		float u = 1 - t;
		float tt = t * t;
		float uu = u * u;
		float uuu = uu * u;
		float ttt = tt * t;
		
		float firstTerm = uuu;
		float secondTerm = 3 * uu * t;
		float thirdTerm = 3 * u * tt;
		float fourthTerm = ttt;
		
		Vector2f pointOnCurve = new Vector2f(0, 0);
		
		pointOnCurve = control0.mul(new Vector2f(firstTerm, firstTerm));
		pointOnCurve = pointOnCurve.add(new Vector2f(secondTerm, secondTerm).mul(control1));
		pointOnCurve = pointOnCurve.add(new Vector2f(thirdTerm, thirdTerm).mul(control2));
		pointOnCurve = pointOnCurve.add(new Vector2f(fourthTerm, fourthTerm).mul(control3));
		
		return pointOnCurve;
	}
	/*
	public void draw(GeometryRenderer batcher){
		Vector2f start = calculateBezierPoint(0);
		Vector2f end = new Vector2f(0, 0);
		
		for(int i = 1; i <= segments; i++){
			float t = (float)i / (float) segments;
			end = calculateBezierPoint(t);
			batcher.drawLine(start.mul(scale), end.mul(scale));
			start = end;
		}
		
		batcher.setColor(Color4f.RED);
		
		batcher.drawCircle(control0.mul(scale).x, control0.mul(scale).y, 3);
		batcher.drawCircle(control1.mul(scale).x, control1.mul(scale).y, 3);
		batcher.drawCircle(control2.mul(scale).x, control2.mul(scale).y, 3);
		batcher.drawCircle(control3.mul(scale).x, control3.mul(scale).y, 3);
		
		batcher.setColor(Color4f.CYAN);
		batcher.drawLine(control0.mul(scale), control1.mul(scale));
		batcher.drawLine(control3.mul(scale), control2.mul(scale));
		
	}
	*/
	
	public Vector2f[] getControlPoints(){
		return new Vector2f[] {control0, control1, control2, control3};
	}
	
	public void setControlPoints(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3){
		this.control0 = p0;
		this.control1 = p1;
		this.control2 = p2;
		this.control3 = p3;
	}
}
