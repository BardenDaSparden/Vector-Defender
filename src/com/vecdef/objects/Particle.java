package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.ai.Behaviour;
import com.vecdef.model.Model;
import com.vecdef.util.Masks;

public class Particle extends Entity{
	
	static float PARTICLE_WIDTH = 18;
	static Vector2f[] vertices = {new Vector2f(-PARTICLE_WIDTH / 2f, 0), new Vector2f(PARTICLE_WIDTH / 2f, 0)};
	
	protected Vector4f color;
	
	protected int currentLife = 0;
	protected int maxLife = 60;
	
	ArrayList<Behaviour> behaviors;
	
	public Particle(float x, float y, Vector4f color, Scene scene){
		super(scene);
		transform.setTranslation(new Vector2f(x, y));
		transform.setOrientation(FastMath.random() * 360f);
		
		model = new Model();
		model.add(vertices[0], color);
		model.add(vertices[1], color);
		
		behaviors = new ArrayList<Behaviour>();
	}
	
	public Particle(Vector2f start, Vector2f end, Vector4f color, Scene scene){
		super(scene);
		transform.setTranslation(start.add(end.sub(start).scale(0.5f)));
		transform.getScale().set(1, 2);
		
		model = new Model();
		model.add(start, color);
		model.add(end, color);
	}

	public void update() {
		
		for(Behaviour b : behaviors){
			b.update();
		}
		
		opacity = (maxLife - currentLife) / (float)maxLife;
		currentLife++;
		
		transform.setOrientation(velocity.direction());
		
		velocity.set(velocity.x * 0.945f, velocity.y * 0.945f);
		
		if(currentLife >= maxLife)
			expire();
	}
	
	public void destroy(){
		for(Behaviour b : behaviors){
			b.destroy();
		}
	}
	
	@Override
	public int getEntityType(){
		return Masks.Entities.PARTICLE;
	}
	
	@Override
	public Model getModel(){
		return model;
	}
	
	public void addBehavior(Behaviour b){
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
