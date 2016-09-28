package com.vecdef.gamestate;

public class GameStates {

	public static GameState NONE;
	public static GameState SPLASH;
	public static GameState MAIN_MENU;
	public static GameState PLAYING;
	
	public static void initStates(VectorDefender instance){
		NONE = new BlankGameState(instance);
		SPLASH = new SplashState(instance);
		MAIN_MENU = new MainMenuState(instance);
		PLAYING = new PlayingState(instance);
	}
	
}
