package org.javatroid.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.nio.ByteBuffer;

public class Texture {

	public static final int CLAMP = GL_CLAMP_TO_EDGE;
	public static final int REPEAT = GL_REPEAT;
	public static final int NEAREST = GL_NEAREST;
	public static final int LINEAR = GL_LINEAR;
	
	private final int handle;
	private final int width, height;
	
	public Texture(int textureID, int width, int height){
		this.handle = textureID;
		this.width = width;
		this.height = height;
	}
	
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	
	public void release(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void dispose(){
		glDeleteTextures(handle);
	}
	
	public void setTextureWrap(int wrap){
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		release();
	}
	
	public void setTextureFilter(int filter){
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		release();
	}
	
	public int getTextureHandle(){
		return handle;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public static Texture createEmptyTexture(int width, int height){
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor4f(1, 1, 1, 1);
		
		return new Texture(textureID, width, height);
	}
	
}
