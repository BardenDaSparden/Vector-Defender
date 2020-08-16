package com.vecdef.objects;

public class ParticlePool extends ObjectPool {

	static final int NUM_PARTICLES = 2000;
	
	Scene scene;
	
	public ParticlePool(Scene scene){
		super(NUM_PARTICLES);
		this.scene = scene;
		for(int i = 0; i < NUM_PARTICLES; i++){
			Particle particle = new Particle(scene);
			particle.recycle();
			objects.add(particle);
			scene.add(particle);
		}
	}
}
