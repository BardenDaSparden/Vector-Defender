package org.javatroid.graphics;

import java.nio.FloatBuffer;

import org.javatroid.core.Util;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.javatroid.math.Vector4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

public class ShaderProgram {

	static FloatBuffer pool16 = BufferUtils.createFloatBuffer(16);
	
	private int programHandle;
	
	public ShaderProgram(){
		programHandle = glCreateProgram();
		
		if(programHandle == 0){
			System.err.println("Error creating shader");
		}
	}
	
	public void bind(){
		glUseProgram(programHandle);
	}
	
	public void addVertexShader(String vertexShaderPath){
		addShader(vertexShaderPath, GL_VERTEX_SHADER);
	}
	
	public void addGeometryShader(String geometryShaderPath){
		addShader(geometryShaderPath, GL_GEOMETRY_SHADER);
	}
	
	public void addFragmentShader(String fragmentShaderPath){
		addShader(fragmentShaderPath, GL_FRAGMENT_SHADER);
	}
	
	private void addShader(String shaderPath, int shaderType){
		String shaderSource = Util.loadFileAsString(shaderPath);
		int shaderHandle = glCreateShader(shaderType);
		glShaderSource(shaderHandle, shaderSource);
		glCompileShader(shaderHandle);
		glAttachShader(programHandle, shaderHandle);
	}
	
	
	public void compile(){
		glLinkProgram(programHandle);
		
		if(glGetProgrami(programHandle, GL_VALIDATE_STATUS) == GL_FALSE){
			System.out.println("Link failed " + programHandle);
			System.out.println(glGetProgramInfoLog(programHandle, 1024));
		}
		
		glValidateProgram(programHandle);
		
		if(glGetProgrami(programHandle, GL_VALIDATE_STATUS) == GL_FALSE){
			System.out.println("Validation failed " + programHandle);
			System.out.println(glGetProgramInfoLog(programHandle, 1024));
		}
		
	}
	
	public int getUniformLocation(String uniformName){
		return glGetUniformLocation(programHandle, uniformName);
	}
	
	public void release(){
		glUseProgram(0);
	}
	
	public int getAttributeLocation(String attributeName){
		return glGetAttribLocation(programHandle, attributeName);
	}
	
	public void setUniformf(String uniformName, float v0){
		int loc = getUniformLocation(uniformName);
		glUniform1f(loc, v0);
	}
	
	public void setUniformf(String uniformName, Vector2f v){
		int loc = getUniformLocation(uniformName);
		glUniform2f(loc, v.x, v.y);
	}
	
	public void setUniformf(String uniformName, Vector3f v){
		int loc = getUniformLocation(uniformName);
		glUniform3f(loc, v.x, v.y, v.z);
	}
	
	public void setUniformf(String uniformName, Vector4f v){
		int loc = getUniformLocation(uniformName);
		glUniform4f(loc, v.x, v.y, v.z, v.w);
	}
	
	public void setUniformi(String uniformName, float v0){
		int loc = getUniformLocation(uniformName);
		glUniform1i(loc, (int)v0);
	}
	
	public void setUniformi(String uniformName, Vector2f v){
		int loc = getUniformLocation(uniformName);
		glUniform2i(loc, (int)v.x, (int)v.y);
	}
	
	public void setUniformi(String uniformName, Vector3f v){
		int loc = getUniformLocation(uniformName);
		glUniform3i(loc, (int)v.x, (int)v.y, (int)v.z);
	}
	
	public void setUniformi(String uniformName, Vector4f v){
		int loc = getUniformLocation(uniformName);
		glUniform4i(loc, (int)v.x, (int)v.y, (int)v.z, (int)v.w);
	}
	
	public void setUniformMat(String uniformName, org.javatroid.math.Matrix4f matrix){
		int loc = getUniformLocation(uniformName);
		matrix.store(pool16);
		pool16.flip();
		glUniformMatrix4fv(loc, false, pool16);
		pool16.clear();
	}
	
	public void setUniformTexture(String uniformName, Texture texture, int textureUnit){
		int loc = getUniformLocation(uniformName);
		glActiveTexture(GL_TEXTURE0 + textureUnit);
		texture.bind();
		glUniform1i(loc, textureUnit);
	}
	
	
	
}
