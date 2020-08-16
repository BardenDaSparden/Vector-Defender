package org.javatroid.math;

public class Transform2D {

	private Vector2f translation;
	private float rotation;
	private Vector2f scale;
	private Matrix3f transformation;
	
	public Transform2D(){
		translation = new Vector2f();
		rotation = 0;
		scale = new Vector2f();
		transformation = new Matrix3f().identity();
	}
	
	public void update(){
		System.out.println("MATRIX NEEDS UPDATED");
	}
	
	public void translate(float x, float y){
		translate(new Vector2f(x, y));
	}
	
	public void translate(Vector2f t){
		translation.addi(t);
	}
	
	public void rotate(float a){
		rotation += a;
	}
	
	public void scale(Vector2f s){
		scale.addi(s);
	}
	
	public Matrix3f getTransformMatrix(){ return transformation; }
	public Vector2f getTranslation() { return translation; }
	public void setTranslation(Vector2f translation) { this.translation = translation; }
	public float getRotation(){ return rotation; }
	public void setRotation(float rotation){ this.rotation = rotation; }
	public Vector2f getScale() { return scale; }
	public void setScale(Vector2f scale){ this.scale = scale; }
	
}
