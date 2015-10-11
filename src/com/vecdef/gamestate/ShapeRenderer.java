package com.vecdef.gamestate;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.ShaderProgram;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.lwjgl.BufferUtils;

import com.vecdef.model.Primitive.DrawType;

public class ShapeRenderer {

	public static final int MAX_DRAWS = 1000;
	
	ShaderProgram shader;
	
	OrthogonalCamera camera;
	
	FloatBuffer positionBuffer;
	FloatBuffer colorBuffer;
	
	Vector2f[] positions;
	Vector4f[] colors;
	
	int draws = 0;
	
	DrawType drawType;
	
	public ShapeRenderer(){
		shader = new ShaderProgram();
		shader.addVertexShader("shaders/shape.vs");
		shader.addFragmentShader("shaders/shape.fs");
		shader.compile();
		
		camera = null;
		
		positionBuffer = BufferUtils.createFloatBuffer(MAX_DRAWS * 2);
		colorBuffer = BufferUtils.createFloatBuffer(MAX_DRAWS * 4);
		
		positions = new Vector2f[MAX_DRAWS];
		colors = new Vector4f[MAX_DRAWS];
		
		glEnable(GL_BLEND);
		glEnable(GL_LINE_SMOOTH);
		
	}
	
	public void begin(DrawType drawType, BlendState blendState){
		this.drawType = drawType;
		glBlendFunc(blendState.sFactor, blendState.dFactor);
	}
	
	public void draw(Vector2f position, Vector4f color){
		if(draws >= MAX_DRAWS)
			flush();
		
		positions[draws] = position;
		colors[draws] = color;
		
		draws++;
	}
	
	private void flush(){
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
		
		glVertexAttribPointer(0, 2, false, 0, positionBuffer);
		glVertexAttribPointer(1, 4, false, 0, colorBuffer);
		
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
