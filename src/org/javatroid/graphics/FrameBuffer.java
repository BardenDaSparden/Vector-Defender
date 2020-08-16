package org.javatroid.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {

	public final int ID;
	
	private Texture texture;
	private int width;
	private int height;
	
	public FrameBuffer(int width, int height){
		this.ID = glGenFramebuffers();
		this.texture = Texture.createEmptyTexture(width, height);
		this.width = width;
		this.height = height;
		
		bind();
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getTextureHandle(), 0);

		release();
	}
	
	public void bind(){
		glViewport(0, 0, (int)width, (int)height);
		glBindFramebuffer(GL_FRAMEBUFFER, ID);
	}
	
	public void release(){
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void clear(float r, float g, float b, float a){
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void destroy(){
		glDeleteFramebuffers(ID);
	}
	
}
