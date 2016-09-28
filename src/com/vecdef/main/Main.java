package com.vecdef.main;

import org.barden.core.Application;
import org.barden.core.ApplicationLauncher;
import org.barden.core.ApplicationSettings;

import com.vecdef.gamestate.VectorDefender;

public class Main {

	public static void main(String[] args){
		Application application = new VectorDefender();
		ApplicationSettings settings = new ApplicationSettings();
		ApplicationLauncher launcher = new ApplicationLauncher();
		
		settings.width = 1920;
		settings.height = 1080;
		settings.fullscreen = false;
		settings.decorated = false;
		settings.framerate = 60;
		settings.title = "Vector Defender";
		
		launcher.launch(application, settings);		
	}
	
}
