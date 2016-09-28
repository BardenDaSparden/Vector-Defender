package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class BulletModel extends Model {
	
	private BulletModel(){
		add(new Vector2f(-6, -2), new Vector4f(1, 1, 1, 1));
		add(new Vector2f(-6, 2), new Vector4f(1, 1, 1, 1));
		
		add(new Vector2f(-6, 2), new Vector4f(1, 1, 1, 1));
		add(new Vector2f(6, 0), new Vector4f(1, 1, 1, 1));
		
		add(new Vector2f(6, 0), new Vector4f(1, 1, 1, 1));
		add(new Vector2f(-6, -2), new Vector4f(1, 1, 1, 1));
	}
	
	static BulletModel instance = null;
	
	public static BulletModel get(){
		if(instance == null)
			instance = new BulletModel();
		return instance;
	}
	
}
