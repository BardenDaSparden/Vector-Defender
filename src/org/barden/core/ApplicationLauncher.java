package org.barden.core;

//import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.*;
import org.barden.util.Debug;

public class ApplicationLauncher {
	
	public void launch(Application application, ApplicationSettings settings){
		glfwInit();
		//glfwInit();
			ApplicationTask task = new ApplicationTask(this, application, settings);
			task.run();
		//glfwTerminate();
		Debug.print();
	}
}