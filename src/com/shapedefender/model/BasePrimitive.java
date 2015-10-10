package com.shapedefender.model;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public abstract class BasePrimitive implements Primitive{

	protected ArrayList<Vector2f> vertices;
	protected ArrayList<Vector4f> colors;
	
	public BasePrimitive(){
		this.vertices = new ArrayList<Vector2f>();
		this.colors = new ArrayList<Vector4f>();
	}
	
	public void addVertex(Vector2f position){
		addVertex(position, new Vector4f(1, 1, 1, 1));
	}
	
	public void addVertex(Vector2f position, Vector4f color){
		vertices.add(position);
		colors.add(color);
	}
	
	public ArrayList<Vector2f> getVertices(){
		return vertices;
	}
	
	public ArrayList<Vector4f> getColors(){
		return colors;
	}
	
	public abstract DrawType getDrawType();
	
}
