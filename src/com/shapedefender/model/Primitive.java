package com.shapedefender.model;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public interface Primitive {

	public enum DrawType{
		LINES(GL_LINES), LINE_LOOP(GL_LINE_LOOP), TRIANGLES(GL_TRIANGLES);
		
		public int glDrawMode;
		
		DrawType(int glDrawMode){
			this.glDrawMode = glDrawMode;
		}
		
	}
	
	public DrawType getDrawType();
	
	public ArrayList<Vector2f> getVertices();
	public ArrayList<Vector4f> getColors();
	
}
