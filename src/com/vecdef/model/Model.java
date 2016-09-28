package com.vecdef.model;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class Model {

	ArrayList<Vector2f> vertices;
	ArrayList<Vector4f> colors;
	
	public Model(){
		vertices = new ArrayList<Vector2f>();
		colors = new ArrayList<Vector4f>();
	}
	
	public void add(Vector2f vertex, Vector4f color){
		vertices.add(vertex);
		colors.add(color);
	}
	
	public ArrayList<Vector2f> getVertices(){
		return vertices;
	}
	
	public ArrayList<Vector4f> getColors(){
		return colors;
	}
	
	public int numVertices(){
		return vertices.size();
	}
	
}
