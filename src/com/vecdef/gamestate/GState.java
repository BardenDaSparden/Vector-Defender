package com.vecdef.gamestate;

public abstract class GState {

	protected GameState gamestate;
	
	protected GState(GameState gamestate){
		this.gamestate = gamestate;
	}
	
	public abstract void initialize();
	public abstract void update();
	public abstract void draw();
	public abstract void destroy();
	
}
