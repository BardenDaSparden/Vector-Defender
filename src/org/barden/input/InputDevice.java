package org.barden.input;

public interface InputDevice {

	public void register(long windowHandle);
	public void release();
	public void update();
	
}
