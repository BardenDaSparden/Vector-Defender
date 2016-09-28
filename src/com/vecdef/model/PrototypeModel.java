package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;


public class PrototypeModel extends Model {

	final float WIDTH = 30;
	final float HEIGHT = 24;
	final Vector4f BASE_COLOR = new Vector4f(1, 0.75f, 0, 1);
	
	private PrototypeModel(){
		add(new Vector2f(-WIDTH / 2f + 5, 2), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, 2), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, 2), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f + 5, 2), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f + 5, -2), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, -2), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, -2), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f + 5, -2), BASE_COLOR);
	}
	
	static PrototypeModel instance = null;
	
	public static PrototypeModel get(){
		if(instance == null)
			instance = new PrototypeModel();
		return instance;
	}
	
}
