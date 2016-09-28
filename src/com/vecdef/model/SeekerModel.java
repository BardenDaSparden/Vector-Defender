package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;


public class SeekerModel extends Model {

	final float WIDTH = 32;
	final float HEIGHT = 32;
	final Vector4f BASE_COLOR = new Vector4f(0, 1, 1, 1);
	
	private SeekerModel(){
		add(new Vector2f(-WIDTH / 2f, 0), BASE_COLOR);
		add(new Vector2f(0, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(0, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, 0), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, 0), BASE_COLOR);
		add(new Vector2f(0, -HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(0, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, 0), BASE_COLOR);
	}
	
	static SeekerModel instance = null;
	
	public static SeekerModel get(){
		if(instance == null)
			instance = new SeekerModel();
		return instance;
	}
	
}
