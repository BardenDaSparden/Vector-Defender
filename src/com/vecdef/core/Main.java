package com.vecdef.core;

public class Main {

	public static void main(String[] args){
		Application application = new VectorDefender();
		ApplicationSettings settings = new ApplicationSettings();
		ApplicationLauncher launcher = new ApplicationLauncher(application, settings);
		
		settings.width = 1280;
		settings.height = 720;
		settings.fullscreen = false;
		settings.borderless = true;
		settings.vsync = false;
		settings.renderrate = 60;
		settings.updaterate = 60;
		settings.multisamples = 1;
			
		launcher.launch();		
	}
	
}
