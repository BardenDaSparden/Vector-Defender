package com.vecdef.objects;

import java.util.ArrayList;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import debug.physics.PointMass;
import debug.physics.Spring;

public class Grid {
	
	protected ArrayList<Spring> springs;
	protected ArrayList<PointMass> masses;
	public PointMass[][] points;
	
	int gridWidth;
	int gridHeight;
	int cellWidth;
	int cellHeight;
	int rows;
	int cols;
	
	public Grid(int width, int height, int cellWidth, int cellHeight){
		this.gridWidth = width;
		this.gridHeight = height;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		
		Vector2f min = new Vector2f(-gridWidth / 2.0F + 1.0F, -gridHeight / 2.0F + 1.0F);
	    Vector2f max = new Vector2f(gridWidth / 2.0F - 1.0F, gridHeight / 2.0F - 1.0F);
	    rows = (int)((max.y - min.y) / cellHeight) + 2;
	    cols = (int)((max.x - min.x) / cellWidth) + 2;
	    
	    springs = new ArrayList<Spring>();
	    masses = new ArrayList<PointMass>();
	    points = new PointMass[rows][cols];
	    
	    float offsetX = 0;
	    float offsetY = 0;
	    
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
	    
//	    float stiff = 0.125F;
//	    float damping = 0.0001F;
	    
	    float stiff = 0.150F;
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
	
	public void update(){
		
		for(Spring spring : springs)
			spring.update();
		
		for(PointMass mass : masses)
			mass.update();
	}
	
	public int getWidth(){
		return gridWidth;
	}

	public int getHeight(){
		return gridHeight;
	}
	
	public int numRows(){
		return rows;
	}
	
	public int numCols(){
		return cols;
	}
	
	public ArrayList<PointMass> getMasses(){
		return masses;
	}
	
	public PointMass[][] getPoints(){
		return points;
	}
}

class GridForce {
	
	public Vector3f position;
	public float radius;
	public Vector3f force;
	
	public GridForce(){
		position = new Vector3f();
		radius = 0;
		force = new Vector3f();
	}
	
	static Vector3f dPos = new Vector3f();
	public boolean inRangeOf(PointMass point){
		Vector3f pointPosition = point.getPosition();
		dPos.set(pointPosition.x - position.x, pointPosition.y - position.y, pointPosition.z - position.z);
		return (dPos.lengthSquared() < radius * radius);
	}
	
}