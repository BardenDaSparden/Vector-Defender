package com.vecdef.objects;

public class EnemyPool extends ObjectPool {

	static final int NUM_ENEMIES = 200;
	Scene scene;
	
	public EnemyPool(Scene scene) {
		super(NUM_ENEMIES);
		this.scene = scene;
		for(int i = 0; i < NUM_ENEMIES; i++){
			Enemy enemy = new Enemy(scene);
			enemy.getTransform().getTranslation().set(-9999, -9999);
			enemy.recycle();
			objects.add(enemy);
			scene.add(enemy);
		}
	}
	
	public void print(){
		System.out.println("Size: " + objects.size());
		System.out.println("Available: " + hasAvailable());
		System.out.println("Next Idx: " + findNextIdx());
	}
	
}
