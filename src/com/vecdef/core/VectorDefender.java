package com.vecdef.core;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import com.vecdef.gamestate.GameStateManager;
import com.vecdef.gamestate.Renderer;

public class VectorDefender implements Application {
	
	Renderer renderer;
	GameStateManager gameStateManager;
	
	@Override
	public void initialize() {
		Resources.loadFont("fonts/imagine_16.fnt", "imagine12");
		Resources.loadFont("fonts/imagine_16.fnt", "imagine16");
		Resources.loadFont("fonts/Imagine.fnt", "imagine18");
		Resources.loadFont("fonts/metroidPrime32.fnt", "TitleFont");
		Resources.loadTexture("textures/Player.png", "player");
		Resources.loadTexture("textures/bomb.png", "bomb");
		Resources.loadTexture("textures/white.png", "blank");
		
		Input.setMouseGrabbed(true);
		
		renderer = new Renderer();
		gameStateManager = new GameStateManager();
		gameStateManager.setState(GameStateManager.MENU_STATE);
	}

	@Override
	public void update() {
		gameStateManager.update();
	}
	
	@Override
	public void render() {
		gameStateManager.draw(renderer);
	}

	@Override
	public void destroy() {
		gameStateManager.dispose();
	}
}