package org.barden.input;

public interface MouseListener {

	public void onButtonPress(int button);
	public void onButtonRelease(int button);
	public void onMove(float dx, float dy);
	public void onScroll(float dy);
	
}
