package com.shapedefender;

import org.javatroid.core.Application;
import org.javatroid.core.BaseGame;
import org.javatroid.core.Input;
import org.javatroid.core.Resources;
import org.javatroid.core.Window;

public class Game implements BaseGame {
	
	public static void main(String[] args){
		Application application = new Application(60, new Game());
		application.start();
	}
	
	Renderer renderer;
	GameStateManager gameStateManager;
	
	public void setupWindow(){
		Window.useDesktopDisplayMode(true);
	    Window.setFullscreen(true);
	    Window.setVsync(true);
	}
	
	private void loadResources(){
		Resources.loadFont("fonts/imagine_16.fnt", "imagine12");
		Resources.loadFont("fonts/imagine_16.fnt", "imagine16");
		Resources.loadFont("fonts/Imagine.fnt", "imagine18");
		Resources.loadFont("fonts/metroidPrime32.fnt", "TitleFont");
		
		Resources.loadTexture("textures/Player.png", "player");
		Resources.loadTexture("textures/bomb.png", "bomb");
		Resources.loadTexture("textures/white.png", "blank");
	}
	
	public void create(){
		Input.setMouseGrabbed(true);
		loadResources();
		renderer = new Renderer();
		gameStateManager = new GameStateManager();
		gameStateManager.setState(GameStateManager.MENU_STATE);
	}
	
	public void update(){
		gameStateManager.update();
	}
	
	public void draw(float inter){
		gameStateManager.draw(renderer, inter);
	}
	
	public void dispose(){
		gameStateManager.dispose();
	}
	
}