package com.vecdef.gamestate;

import org.javatroid.graphics.SpriteBatch;

public class Renderer {

	private static final int MAX_DRAWS = 10000;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	
	public Renderer(){
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch(MAX_DRAWS);
	}
	
	public SpriteBatch SpriteBatch(){
		return spriteBatch;
	}
	
	public ShapeRenderer ShapeRenderer(){
		return shapeRenderer;
	}
	
}
