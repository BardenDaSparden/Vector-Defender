package com.vecdef.rendering;

import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;

public class Renderer {

	private static final int MAX_DRAWS = 10000;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	
	public Renderer(int width, int height){
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch(width, height, MAX_DRAWS);
	}
	
	public void setCamera(OrthogonalCamera camera){
		shapeRenderer.setCamera(camera);
		spriteBatch.setCamera(camera);
	}
	
	public OrthogonalCamera getCamera(){
		return spriteBatch.getCamera();
	}
	
	public SpriteBatch SpriteBatch(){
		return spriteBatch;
	}
	
	public ShapeRenderer ShapeRenderer(){
		return shapeRenderer;
	}
	
}
