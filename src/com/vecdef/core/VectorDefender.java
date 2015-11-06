package com.vecdef.core;

import org.javatroid.core.Resources;
import com.vecdef.gamestate.GameState;
import com.vecdef.gamestate.MenuState;
import com.vecdef.gamestate.PlayState;

public class VectorDefender implements Application {
	
	GameState gamestate;
	
	@Override
	public void initialize() {
		Resources.loadFont("fonts/imagine_16.fnt", "imagine12");
		Resources.loadFont("fonts/imagine_16.fnt", "imagine16");
		Resources.loadFont("fonts/Imagine.fnt", "imagine18");
		Resources.loadFont("fonts/tech18.fnt", "tech18");
		Resources.loadFont("fonts/tech30.fnt", "tech30");
		Resources.loadFont("fonts/tech36.fnt", "tech36");
		
		Resources.loadTexture("textures/white.png", "blank");
		Resources.loadTexture("textures/menuTitle.png", "title");
		Resources.loadTexture("textures/energyBarOutline.png", "energyBar");
		Resources.loadTexture("textures/livesBarOutline.png", "livesBar");
		Resources.loadTexture("textures/life.png", "life");
		
		gamestate = new GameState();
		gamestate.registerGState(new MenuState(gamestate));
		gamestate.registerGState(new PlayState(gamestate));
		gamestate.initialize();
	}

	@Override
	public void update() {
		gamestate.update();
	}
	
	@Override
	public void render(){
		gamestate.draw();
	}

	@Override
	public void destroy() {
		gamestate.destroy();
	}
}