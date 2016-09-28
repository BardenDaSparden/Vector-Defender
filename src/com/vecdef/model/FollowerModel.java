package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class FollowerModel extends Model {

	final float WIDTH = 24;
	final float HEIGHT = 24;
	final float CAP_SIZE = 6;
	final Vector4f BASE_COLOR = new Vector4f(1, 0, 1, 1);
	
	private FollowerModel(){
		add(new Vector2f(0, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, 0), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, 0), BASE_COLOR);
		add(new Vector2f(0, -HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(0, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, 0), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, 0), BASE_COLOR);
		add(new Vector2f(0, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f + CAP_SIZE, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f + CAP_SIZE, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f - CAP_SIZE, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f - CAP_SIZE, HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f + CAP_SIZE, -HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f + CAP_SIZE, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), BASE_COLOR);
		
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), BASE_COLOR);
		add(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f - CAP_SIZE, -HEIGHT / 2f), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f - CAP_SIZE, -HEIGHT / 2f), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), BASE_COLOR);
		
		add(new Vector2f(WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), BASE_COLOR);
		add(new Vector2f(WIDTH / 2f, -HEIGHT / 2f), BASE_COLOR);
	}
	
	static FollowerModel instance = null;
	
	public static FollowerModel get(){
		if(instance == null)
			instance = new FollowerModel();
		return instance;
	}
	
}
