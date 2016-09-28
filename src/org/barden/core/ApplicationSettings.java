package org.barden.core;

public class ApplicationSettings {

	public int width;
	public int height;
	public String title;
	public boolean decorated;
	public boolean fullscreen;
	public boolean resizable;
	public int framerate;
	
	public ApplicationSettings(){
		width = 800;
		height = 600;
		title = "Default App Title";
		decorated = false;
		fullscreen = true;
		resizable = false;
		framerate = 60;
	}
	
}
