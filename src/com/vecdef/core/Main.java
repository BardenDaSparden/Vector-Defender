package com.vecdef.core;

public class Main {

	public static void main(String[] args){
	
		ApplicationSettings settings = new ApplicationSettings();
		settings.width = 1600;
		settings.height = 900;
		settings.fullscreen = true;
		settings.borderless = false;
		settings.vsync = false;
		settings.renderrate = 60;
		settings.updaterate = 60;
		settings.multisamples = 4;
		
		Application application = new VectorDefender();
		
		ApplicationLauncher launcher = new ApplicationLauncher(application, settings);
		launcher.launch();
		
	}
	
}
