package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class ZoomerModel extends Model {

	final int WIDTH = 28;
	final int HEIGHT = 14;
	final float SPACE = 2.5f;
	final Vector4f BASE_COLOR = new Vector4f(0, 1.0f, 0.0f, 1.0f);
	
	private ZoomerModel(){
//		add(new Vector2f(-WIDTH / 2.0f, 0), BASE_COLOR);
//		add(new Vector2f(WIDTH / 2.0f, 0), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2.0f, -SPACE), BASE_COLOR);
		add(new Vector2f(WIDTH / 2.0f, -SPACE), BASE_COLOR);
		
		//Bottom Wing
		add(new Vector2f(-WIDTH / 2.0f, -SPACE), BASE_COLOR);
		add(new Vector2f(WIDTH / 2.0f, -SPACE), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2.0f, -SPACE), BASE_COLOR);
		add(new Vector2f(0, -HEIGHT / 2.0f), BASE_COLOR);
		
		add(new Vector2f(0, -HEIGHT / 2.0f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2.0f, -HEIGHT / 2.0f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2.0f, -HEIGHT / 2.0f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2.0f, -SPACE), BASE_COLOR);
		
		//Top Wing
		add(new Vector2f(-WIDTH / 2.0f, SPACE), BASE_COLOR);
		add(new Vector2f(WIDTH / 2.0f, SPACE), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2.0f, SPACE), BASE_COLOR);
		add(new Vector2f(0, HEIGHT / 2.0f), BASE_COLOR);
		
		add(new Vector2f(0, HEIGHT / 2.0f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2.0f, HEIGHT / 2.0f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2.0f, HEIGHT / 2.0f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2.0f, SPACE), BASE_COLOR);
	}
	
	static ZoomerModel instance = null;
	
	public static ZoomerModel get(){
		if(instance == null)
			instance = new ZoomerModel();
		return instance;
	}
	
}
