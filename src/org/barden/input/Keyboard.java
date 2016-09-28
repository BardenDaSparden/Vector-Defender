package org.barden.input;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

public class Keyboard implements InputDevice {

	static final int NUM_KEYS = GLFW_KEY_LAST + 1;
	
	GLFWKeyCallback keyCB;
	GLFWCharCallback charCB;
	ArrayList<KeyboardListener> listeners;
	boolean[] keys;
	
	public Keyboard(){
		keyCB = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(action == GLFW_PRESS){
					keys[key] = true;
					onKeyPress(key);
				}
				
				if(action == GLFW_RELEASE){
					keys[key] = false;
					onKeyRelease(key);
				}
			}
		};
		charCB = new GLFWCharCallback() {
			@Override
			public void invoke(long window, int codepoint) {
				onChar(Character.toChars(codepoint)[0]);
			}
		};
		listeners = new ArrayList<KeyboardListener>();
		keys = new boolean[NUM_KEYS];
	}
	
	@Override
	public void register(long windowHandle) {
		glfwSetKeyCallback(windowHandle, keyCB);
		glfwSetCharCallback(windowHandle, charCB);
	}

	@Override
	public void release() {
		keyCB.close();
		charCB.close();
	}

	@Override
	public void update() {
		
	}
	
	public void addListener(KeyboardListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(KeyboardListener listener){
		listeners.remove(listener);
	}
	
	protected void onKeyPress(int key){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onKeyPress(key);
	}
	
	protected void onKeyRelease(int key){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onKeyRelease(key);
	}
	
	protected void onChar(char ch){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onChar(ch);
	}
	
	public boolean isKeyDown(int key){
		return keys[key];
	}
}
