package com.vecdef.objects;

public class SpawnEffectPool extends ObjectPool {

	static final int NUM_EFFECTS = 100;
	Scene scene;
	
	public SpawnEffectPool(Scene scene){
		super(NUM_EFFECTS);
		this.scene = scene;
		for(int i = 0; i < NUM_EFFECTS; i++){
			SpawnEffect effect = new SpawnEffect(scene);
			effect.recycle();
			objects.add(effect);
			scene.add(effect);
		}
	}
	
}
