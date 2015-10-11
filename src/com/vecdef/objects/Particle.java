package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.ai.Behavior;
import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;

public class Particle extends Entity{
	
	static float PARTICLE_WIDTH = 18;
	static Vector2f[] vertices = {new Vector2f(-PARTICLE_WIDTH / 2f, 0), new Vector2f(PARTICLE_WIDTH / 2f, 0)};
	
	protected Mesh mesh;
	protected Vector4f color;
	
	protected int currentLife = 0;
	protected int maxLife = 60;
	protected float opacity = 1;
	
	ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
	
	public Particle(float x, float y){
		this(x, y, new Vector4f(1, 1, 1, 1));
	}
	
	public Particle(float x, float y, Vector4f color){
		transform.setTranslation(new Vector2f(x, y));
		transform.setOrientation(FastMath.random() * 360f);
		
		mesh = new Mesh();
		LinePrimitive l = new LinePrimitive();
		l.addVertex(vertices[0], color);
		l.addVertex(vertices[1], color);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(l);
		
		mesh.addLayer(layer);
	}
	
	public Particle(Vector2f start, Vector2f end, Vector4f color){
		transform.setTranslation(start.add(end.sub(start).scale(0.5f)));
		
		mesh = new Mesh();
		LinePrimitive l = new LinePrimitive();
		l.addVertex(start, color);
		l.addVertex(end, color);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(l);
		
		mesh.addLayer(layer);
	}

	public void onUpdate(Grid grid, float dt) {
		
		for(Behavior b : behaviors){
			b.onUpdate(this, grid, dt);
		}
		
		if(currentLife == maxLife){
			bExpired = true;
		}
		
		opacity = (maxLife - currentLife) / (float)maxLife;
		
		currentLife++;
	}

	public void onCollision(Entity other) {

	}
	
	public void onDestroy(){
		
	}
	
	public Mesh getMesh(){
		return mesh;
	}
	
	public float getOpacity(){
		return opacity;
	}
	
	public void addBehavior(Behavior b){
		behaviors.add(b);
	}
	
	public int getCurrentLife(){
		return currentLife;
	}
	
	public void setCurrentLife(int l){
		this.currentLife = l;
	}
	
	public int getMaxLife(){
		return maxLife;
	}
	public void setMaxLife(int ml){
		this.maxLife = ml;
	}
	
}
