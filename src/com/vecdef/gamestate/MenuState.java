package com.vecdef.gamestate;

import org.javatroid.core.Input;
import org.lwjgl.input.Keyboard;

public class MenuState extends GState{

	public MenuState(GameState gamestate) {
		super(gamestate);
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void update() {
		if(Input.isKeyDown(Keyboard.KEY_SPACE)){
			gamestate.next();
		}
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void destroy() {
		
	}
}
