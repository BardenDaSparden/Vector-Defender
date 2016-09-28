package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class WandererModel extends Model {

	final float WIDTH = 32;
	final float HEIGHT = 32;
	final Vector4f BASE_COLOR = new Vector4f(0.35f, 0.25f, 1, 1);
	
	private WandererModel(){
		add(new Vector2f(0, 0), BASE_COLOR);
		add(new Vector2f(0, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(0, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 4f, HEIGHT / 4f), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 4f, HEIGHT / 4f), BASE_COLOR);
		add(new Vector2f(0, 0), BASE_COLOR);
		
		add(new Vector2f(0, 0), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, 0), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, 0), BASE_COLOR);
		add(new Vector2f(WIDTH / 4f, -HEIGHT / 4f), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 4f, -HEIGHT / 4f), BASE_COLOR);
		add(new Vector2f(0, 0), BASE_COLOR);
		
		add(new Vector2f(0, 0), BASE_COLOR);
		add(new Vector2f(0, -HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(0, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 4f, -HEIGHT / 4f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 4f, -HEIGHT / 4f), BASE_COLOR);
		add(new Vector2f(0, 0), BASE_COLOR);
		
		add(new Vector2f(0, 0), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, 0), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, 0), BASE_COLOR);
		add(new Vector2f(-WIDTH / 4f, HEIGHT / 4f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 4f, HEIGHT / 4f), BASE_COLOR);
		add(new Vector2f(0, 0), BASE_COLOR);
	}
	
	static WandererModel instance = null;
	
	public static WandererModel get(){
		if(instance == null)
			instance = new WandererModel();
		return instance;
	}
	
}
