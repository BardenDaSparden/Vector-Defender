package com.vecdef.gamestate;

import org.javatroid.core.Resources;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.Color;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.text.BitmapFont;
import org.lwjgl.opengl.Display;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.core.ApplicationLauncher;

public class MenuState extends GState{

	public MenuState(GameState gamestate) {
		super(gamestate);
	}

	Renderer renderer;
	OrthogonalCamera camera;	
	BitmapFont font;
	String[] menuItems = {"Start Endless Mode", "Exit"};
	Color itemColor;
	int menuItemIdx = 0;
	
	@Override
	public void initialize() {
		renderer = new Renderer();
		camera = new OrthogonalCamera((int)Display.getWidth(), (int)Display.getHeight());
		renderer.setCamera(camera);
		font = Resources.getFont("imagine12");
		itemColor = new Color(0.5f, 0.5f, 0.5f, 1);
	}

	@Override
	public void update() {
		if(gamestate.gamepad.isButtonPressed(Gamepad.DPAD_DOWN_BUTTON)){
			menuItemIdx = (menuItemIdx + 1) % menuItems.length;
		}
		
		if(gamestate.gamepad.isButtonPressed(Gamepad.DPAD_UP_BUTTON)){
			if(menuItemIdx == 0)
				menuItemIdx = menuItems.length - 1;
			else
				menuItemIdx--;
		}
		
		if(gamestate.gamepad.isButtonPressed(Gamepad.A_BUTTON) || gamestate.gamepad.isButtonPressed(Gamepad.START_BUTTON)){
			if(menuItemIdx == 0){
				gamestate.next();
			} else if(menuItemIdx == 1){
				ApplicationLauncher.exit();
			}
		}
	}

	@Override
	public void draw() {
		SpriteBatch batch = renderer.SpriteBatch();
		batch.begin(BlendState.ALPHA);
			int n = menuItems.length;
			int y = 15;
			for(int i = 0; i < n; i++){
				if(menuItemIdx == i)
					itemColor.set(1, 1, 1, 1);
				else
					itemColor.set(0.5f, 0.5f, 0.5f, 1);
				String item = menuItems[i];
				batch.setColor(itemColor);
				font.drawStringCentered(0, y, item, batch);
				y -= 30;
			}
			
		batch.end();
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void destroy() {
		
	}
}
