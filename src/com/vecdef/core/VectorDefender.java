package com.vecdef.core;

import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import com.vecdef.gamestate.PlayState;

public class VectorDefender implements Application {
	
	PlayState playState;
	
	@Override
	public void initialize() {
		Resources.loadFont("fonts/imagine_16.fnt", "imagine12");
		Resources.loadFont("fonts/imagine_16.fnt", "imagine16");
		Resources.loadFont("fonts/Imagine.fnt", "imagine18");
		Resources.loadFont("fonts/metroidPrime32.fnt", "TitleFont");
		Resources.loadTexture("textures/Player.png", "player");
		Resources.loadTexture("textures/bomb.png", "bomb");
		Resources.loadTexture("textures/white.png", "blank");
		//Input.setMouseGrabbed(true);
		
		playState = new PlayState();
		playState.initialize();
	}

	@Override
	public void update() {
		playState.update();
		Input.update();
	}
	
	@Override
	public void render() {
		playState.draw();
	}

	@Override
	public void destroy() {
		
	}
}