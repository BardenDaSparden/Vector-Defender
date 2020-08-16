package com.vecdef.objects;

public class BulletPool extends ObjectPool {

	static final int NUM_BULLETS = 350;
	Scene scene;
	
	public BulletPool(Scene scene){
		super(NUM_BULLETS);
		this.scene = scene;
		for(int i = 0; i < NUM_BULLETS; i++){
			Bullet bullet = new Bullet(scene);
			bullet.recycle();
			objects.add(bullet);
			scene.add(bullet);
		}
	}
	
}
