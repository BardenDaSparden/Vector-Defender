package com.vecdef.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.ShaderProgram;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.lwjgl.BufferUtils;

public class ShapeRenderer {

	public enum DrawType{
		LINES(GL_LINES), LINE_LOOP(GL_LINE_LOOP), TRIANGLES(GL_TRIANGLES);
		
		public int glDrawMode;
		
		DrawType(int glDrawMode){
			this.glDrawMode = glDrawMode;
		}
		
	}
	
	public static final int MAX_DRAWS = 100000;
	
	ShaderProgram shader;
	OrthogonalCamera camera;
	
	FloatBuffer positionBuffer;
	FloatBuffer colorBuffer;
	
	Vector2f[] positions;
	Vector4f[] colors;
	
	int draws = 0;
	
	int batchCount = 0;
	
	DrawType drawType;
	
	public ShapeRenderer(){
		shader = new ShaderProgram();
		shader.addVertexShader("shaders/shape.vs");
		shader.addFragmentShader("shaders/shape.fs");
		shader.compile();
		
		camera = new OrthogonalCamera();
		
		positionBuffer = BufferUtils.createFloatBuffer(MAX_DRAWS * 2);
		colorBuffer = BufferUtils.createFloatBuffer(MAX_DRAWS * 4);
		
		positions = new Vector2f[MAX_DRAWS];
		colors = new Vector4f[MAX_DRAWS];
		
		for(int i = 0; i < MAX_DRAWS; i++){
			positions[i] = new Vector2f(0, 0);
			colors[i] = new Vector4f(0, 0, 0, 1);
		}
		
		glEnable(GL_BLEND);
		glEnable(GL_LINE_SMOOTH);
		
	}
	
	public void startFrame(){
		batchCount = 0;
	}
	
	public void endFrame(){
		
	}
	
	public void begin(DrawType drawType, BlendState blendState){
		this.drawType = drawType;
		glBlendFunc(blendState.sFactor, blendState.dFactor);
	}
	
	public void draw(Vector2f position, Vector4f color){
		if(draws >= MAX_DRAWS)
			flush();
		
		positions[draws].set(position);
		colors[draws].set(color);
		
		draws++;
	}
	
	public void draw(float x, float y, float r, float g, float b, float a){
		if(draws >= MAX_DRAWS)
			flush();
		
		positions[draws].set(x, y);
		colors[draws].set(r, g, b, a);
		
		draws++;
	}
	
	private void flush(){
		
		batchCount++;
		
		for(int i = 0; i < draws; i++){
			Vector2f position = positions[i];
			Vector4f color = colors[i];
			
			position.store(positionBuffer);
			color.store(colorBuffer);
		}
		
		positionBuffer.flip();
		colorBuffer.flip();
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, positionBuffer);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorBuffer);
		
		shader.bind();
		shader.setUniformMat("projection", camera.getCombined());
		
		glDrawArrays(drawType.glDrawMode, 0, draws);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		positionBuffer.clear();
		colorBuffer.clear();
		
		draws = 0;
		
	}
	
	public void end(){
		flush();
	}
	
	public void setCamera(OrthogonalCamera camera){
		this.camera = camera;
	}
	
}
