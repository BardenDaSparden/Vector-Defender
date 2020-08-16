package org.barden.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.barden.input.InputSystem;
import org.barden.util.Debug;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.opengl.GL;

import static org.lwjgl.openal.ALC10.*;

public class ApplicationTask {
	
	ApplicationLauncher launcher;
	Application application;
	ApplicationSettings settings;
	
	InputSystem input;
	
	GLFWFramebufferSizeCallback resizeCB;
	
	long alDevice;
	long alContext;
	
	boolean running;
	long window;
	
	public ApplicationTask(ApplicationLauncher launcher, Application application, ApplicationSettings settings){
		this.launcher = launcher;
		this.application = application;
		this.settings = settings;
	}
	
	void initGLFW(){
		int resizable = (settings.resizable) ? GL_TRUE : GL_FALSE;
		int decorate = (settings.decorated) ? GL_TRUE : GL_FALSE;
		int visible = GL_TRUE;
		int width = settings.width;
		int height = settings.height;
		
		long primaryMonitor = glfwGetPrimaryMonitor();
		long monitor = (settings.fullscreen) ? primaryMonitor : 0L;
		
		//Configure new window properties
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, resizable);
		glfwWindowHint(GLFW_DECORATED, decorate);
		glfwWindowHint(GLFW_VISIBLE, visible);
		glfwWindowHint(GLFW_SAMPLES, 0);
		glfwWindowHint(GLFW_ALPHA_BITS, 8);
		glfwWindowHint(GLFW_DEPTH_BITS, 24);
		glfwWindowHint(GLFW_STENCIL_BITS, 8);
		
		resizeCB = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				application.resize(width, height);
			}
		};
		
		window = glfwCreateWindow(width, height, settings.title, monitor, 0L);
		Debug.logInfo("Application", "Display Created[" + width + ", " + height + ", " + (monitor != 0) + "]");
		
		//Configure listeners
		glfwSetFramebufferSizeCallback(window, resizeCB);
		
		//Set window position to center of primary screen
		//ByteBuffer vidBuffer = glfwGetVideoMode(primaryMonitor);
		GLFWVidMode vidmode = glfwGetVideoMode(primaryMonitor);
		glfwSetWindowPos(window, vidmode.width() / 2 - width / 2, vidmode.height() / 2  - height / 2);
		
		glfwSetCursorPos(window, settings.width / 2, settings.height / 2);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		//glfwSwapInterval(1);
		
		alDevice = alcOpenDevice((ByteBuffer)null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(alDevice);
		
		alContext = alcCreateContext(alDevice, (IntBuffer)null);
		alcMakeContextCurrent(alContext);
		AL.createCapabilities(deviceCaps);
		
		input = new InputSystem();
		input.register(window);
	}
	
	void initGL(){
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		String glVersion = glGetString(GL_VERSION);
		String glVendor  = glGetString(GL_VENDOR);
		String glRenderer = glGetString(GL_RENDERER);
		String glGLSLVersion = glGetString(GL_SHADING_LANGUAGE_VERSION);
		
		Debug.logInfo("OpenGL Info", "Version=" + glVersion);
		Debug.logInfo("OpenGL Info", "Vendor=" + glVendor);
		Debug.logInfo("OpenGL Info", "Renderer=" + glRenderer);
		Debug.logInfo("OpenGL Info", "GLSL Version=" + glGLSLVersion);
	}
	
	public void run(){
		running = true;
		
		Debug.logInfo("Application", "Initialize");
		initGLFW();
		initGL();
		
		application.task = this;
		application.initialize();
		
		final long NANOS_PER_FRAME = (long)settings.framerate / 1000000000L;
		final short MAX_UPDATES_PER_FRAME = 1;
		long previousTime = System.nanoTime();
		long currentTime = previousTime + NANOS_PER_FRAME;
		long unprocessedTime = 0L;
		
		while(running && (glfwWindowShouldClose(window) == false) ){
			previousTime = currentTime;
			currentTime = System.nanoTime();
			unprocessedTime += (currentTime - previousTime);
			
			short updates = 0;
			while(unprocessedTime >= NANOS_PER_FRAME && updates < MAX_UPDATES_PER_FRAME){
				input.update();
				application.update();
				unprocessedTime -= NANOS_PER_FRAME;
				updates++;
			}
			
			double interpolation = 0;
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			application.render(interpolation);
			glfwSwapBuffers(window);
			glfwPollEvents();
			
			try {
				Thread.sleep(11);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Debug.print();
		}
		dispose();
		return;
	}
	
	public void stop(){
		running = false;
	}
	
	public void dispose(){
		Debug.logInfo("Application", "Terminate");
		resizeCB.close();
		alcCloseDevice(alDevice);
		input.unregister();
		application.destroy();
		glfwDestroyWindow(window);
	}
	
	public ApplicationSettings getSettings(){
		return settings;
	}
	
	public InputSystem getInput(){
		return input;
	}
	
}
