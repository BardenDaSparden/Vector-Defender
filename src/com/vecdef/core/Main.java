package com.vecdef.core;

public class Main {

	public static void main(String[] args){
		Application application = new VectorDefender();
		ApplicationSettings settings = new ApplicationSettings();
		ApplicationLauncher launcher = new ApplicationLauncher(application, settings);
		
		settings.width = 1600;
		settings.height = 900;
		settings.fullscreen = true;
		settings.borderless = false;
		settings.vsync = false;
		settings.renderrate = 60;
		settings.updaterate = 60;
		settings.multisamples = 4;
			
		launcher.launch();		
	}
	
}
