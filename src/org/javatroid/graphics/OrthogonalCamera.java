package org.javatroid.graphics;

import org.javatroid.math.Matrix4f;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

public class OrthogonalCamera {

	private Matrix4f projection;
	private Matrix4f view;
	private Matrix4f combined;
	
	private Vector2f translation;
	private float orientation;
	private Vector2f scale;
	private float width, height;
	
	public OrthogonalCamera(){
		this(2, 2);
	}
	
	public OrthogonalCamera(float width, float height){
		projection = Matrix4f.createOrthographicProjection(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, -1, 1);
		view = new Matrix4f().identity();
		combined = new Matrix4f().identity();
		
		this.width = width;
		this.height = height;
		
		translation = new Vector2f();
		orientation = 0;
		scale = new Vector2f(1, 1);
		
		update();
		
	}
	
	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
		projection = Matrix4f.createOrthographicProjection(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, -1, 1);
		update();
	}
	
	/**
	 * This method updates view, and combined matrix after changes have been made to position, rotation, scale
	 */
	public void update(){
		
		Matrix4f t = Matrix4f.createTranslation( new Vector3f(-translation.x, -translation.y, 0) );
		Matrix4f r = Matrix4f.createRotationZ(-orientation);
		Matrix4f s = Matrix4f.createScale( new Vector3f(scale.x, scale.y, 1) );
		
		view = s.mul(r).mul(t);
		combined = projection.mul(view);
		
	}
	
	public void translate(float x, float y){
		translation.x += x;
		translation.y += y;
	}
	
	public void rotate(float a){
		orientation += a;
	}
	
	public void scale(float xFactor, float yFactor){
		scale.x += xFactor;
		scale.y += yFactor;
	}
	
	public float getViewWidth(){
		return width / scale.x;
	}
	
	public float getViewHeight(){
		return height / scale.y;
	}
	
	public Matrix4f getProjection() 	{ return projection; }
	public Matrix4f getView() 			{ return view; }
	public Matrix4f getCombined()		{ return combined; }
	
	public Vector2f getTranslation() 	{ return translation; }
	public float getOrientation() 		{ return orientation; }
	public void setOrientation(float a)	{ orientation = a; }
	public Vector2f getScale()   		{ return scale; }
	
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	
}
