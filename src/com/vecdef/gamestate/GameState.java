package com.vecdef.gamestate;

public abstract class GameState {
	
	VectorDefender instance;
	
	public GameState(VectorDefender instance){
		this.instance = instance;
	}
	
	//Load resources
	public abstract void initialize();
	
	//Logicial update call
	public abstract void update();
	
	//Draw current state
	public abstract void render();
	
	//Clear up resources
	public abstract void cleanUp();
	
}
