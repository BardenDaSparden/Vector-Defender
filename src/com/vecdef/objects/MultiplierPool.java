package com.vecdef.objects;

public class MultiplierPool extends ObjectPool {

	static final int NUM_MULTIPLIERS = 250;
	Scene scene;
	
	public MultiplierPool(Scene scene){
		super(NUM_MULTIPLIERS);
		this.scene = scene;
		for(int i = 0; i < NUM_MULTIPLIERS; i++){
			MultiplierPiece piece = new MultiplierPiece(scene);
			piece.recycle();
			objects.add(piece);
			scene.add(piece);
		}
	}
	
}
