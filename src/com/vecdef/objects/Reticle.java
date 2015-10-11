package com.shapedefender.objects;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.shapedefender.model.LinePrimitive;
import com.shapedefender.model.Mesh;
import com.shapedefender.model.MeshLayer;

public class Reticle extends Entity{

	static Vector2f[] vertices = {
		new Vector2f(-10, 0), new Vector2f(10, 0),
		new Vector2f(0, -10), new Vector2f(0, 10)
	};
	
	private Mesh mesh;
	
	public Reticle(Vector4f color){
		mesh = new Mesh();
		LinePrimitive p = new LinePrimitive();
		
		p.addVertex(vertices[0], color);
		p.addVertex(vertices[1], color);
		p.addVertex(vertices[2], color);
		p.addVertex(vertices[3], color);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(p);
		
		mesh.addLayer(layer);
	}
	
	public void onUpdate(Grid grid, float dt) {
		
	}

	@Override
	public void onCollision(Entity other) {
		// TODO Auto-generated method stub
		
	}
	
	public void onDestroy(){
		
	}

	public void setPosition(Vector2f position){
		transform.setTranslation(position);
	}
	
	public Mesh getMesh(){
		return mesh;
	}
	
}
