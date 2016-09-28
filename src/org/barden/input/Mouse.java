package org.barden.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Mouse implements InputDevice {

	public static final int LEFT_BUTTON = 0;
	public static final int RIGHT_BUTTON = 1;
	public static final int MIDDLE_BUTTON = 2;
	public static final int NUM_BUTTONS = GLFW_MOUSE_BUTTON_LAST + 1;
	
	GLFWCursorPosCallback cursorPosCB;
	GLFWMouseButtonCallback mouseButtonCB;
	GLFWScrollCallback scrollCB;
	
	long window;
	IntBuffer widthBuf;
	IntBuffer heightBuf;
	
	ArrayList<MouseListener> listeners;
	
	int windowWidth;
	int windowHeight;
	
	double previousX = 0.0, previousY = 0.0;
	double positionX = 0.0, positionY = 0.0;
	
	boolean[] buttons;
	
	double previousScroll = 0.0;
	double scroll = 0.0;
	
	public Mouse(){
		widthBuf = BufferUtils.createIntBuffer(1);
		heightBuf = BufferUtils.createIntBuffer(1);
		buttons = new boolean[NUM_BUTTONS];
		cursorPosCB = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				previousX = positionX;
				previousY = positionY;
				positionX = xpos - (windowWidth / 2.0f);
				positionY = (windowHeight / 2.0f) - ypos;
				onMove((float)(positionX - previousX), (float)(positionY - previousY));
			}
		};
		
		mouseButtonCB = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if(action == GLFW_PRESS){
					buttons[button] = true;
					onButtonPress(button);
				}
				if(action == GLFW_RELEASE){
					buttons[button] = false;
					onButtonRelease(button);
				}
			}
		};
		scrollCB = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				previousScroll = scroll;
				scroll = yoffset;
				onScroll((float)(scroll - previousScroll));
			}
		};
		listeners = new ArrayList<MouseListener>();
		buttons = new boolean[NUM_BUTTONS];
	}
	
	@Override
	public void register(long windowHandle) {
		glfwSetCursorPosCallback(windowHandle, cursorPosCB);
		glfwSetMouseButtonCallback(windowHandle, mouseButtonCB);
		glfwSetScrollCallback(windowHandle, scrollCB);
		window = windowHandle;
		
		glfwGetWindowSize(window, widthBuf, heightBuf);
		windowWidth = widthBuf.get(0);
		windowHeight = heightBuf.get(0);
		positionX = 0;
		previousX = 0;
		positionY = 0;
		previousY = 0;
	}

	@Override
	public void release() {
		cursorPosCB.close();
		mouseButtonCB.close();
		scrollCB.close();
	}

	@Override
	public void update() {
		
	}
	
	public void addListener(MouseListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(MouseListener listener){
		listeners.remove(listener);
	}
	
	protected void onButtonPress(int button){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onButtonPress(button);
	}
	
	protected void onButtonRelease(int button){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onButtonRelease(button);
	}
	
	protected void onMove(float dx, float dy){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onMove(dx, dy);
	}
	
	protected void onScroll(float dy){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onScroll(dy);
	}
	
	public double getX(){
		return positionX;
	}
	
	public double getY(){
		return positionY;
	}
	
	public void setCursorPosition(int x, int y, int windowWidth, int windowHeight){
		glfwSetCursorPos(window, x + windowWidth / 2, y + windowHeight / 2);
	}
	
	public double getScroll(){
		return scroll;
	}
	
	public boolean isButtonDown(int button){
		return buttons[button];
	}
}
