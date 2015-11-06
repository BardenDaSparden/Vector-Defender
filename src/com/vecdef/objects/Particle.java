package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.ai.Behavior;
import com.vecdef.gamestate.Scene;
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
	
	ArrayList<Behavior> behaviors;
	
	public Particle(float x, float y, Vector4f color, Scene scene){
		super(scene);
		transform.setTranslation(new Vector2f(x, y));
		transform.setOrientation(FastMath.random() * 360f);
		
		mesh = new Mesh();
		LinePrimitive l = new LinePrimitive();
		l.addVertex(vertices[0], color);
		l.addVertex(vertices[1], color);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(l);
		
		mesh.addLayer(layer);
		
		behaviors = new ArrayList<Behavior>();
	}
	
	public Particle(Vector2f start, Vector2f end, Vector4f color, Scene scene){
		super(scene);
		transform.setTranslation(start.add(end.sub(start).scale(0.5f)));
		
		mesh = new Mesh();
		LinePrimitive l = new LinePrimitive();
		l.addVertex(start, color);
		l.addVertex(end, color);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(l);
		
		mesh.addLayer(layer);
	}

	public void update() {
		
		for(Behavior b : behaviors){
			b.update();
		}
		
		opacity = (maxLife - currentLife) / (float)maxLife;
		currentLife++;
		
		transform.setOrientation(velocity.direction());
		
		velocity.set(velocity.x * 0.965f, velocity.y * 0.965f);
		
		if(currentLife >= maxLife)
			expire();
	}
	
	public void destroy(){
		for(Behavior b : behaviors){
			b.destroy();
		}
	}
	
	@Override
	public int getEntityType(){
		return Masks.Entities.PARTICLE;
	}
	
	public Mesh getMesh(){
		return mesh;
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
	
}
