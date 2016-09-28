package com.vecdef.model;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class ChaserModel extends Model {

	final float RADIUS = 7;
	final int SEGMENTS = 12;
	final Vector4f BASE_COLOR = new Vector4f(0.5f, 1, 1, 1);
	
	private ChaserModel(){
		for(int i = 0; i < SEGMENTS; i++){
	    	float a1 = (float)i / (float) SEGMENTS * (float)Math.PI * 2.0f;
	    	float a2 = (float)((i + 1) % SEGMENTS) / (float)SEGMENTS * (float)Math.PI * 2.0f;
	    	Vector2f v0 = new Vector2f(FastMath.cos(a1) * RADIUS, FastMath.sin(a1) * RADIUS);
	    	Vector2f v1 = new Vector2f(FastMath.cos(a2) * RADIUS, FastMath.sin(a2) * RADIUS);
	    	add(v0, BASE_COLOR);
	    	add(v1, BASE_COLOR);
	    }
	}
	
	static ChaserModel instance = null;
	
	public static ChaserModel get(){
		if(instance == null)
			instance = new ChaserModel();
		return instance;
	}
	
}
