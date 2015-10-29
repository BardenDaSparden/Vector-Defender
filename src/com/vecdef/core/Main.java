package com.vecdef.core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {

	public static void main(String[] args){
		Application application = new VectorDefender();
		ApplicationSettings settings = new ApplicationSettings();
		ApplicationLauncher launcher = new ApplicationLauncher(application, settings);
		
		DisplayMode desktopDisplay = Display.getDesktopDisplayMode();
		
		settings.width = desktopDisplay.getWidth();
		settings.height = desktopDisplay.getHeight();
		settings.fullscreen = true;
		settings.borderless = false;
		settings.vsync = false;
		settings.renderrate = 60;
		settings.updaterate = 60;
		settings.multisamples = 4;
			
		launcher.launch();		
	}
	
}
