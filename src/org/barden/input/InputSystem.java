package org.barden.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWJoystickCallback;

public class InputSystem {
	
	static final int KEYBOARD = 0;
	static final int MOUSE = 1;
	static final int JOYSTICK_1 = 2; // Joypad 1
	static final int JOYSTICK_2 = 3; // Joypad 2
	static final int JOYSTICK_3 = 4; // Joypad 3
	static final int JOYSTICK_4 = 5; // Joypad 4
	static final int NUM_DEVICES = 6;
	
	GLFWJoystickCallback joystickCB;
	InputDevice[] devices;
	ArrayList<InputSystemListener> listeners;
	
	public InputSystem(){
		joystickCB = new GLFWJoystickCallback() {
			@Override
			public void invoke(int joystickID, int event) {
				Joystick joystick = getJoystick(joystickID);
				
				if(event == GLFW_CONNECTED){
					joystick.connect();
					onJoystickChange(joystickID, event);
				}
				
				if(event == GLFW_DISCONNECTED){
					joystick.disconnect();
					onJoystickChange(joystickID, event);
				}
			}
		};
		
		devices = new InputDevice[NUM_DEVICES];
		devices[KEYBOARD] = new Keyboard();
		devices[MOUSE] = new Mouse();
		devices[JOYSTICK_1] = new Joystick(0);
		devices[JOYSTICK_2] = new Joystick(1);
		devices[JOYSTICK_3] = new Joystick(2);
		devices[JOYSTICK_4] = new Joystick(3);
		listeners = new ArrayList<InputSystemListener>();
	}
	
	public void register(long window){
		glfwSetJoystickCallback(joystickCB);
		for(int i = 0; i < NUM_DEVICES; i++){
			InputDevice device = devices[i];
			device.register(window);
		}
	}
	
	public void unregister(){
		joystickCB.close();
		for(int i = 0; i < NUM_DEVICES; i++){
			InputDevice device = devices[i];
			device.release();
		}
	}
	
	public void update(){
		for(int i = 0; i < NUM_DEVICES; i++){
			InputDevice device = devices[i];
			device.update();
		}
	}
	
	protected void onJoystickChange(int joystickID, int event){
		for(int i = 0; i < listeners.size(); i++){
			InputSystemListener listener = listeners.get(i);
			listener.onJoystickChange(joystickID, event);
		}
	}
	
	InputDevice getDevice(int deviceID){
		if(deviceID > NUM_DEVICES)
			throw new RuntimeException("Device ID doesn't exist");
		return devices[deviceID];
	}
	
	public void addListener(InputSystemListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(InputSystemListener listener){
		listeners.remove(listener);
	}
	
	public Mouse getMouse(){
		return (Mouse) getDevice(MOUSE);
	}
	
	public Keyboard getKeyboard(){
		return (Keyboard) getDevice(KEYBOARD);
	}
	
	public Joystick getJoystick(int joystickID){
		return (Joystick) getDevice(2 + joystickID);
	}
}
