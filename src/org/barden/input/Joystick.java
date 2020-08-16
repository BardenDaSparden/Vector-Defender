package org.barden.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWJoystickCallback;

public class Joystick implements InputDevice {

	public static final int ANALOG_LEFT_X = 0;
	public static final int ANALOG_LEFT_Y = 1;
	public static final int ANALOG_RIGHT_X = 2;
	public static final int ANALOG_RIGHT_Y = 3;
	public static final int ANALOG_TRIGGER_LEFT = 4;
	public static final int ANALOG_TRIGGER_RIGHT = 5;
	static final int NUM_AXES = 6;
	
	public static final int BUTTON_A = 0;
	public static final int BUTTON_B = 1;
	public static final int BUTTON_X = 2;
	public static final int BUTTON_Y = 3;
	public static final int BUTTON_LEFT_BUMPER = 4;
	public static final int BUTTON_RIGHT_BUMPER = 5;
	public static final int BUTTON_BACK = 6;
	public static final int BUTTON_START = 7;
	public static final int BUTTON_LEFT_ANALOG_CLICK = 8;
	public static final int BUTTON_RIGHT_ANALOG_CLICK = 9;
	public static final int BUTTON_DPAD_UP = 10;
	public static final int BUTTON_DPAD_RIGHT = 11;
	public static final int BUTTON_DPAD_DOWN = 12;
	public static final int BUTTON_DPAD_LEFT = 13;
	static final int NUM_BUTTONS = 14;
	
	static final float AXIS_EPSILON = 0.005f;
	
	int id;
	FloatBuffer axesBuffer;
	ByteBuffer buttonsBuffer;
	
	ArrayList<JoystickListener> listeners;
	
	GLFWJoystickCallback joystickCB;
	float[] previousAxes;
	float[] axes;
	boolean[] previousButtons;
	boolean[] buttons;
	boolean isConnected;
	
	public Joystick(int joystickIdx){
		id = joystickIdx;
		axesBuffer = BufferUtils.createFloatBuffer(NUM_AXES);
		buttonsBuffer = BufferUtils.createByteBuffer(NUM_BUTTONS);
		listeners = new ArrayList<JoystickListener>();
		previousAxes = new float[NUM_AXES];
		axes = new float[NUM_AXES];
		previousButtons = new boolean[NUM_BUTTONS];
		buttons = new boolean[NUM_BUTTONS];
		isConnected = glfwJoystickPresent(GLFW_JOYSTICK_1 + id);
	}
	
	@Override
	public void register(long windowHandle) {
		//glfwSetJoystickCallback(joystickCB);
	}

	@Override
	public void release() {
		//joystickCB.close();
	}

	@Override
	public void update() {
		if(isConnected && glfwJoystickPresent(GLFW_JOYSTICK_1 + id)){
			for(int i = 0; i < NUM_AXES; i++)
				previousAxes[i] = axes[i];
			for(int i = 0; i < NUM_BUTTONS; i++)
				previousButtons[i] = buttons[i];
			
			axesBuffer = glfwGetJoystickAxes(GLFW_JOYSTICK_1 + id);
			buttonsBuffer = glfwGetJoystickButtons(GLFW_JOYSTICK_1 + id);
			
			for(int i = 0; i < NUM_AXES; i++){
				axes[i] = axesBuffer.get();
			}
					
			for(int i = 0; i < buttonsBuffer.capacity(); i++)
				buttons[i] = (buttonsBuffer.get(i) == 1) ? true : false;
			
			for(int i = 0; i < NUM_BUTTONS; i++){
				boolean previous = previousButtons[i];
				boolean current = buttons[i];
				if(previous && !current)
					onButtonRelease(i);
				if(!previous && current)
					onButtonPress(i);
			}
		}
	}

	public void addListener(JoystickListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(JoystickListener listener){
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
	
	public void connect(){
		System.out.println(id + " connected");
		isConnected = true;
	}
	
	public void disconnect(){
		System.out.println(id + " disconnected");
		isConnected = false;
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	
	public boolean isButtonDown(int button){
		return buttons[button];
	}
	
	public float getLeftTrigger(){
		return axes[ANALOG_TRIGGER_LEFT];
	}
	
	public float getRightTrigger(){
		return axes[ANALOG_TRIGGER_RIGHT];
	}
	
	public float getLeftX(){
		return axes[ANALOG_LEFT_X];
	}
	
	public float getLeftY(){
		return axes[ANALOG_LEFT_Y];
	}
	
	public float getRightX(){
		return axes[ANALOG_RIGHT_X];
	}
	
	public float getRightY(){
		return axes[ANALOG_RIGHT_Y];
	}
	
}
