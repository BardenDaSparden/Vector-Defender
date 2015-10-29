package com.vecdef.core;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.gamestate.GameState;
import com.vecdef.gamestate.MenuState;
import com.vecdef.gamestate.PlayState;

public class VectorDefender implements Application {
	
	Input input;
	Gamepad gamepad;
	GameState gamestate;
	
	@Override
	public void initialize() {
		Resources.loadFont("fonts/imagine_16.fnt", "imagine12");
		Resources.loadFont("fonts/imagine_16.fnt", "imagine16");
		Resources.loadFont("fonts/Imagine.fnt", "imagine18");
		Resources.loadTexture("textures/Player.png", "player");
		Resources.loadTexture("textures/bomb.png", "bomb");
		Resources.loadTexture("textures/white.png", "blank");
		Resources.loadTexture("textures/menuTitle.png", "title");
		
		input = new Input();
		input.setMouseGrabbed(true);
		
		gamepad = new Gamepad();
		
		gamestate = new GameState(input, gamepad);
		gamestate.registerGState(new MenuState(gamestate));
		gamestate.registerGState(new PlayState(gamestate));
		gamestate.initialize();
	}

	@Override
	public void update() {
		gamestate.update();
		input.poll();
		gamepad.poll();
	}
	
	@Override
	public void render() {
		gamestate.draw();
	}

	@Override
	public void destroy() {
		gamestate.destroy();
	}
}