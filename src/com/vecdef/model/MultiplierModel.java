package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class MultiplierModel extends Model {

	final float WIDTH = 6;
	final float HEIGHT = 6;
	final Vector4f BASE_COLOR = new Vector4f(0.2f, 1, 0.5f, 1);
	
	private MultiplierModel(){
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f + 1, HEIGHT / 2f - 1), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f + 1, HEIGHT / 2f - 1), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f - 1, -HEIGHT / 2f + 1), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f - 1, -HEIGHT / 2f + 1), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
	}
	
	static MultiplierModel instance = null;
	
	public static MultiplierModel get(){
		if(instance == null)
			instance = new MultiplierModel();
		return instance;
	}
	
}
