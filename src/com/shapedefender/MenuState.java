package com.shapedefender;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import org.javatroid.core.Window;
import org.javatroid.graphics.GLUtil;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.text.BitmapFont;

public class MenuState extends GameState{

	public MenuState(GameStateManager gsm){
		super(gsm);
	}

	OrthogonalCamera camera;
	BitmapFont titleFont;
	BitmapFont messageFont;
	
	float time = 0;
	float messageAlpha = 1;
	
	public void initialize() {
		camera = new OrthogonalCamera(Window.getWidth(), Window.getHeight());
		titleFont = Resources.getFont("TitleFont");
		messageFont = Resources.getFont("imagine18");
	}

	public void update() {
		if(Input.isMouseButtonPressed(Input.MOUSE_LEFT)){
			gameStateManager.setState(GameStateManager.PLAY_STATE);
		}
		time++;
		messageAlpha = (float)Math.abs(Math.sin(time * 0.1));
	}

	public void draw(Renderer renderer, float inter) {
		SpriteBatch sb = renderer.SpriteBatch();
		GLUtil.clear(true, false, false, false);
		sb.setCamera(camera);
		sb.begin();
		messageFont.drawStringCentered(0, Window.getHeight() / 2 - 100, "Geometry Wars", sb);
		
		sb.setColor(1, 1, 1, messageAlpha);
		titleFont.drawStringCentered(0, 0, "Click Mouse to begin...", sb);
		sb.setColor(1, 1, 1, 1);
		
		sb.end();
	}

	public void dispose() {
		
	}
}
