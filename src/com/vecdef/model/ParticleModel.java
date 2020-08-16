package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class ParticleModel extends Model {

	final float PARTICLE_WIDTH = 16;
	
	private ParticleModel(){
		add(new Vector2f(-PARTICLE_WIDTH / 2f, 0), new Vector4f(1, 1, 1, 1));
		add(new Vector2f(PARTICLE_WIDTH / 2f, 0), new Vector4f(1, 1, 1, 1));
	}
	
	static ParticleModel instance = null;
	
	public static ParticleModel get(){
		if(instance == null)
			instance = new ParticleModel();
		return instance;
	}
	
}
