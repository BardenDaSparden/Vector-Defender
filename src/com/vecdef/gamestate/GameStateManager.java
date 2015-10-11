package com.vecdef.gamestate;

public class GameStateManager {

	public static final int MENU_STATE = 0;
	public static final int PLAY_STATE = 1;
	
	private GameState currentState;
	
	public GameStateManager(){
		currentState = null;
	}
	
	public void update(){
		if(currentState == null) return;
		currentState.update();
	}
	
	public void draw(Renderer renderer){
		if(currentState == null) return;
		currentState.draw(renderer);
	}
	
	public void setState(int state){
		
		if(currentState != null)
			currentState.destroy();
		
		if(state == MENU_STATE)
			currentState = new MenuState(this);
		
		if(state == PLAY_STATE)
			currentState = new PlayState(this);
		
		if(currentState != null)
			currentState.initialize();
		
	}
	
	public void dispose(){
		if(currentState != null)
			currentState.destroy();
	}
	
}
