package com.vecdef.core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {

	public static void main(String[] args){
		Application application = new VectorDefender();
		ApplicationSettings settings = new ApplicationSettings();
		ApplicationLauncher launcher = new ApplicationLauncher(application, settings);
		
		DisplayMode desktopDisplay = Display.getDesktopDisplayMode();
		
		settings.width = 1280;
		settings.height = 720;
		settings.fullscreen = false;
		settings.borderless = false;
		settings.vsync = true;
		settings.updaterate = 60;
		settings.multisamples = 1;
			
		launcher.launch();		
	}
	
}
