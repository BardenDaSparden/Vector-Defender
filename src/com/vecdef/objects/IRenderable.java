package com.shapedefender.objects;

import org.javatroid.math.Vector4f;

import com.shapedefender.model.DefaultMesh;
import com.shapedefender.model.Mesh;
import com.shapedefender.model.Transform2D;

public interface IRenderable {
	
	default public Mesh getMesh(){
		return new DefaultMesh();
	}
	
	default public Transform2D getTransform(){
		return new Transform2D();
	}
	
	default public Vector4f getBaseColor(){
		return new Vector4f(1, 1, 1, 1);
	}
	
	default public float getOpacity(){
		return 1;
	}
	
}
