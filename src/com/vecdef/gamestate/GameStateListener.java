package com.vecdef.gamestate;

public interface GameStateListener {

	public void onStateChange(GameState oldState, GameState newState);
	
}
