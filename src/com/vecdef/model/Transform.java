package com.vecdef.model;

import org.javatroid.math.Vector2f;

public class Transform {

	private Vector2f translation;
	private float orientation;
	private Vector2f scale;
	
	public Transform(){
		this(new Vector2f(0, 0), 0, new Vector2f(1, 1));
	}
	
	public Transform(Vector2f translation){
		this(translation, 0, new Vector2f(1, 1));
	}
	
	public Transform(Vector2f translation, float orientation){
		this(translation, orientation, new Vector2f(1, 1));
	}
	
	public Transform(Transform transform){
		this.translation = new Vector2f(transform.getTranslation());
		this.orientation = transform.getOrientation();
		this.scale = new Vector2f(transform.getScale());
	}
	
	public void set(Transform t){
		translation.set(t.translation);
		orientation = t.orientation;
		scale.set(t.scale);
	}
	
	public Transform(Vector2f translation, float orientation, Vector2f scale){
		this.translation = translation;
		this.orientation = orientation;
		this.scale = scale;
	}
	
	public void translate(float x, float y){
		this.translation.x += x;
		this.translation.y += y;
	}
	
	public void rotate(float orientation){
		this.orientation += orientation;
	}
	
	public void scale(float sx, float sy){
		this.scale.x += sx;
		this.scale.y += sy;
	}
	
	public void setTranslation(Vector2f translation){
		this.translation = translation;
	}
	
	public void setOrientation(float orientation){
		this.orientation = orientation;
	}
	
	public void setScale(Vector2f scale){
		this.scale = scale;
	}
	
	public Vector2f getTranslation(){
		return translation;
	}
	
	public float getOrientation(){
		return orientation;
	}
	
	public Vector2f getScale(){
		return scale;
	}
	
}
