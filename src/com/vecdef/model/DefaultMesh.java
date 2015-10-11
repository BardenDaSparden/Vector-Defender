package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class DefaultMesh extends Mesh{

	public DefaultMesh(){
		LinePrimitive p = new LinePrimitive();
		p.addVertex(new Vector2f(-5, -5), new Vector4f(1, 0, 1, 1));
		p.addVertex(new Vector2f(-5, 5), new Vector4f(1, 0, 1, 1));
		p.addVertex(new Vector2f(-5, 5), new Vector4f(1, 0, 1, 1));
		p.addVertex(new Vector2f(5, 0), new Vector4f(1, 1, 1, 1));
		p.addVertex(new Vector2f(5, 0), new Vector4f(1, 1, 1, 1));
		p.addVertex(new Vector2f(-5, -5), new Vector4f(1, 0, 1, 1));
		
		
		MeshLayer layer1 = new MeshLayer();
		layer1.addPrimitive(p);
		
		addLayer(layer1);
	}
	
}
