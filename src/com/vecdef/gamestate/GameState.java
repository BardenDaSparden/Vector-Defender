package com.vecdef.gamestate;

public abstract class GameState {

	protected GameStateManager gameStateManager;
	
	public GameState(GameStateManager gsm){
		this.gameStateManager = gsm;
	}
	
	public abstract void initialize();
	public abstract void update();
	public abstract void draw(Renderer renderer);
	public abstract void destroy();
	
}
