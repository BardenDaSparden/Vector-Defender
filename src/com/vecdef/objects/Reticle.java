package com.vecdef.objects;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.gamestate.Scene;
import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;

public class Reticle extends Entity{

	static Vector2f[] vertices = {
		new Vector2f(-10, 0), new Vector2f(10, 0),
		new Vector2f(0, -10), new Vector2f(0, 10)
	};
	
	Mesh mesh;
	
	public Reticle(Scene scene){
		super(scene);
		mesh = new Mesh();
		LinePrimitive p = new LinePrimitive();
		
		Vector4f color = new Vector4f(0, 1, 0, 1);
		p.addVertex(vertices[0], color);
		p.addVertex(vertices[1], color);
		p.addVertex(vertices[2], color);
		p.addVertex(vertices[3], color);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(p);
		
		mesh.addLayer(layer);
	}
	
	public void update(){}
	public void destroy(){}
	public int getEntityType(){
		return Masks.Entities.OTHER;
	}

	@Override
	public int getRadius() {
		return 0;
	}

	@Override
	public int getGroupMask() {
		return Masks.NONE;
	}

	@Override
	public int getCollisionMask() {
		return Masks.NONE;
	}
	
	public Mesh getMesh(){
		return mesh;
	}
	
}
