package org.javatroid.graphics;

import org.javatroid.math.Vector4f;
import static org.lwjgl.opengl.GL11.*;

public class GLUtil {

	static Vector4f clearColor;
	
	static{
		clearColor = new Vector4f(0, 0, 0, 1);
	}
	
	public static void setClearColor(float r, float g, float b, float a){
		clearColor.x = r;
		clearColor.y = g;
		clearColor.z = b;
		clearColor.w = a;
	}
	
	public static void setClearColor(Vector4f color){
		clearColor = color;
	}
	
	public static void clear(boolean color, boolean depth, boolean stencil, boolean scissor){
		int clear = 0;
		
		if(color)
			clear |= GL_COLOR_BUFFER_BIT;
		
		if(depth)
			clear |= GL_DEPTH_BUFFER_BIT;
		
		if(stencil)
			clear |= GL_STENCIL_BUFFER_BIT;
		
		if(scissor)
			clear |= GL_SCISSOR_BIT;
		
		if(clear == 0)
			return;
		
		glClear(clear);
		
	}
	
}
