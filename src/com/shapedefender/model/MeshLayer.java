package com.shapedefender.model;

import java.util.ArrayList;

public class MeshLayer{
	
	protected ArrayList<Primitive> primitives;
	
	public MeshLayer(){
		this.primitives = new ArrayList<Primitive>();
	}
	
	public void addPrimitive(Primitive primitive){
		primitives.add(primitive);
	}
	
	public ArrayList<Primitive> getPrimitives(){
		return primitives;
	}
	
}
