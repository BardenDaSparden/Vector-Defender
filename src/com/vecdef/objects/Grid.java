package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.javatroid.math.Vector4f;
import org.lwjgl.opengl.Display;

import com.vecdef.gamestate.ShapeRenderer;
import com.vecdef.model.Primitive.DrawType;

import debug.physics.PointMass;
import debug.physics.Spring;

public class Grid{
	
	ArrayList<Spring> springs = new ArrayList<Spring>();
	ArrayList<PointMass> masses = new ArrayList<PointMass>();
	PointMass[][] points;
	float cellWidth;
	float cellHeight;
	int rows;
	int cols;
	final Vector2f offset;
	
	Vector4f color = new Vector4f(0.05f, 0.43f, 1f, 0.28f);
	
	public Grid(float cellWidth, float cellHeight){
		
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		
		this.offset = new Vector2f(-cellWidth / 2.0f, -cellHeight / 2.0f);
		
		Vector2f min = new Vector2f(-Display.getWidth() / 2.0F + 1.0F, -Display.getHeight() / 2.0F + 1.0F);
	    Vector2f max = new Vector2f(Display.getWidth() / 2.0F - 1.0F, Display.getHeight() / 2.0F - 1.0F);
	    rows = (int)((max.y - min.y) / cellHeight) + 2;
	    cols = (int)((max.x - min.x) / cellWidth) + 2;

	    float offsetX = cellWidth / 2.0F;
	    float offsetY = cellHeight / 2.0F;

	    points = new PointMass[rows][cols];

	    for (int j = 0; j < rows; j++) {
	    	for(int i = 0; i < cols; i++){
	    		
		        float x = offsetX + min.x + i * cellWidth;
		        float y = offsetY + min.y + j * cellHeight;
		        Vector3f position = new Vector3f(x, y, 10.0F);
		        float invMass = 0.0F;

		        if ((j == 0) || (j == rows - 1) || (i == 0) || (i == cols - 1))
		        	invMass = 0.0F;
		        else {
		        	invMass = 0.25F;
		        }

		        points[j][i] = new PointMass(position, invMass);
		        
	    	}
	    }

	    float stiff = 0.25F;
	    float damping = 0.001F;
	    
	    PointMass start;
	    PointMass right;
	    
	    for (int j = 0; j < rows - 1; j++){
	    	for(int i = 0; i < cols - 1; i++){
	    		
	    		start = points[j][i];
		        right = points[j][(i + 1)];
		        PointMass up = points[(j + 1)][i];

		        springs.add(new Spring(start, right, stiff, damping));
		        springs.add(new Spring(start, up, stiff, damping));
		        
	    	}
	    }
	    
	    int ji = rows - 1;
	    
	    for(int i = 0; i < cols - 1; i++){
	    	start = points[ji][i];
	    	right = points[ji][i + 1];
	    	springs.add(new Spring(start, right, stiff, damping));
	    }
	    
	    int ii = cols - 1;
	    for(int j = 0; j < rows - 1; j++){
	    	start = points[j][ii];
	    	right = points[j + 1][ii];
	    	springs.add(new Spring(start, right, stiff, damping));
	    }

	    for (PointMass[] row : points)
	    	for(PointMass mass : row)
	    		masses.add(mass);
	        
	}

	public void applyDirectedForce(Vector3f force, Vector3f position, float radius){
		for (PointMass mass : masses){
			if (mass.getPosition().sub(position).lengthSquared() <= radius * radius){
		        float length = mass.getPosition().sub(position).length();
		        float s = 10.0F / (10.0F + length);
		        mass.applyForce(force.scale(s));
		    }
		}
	}

	public void applyImplosiveForce(float force, Vector3f position, float radius){
		for (PointMass mass : masses){
			 float d2 = position.sub(mass.getPosition()).lengthSquared();
			 if (d2 < radius * radius){
				 float s = 10.0F * force;
				 float s2 = 1.0F / (100.0F + d2);
				 
				 Vector3f dPos = position.sub(mass.getPosition());
				 mass.applyForce(dPos.scale(s).scale(s2));
			     mass.increaseDamping(0.6F);
			 }
		}
	}

	public void applyExplosiveForce(float force, Vector3f position, float radius){
		for (PointMass mass : masses){
			float d2 = position.sub(mass.getPosition()).lengthSquared();
			if (d2 < radius * radius){
				float s = 100.0F * force;
		        float s2 = 1.0F / (10000.0F + d2);

		        Vector3f dPos = mass.getPosition().sub(position);

		        mass.applyForce(dPos.scale(s).scale(s2));
		        mass.increaseDamping(0.6F);
			}
		}
	}

	
	float time = 0;
	
	final int SKIP_TIME = 5;
	int skip = SKIP_TIME;
	public void update(){
		
		for(Spring spring : springs)
			spring.update();
		
		for(PointMass mass : masses)
			mass.update();
		
	}

	public void draw(ShapeRenderer renderer){
		
		renderer.begin(DrawType.LINES, BlendState.ADDITIVE);
		
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
				
				Vector3f IT = TL.add(TR.sub(TL).scale(0.5f));
				Vector3f IR = TR.add(BR.sub(TR).scale(0.5f));
				Vector3f IB = BL.add(BR.sub(BL).scale(0.5f));
				Vector3f IL = BL.add(TL.sub(BL).scale(0.5f));
				
				drawLine(IT, IB, renderer);
				drawLine(IL, IR, renderer);
				
				drawLine(TL, TR, renderer);
				drawLine(TL, BL, renderer);			
			}
	    }
		
		for(int i = 0; i < cols - 1; i++){
			PointMass p = points[rows - 1][i];
			PointMass p2 = points[rows - 1][i + 1];
			
			Vector3f start = p.getPosition();
			Vector3f end = p2.getPosition();
			
			drawLine(start, end, renderer);
		}	
		
		for(int j = 0; j < rows - 1; j++){
			PointMass p = points[j][cols - 1];
			PointMass p2 = points[j + 1][cols - 1];
			
			Vector3f start = p.getPosition();
			Vector3f end = p2.getPosition();
			
			drawLine(start, end, renderer);
		}	
		
		renderer.end();
		
	}
	
	private void drawLine(Vector3f start, Vector3f end, ShapeRenderer renderer){
	    float startFactor = (start.z + 2000.0F) / 2000.0F;
	    float endFactor = (end.z + 2000.0F) / 2000.0F;
	    
	    Vector2f v0 = new Vector2f(start.x * startFactor, start.y * startFactor);
	    Vector2f v1 = new Vector2f(end.x * endFactor, end.y * endFactor);
	    
	    renderer.draw(v0, color);
	    renderer.draw(v1, color);
	}
}