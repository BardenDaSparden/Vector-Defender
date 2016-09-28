package com.vecdef.rendering;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.FrameBuffer;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.javatroid.math.Vector4f;

import com.vecdef.audio.AudioAnalyzer;
import com.vecdef.objects.Grid;
import com.vecdef.rendering.ShapeRenderer.DrawType;

import debug.physics.PointMass;

public class GridRenderer {
	//TODO implement
	
	ShapeRenderer renderer;
	final int NUM_VERTICES = 80000;
	int vertexIdx = 0;
	ArrayList<Vector2f> positions;
	ArrayList<Vector4f> colors;
	
	Vector4f color = new Vector4f(1, 0, 0, 0.23f);
	AudioAnalyzer analyzer;
	FrameBuffer buffer;
	FrameBuffer buffer2;
	Vector3f HSB = new Vector3f(0, 1, 0.5f);
	Vector3f RGB = new Vector3f(1, 0, 0);
	Vector4f waveColor = new Vector4f(1, 0, 0, 1);
	float waveAmplitude = 50;
	float time = 0;
	
	public GridRenderer(ShapeRenderer renderer){
		this.renderer = renderer;
		positions = new ArrayList<Vector2f>();
		colors = new ArrayList<Vector4f>();
		for(int i = 0; i < NUM_VERTICES; i++){
			positions.add(new Vector2f());
			colors.add(new Vector4f());
		}
	}
	
	void drawLine(Vector3f p0, Vector3f p1){
		positions.get(vertexIdx).set(p0.x, p0.y);
	    positions.get(vertexIdx + 1).set(p1.x, p1.y);
	    colors.get(vertexIdx).set(color);
	    colors.get(vertexIdx + 1).set(color);
	    if(vertexIdx + 2 < NUM_VERTICES)
	    	vertexIdx += 2;
	}
	
	public void render(Grid grid){
		
		HSB.x += 1.0f / 1440.0f;
		java.awt.Color c = java.awt.Color.getHSBColor(HSB.x, HSB.y, HSB.z);
		RGB.x = c.getRed() / 255.0f;
		RGB.y = c.getGreen() / 255.0f;
		RGB.z = c.getBlue() / 255.0f;
		color.set(RGB.x, RGB.y, RGB.z, color.w);
		
		PointMass[][] points = grid.getPoints();
		int cols = grid.numCols();
		int rows = grid.numRows();
		
		Vector3f temp1 = new Vector3f();
		Vector3f temp2 = new Vector3f();
		Vector3f temp3 = new Vector3f();
		Vector3f temp4 = new Vector3f();
		
		for (int j = 0; j < points.length - 1; j++){
			for(int i = 0; i < points[j].length - 1; i++){
				PointMass point = points[j][i];
				PointMass point2 = points[j][i + 1];
				PointMass point3 = points[j + 1][i];
				PointMass point4 = points[j + 1][i + 1];
				
				Vector3f TL = point.getPosition();
				Vector3f TR = point2.getPosition();
				Vector3f BL = point3.getPosition();
				Vector3f BR = point4.getPosition();
				
				temp1.x = TL.x + (TR.x - TL.x) * 0.5f;
				temp1.y = TL.y + (TR.y - TL.y) * 0.5f;
				temp1.z = TL.z + (TR.z - TL.z) * 0.5f;
				
				temp2.x = TR.x + (BR.x - TR.x) * 0.5f;
				temp2.y = TR.y + (BR.y - TR.y) * 0.5f;
				temp2.z = TR.z + (BR.z - TR.z) * 0.5f;
				
				temp3.x = BL.x + (BR.x - BL.x) * 0.5f;
				temp3.y = BL.y + (BR.y - BL.y) * 0.5f;
				temp3.z = BL.z + (BR.z - BL.z) * 0.5f;
				
				temp4.x = BL.x + (TL.x - BL.x) * 0.5f;
				temp4.y = BL.y + (TL.y - BL.y) * 0.5f;
				temp4.z = BL.z + (TL.z - BL.z) * 0.5f;
				
				drawLine(temp1, temp3);
				drawLine(temp4, temp2);
				
				drawLine(TL, TR);
				drawLine(TL, BL);			
			}
	    }
		
		for(int i = 0; i < cols - 1; i++){
			PointMass p = points[rows - 1][i];
			PointMass p2 = points[rows - 1][i + 1];
			
			Vector3f start = p.getPosition();
			Vector3f end = p2.getPosition();
			
			drawLine(start, end);
		}	
		
		for(int j = 0; j < rows - 1; j++){
			PointMass p = points[j][cols - 1];
			PointMass p2 = points[j + 1][cols - 1];
			
			Vector3f start = p.getPosition();
			Vector3f end = p2.getPosition();
			
			drawLine(start, end);
		}
		
		renderer.begin(DrawType.LINES, BlendState.ADDITIVE);
			for(int i = 0; i < vertexIdx; i++){
				Vector2f position = positions.get(i);
				Vector4f color = colors.get(i);
				renderer.draw(position, color);
			}
		renderer.end();
		
		vertexIdx = 0;
	}
}