package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class SplitterModel extends Model {
	
	public static final int SIZE = 16;
	final Vector4f BASE_COLOR = new Vector4f(1, 0.15f, 0, 1);
	
	private SplitterModel(){
		add(new Vector2f(-SIZE, -SIZE), BASE_COLOR);
		add(new Vector2f(SIZE, -SIZE), BASE_COLOR);
		
		add(new Vector2f(SIZE, -SIZE), BASE_COLOR);
		add(new Vector2f(SIZE, SIZE), BASE_COLOR);
		
		add(new Vector2f(SIZE, SIZE), BASE_COLOR);
		add(new Vector2f(-SIZE, SIZE), BASE_COLOR);
		
		add(new Vector2f(-SIZE, SIZE), BASE_COLOR);
		add(new Vector2f(-SIZE, -SIZE), BASE_COLOR);
		
		add(new Vector2f(0, 0), BASE_COLOR);
		add(new Vector2f(SIZE, 0), BASE_COLOR);
	}
	
	static SplitterModel instance = null;
	
	public static SplitterModel get(){
		if(instance == null)
			instance = new SplitterModel();
		return instance;
	}
	
}
