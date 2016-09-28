package org.barden.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class GLError {

	public static String checkCore(){
		String message = "";
		int glError = glGetError();
		
		switch(glError){
			case GL_NO_ERROR:
				message = "N/A";
				break;
			
			case GL_INVALID_ENUM:
				message = "Invalid Enum";
				break;
				
			case GL_INVALID_VALUE:
				message = "Invalid Valid";
				break;
				
			case GL_INVALID_OPERATION:
				message = "Invalid Operation";
				break;
				
			case GL_INVALID_FRAMEBUFFER_OPERATION:
				message = "Invalid Framebuffer Operation";
				break;
				
			case GL_OUT_OF_MEMORY:
				message = "Out Of Memory";
				break;
				
			case GL_STACK_UNDERFLOW:
				message = "Stack Underflow";
				break;
				
			case GL_STACK_OVERFLOW:
				message = "Stack Overflow";
				break;
				
			default:
				message = "Undefined GL Error";
				break;
		}
		
		return message;
	}
	
	public static String checkFramebuffer(){
		String message = "";
		int glError = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		
		switch(glError){
			case GL_FRAMEBUFFER_COMPLETE:
				message = "N/A";
				break;
				
			case GL_FRAMEBUFFER_UNDEFINED:
				message = "Undefined";
				break;
				
			case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				message = "Incomplete Attachment";
				break;
				
			case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				message = "Incomplete Missing Attachment";
				break;
				
			case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
				message = "Incomplete Draw Buffer";
				break;
				
			case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
				message = "Incomplete Read Buffer";
				break;
				
			case GL_FRAMEBUFFER_UNSUPPORTED:
				message = "Unsupported";
				break;
				
			default:
				message = "Unhandled Framebuffer Error";
				break;
		}
		
		return message;
	}
	
}
