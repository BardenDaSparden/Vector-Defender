package org.barden.core;

public abstract class Application {

	protected ApplicationTask task;
	
	public abstract void initialize();
	public abstract void update();
	public abstract void render(double i);
	public abstract void destroy();
	public abstract void resize(int width, int height);
	
}
