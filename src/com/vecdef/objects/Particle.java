package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.Vector3f;

import com.vecdef.ai.Behaviour;
import com.vecdef.model.Model;
import com.vecdef.model.ParticleModel;
import com.vecdef.util.Masks;

public class Particle extends Entity{
	
	protected int currentLife = 0;
	protected int maxLife = 60;
	
	ArrayList<Behaviour> behaviors;
	
	Vector3f HSB = new Vector3f(0, 1, 0.5f);
	Vector3f RGB = new Vector3f(1, 0, 0);
	float[] hsbTemp = new float[3];
	float time = 0;
	
	public Particle(Scene scene){
		super(scene);
		transform.getTranslation().set(0, 0);
		model = ParticleModel.get();
		behaviors = new ArrayList<Behaviour>();
		radius = 0;
	}

	public void update() {
		if(!isVisible)
			return;
		
		for(Behaviour b : behaviors){
			b.update();
		}
		
		opacity = (maxLife - currentLife) / (float)maxLife;
		currentLife++;
		
		transform.setOrientation(velocity.direction());
		velocity.set(velocity.x * 0.95f, velocity.y * 0.95f);
		
		java.awt.Color.RGBtoHSB((int)(overrideColor.x * 255), (int)(overrideColor.y * 255), (int)(overrideColor.z * 255), hsbTemp);
		hsbTemp[0] = (hsbTemp[0] + 1/360.0f);
		java.awt.Color c = java.awt.Color.getHSBColor(hsbTemp[0], hsbTemp[1], hsbTemp[2]);
		overrideColor.x = c.getRed() / 255.0f;
		overrideColor.y = c.getGreen() / 255.0f;
		overrideColor.z = c.getBlue() / 255.0f;
		
		if(currentLife >= maxLife)
			destroy();
	}
	
	public void destroy(){
		for(Behaviour b : behaviors){
			b.destroy();
		}
		
		if(!super.isRecycled())
			scene.getParticlePool().recycle(this);
	}
	
	@Override
	public void reuse(){
		super.reuse();
		isVisible = true;
		currentLife = 0;
	}
	
	@Override
	public void recycle(){
		super.recycle();
		behaviors.clear();
		isVisible = false;
		currentLife = maxLife;
	}
	
	@Override
	public int getEntityType(){
		return Masks.Entities.PARTICLE;
	}
	
	@Override
	public Model getModel(){
		return model;
	}
	
	@Override
	public boolean useOverrideColor(){
		return true;
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
