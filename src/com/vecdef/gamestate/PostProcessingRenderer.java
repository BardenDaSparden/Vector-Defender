package com.vecdef.gamestate;

import org.javatroid.graphics.FrameBuffer;
import org.javatroid.graphics.SpriteBatch;
import org.lwjgl.opengl.Display;

public class PostProcessingRenderer {

	Scene scene;
	int screenWidth;
	int screenHeight;
	FrameBuffer back;
	
	public PostProcessingRenderer(Scene scene){
		this.scene = scene;
		screenWidth = Display.getWidth();
		screenHeight = Display.getHeight();
		back = new FrameBuffer(screenWidth, screenHeight);
	}
	
	public void render(Renderer renderer){
		back.bind(); {
			back.clear(0, 0, 0, 1);
			scene.draw(renderer);
		} 
		back.release();
		
		SpriteBatch batch = renderer.SpriteBatch();
		
		batch.begin();{
			batch.draw(0, 0, back.getWidth(), back.getHeight(), 0, back.getTexture());
		}
		batch.end();
		
	}
	
}
