package com.vecdef.collision;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

public class LooseGrid {

	int width, height;
	int cellWidth, cellHeight;
	float offsetX, offsetY;
	int rows, cols;
	ArrayList<ArrayList<ArrayList<ICollidable>>> objects;
	
	public LooseGrid(int width, int height, int rows, int cols){
		this.width = width;
		this.height = height;
		this.cellWidth = width / cols;
		this.cellHeight = height / rows;
		this.offsetX = width / 2.0f - cellWidth / 2.0f;
		this.offsetY = height / 2.0f - cellHeight / 2.0f;
		this.rows = rows;
		this.cols = cols;
		objects = new ArrayList<ArrayList<ArrayList<ICollidable>>>();
		for(int i = 0; i < rows; i++){
			ArrayList<ArrayList<ICollidable>> list = new ArrayList<ArrayList<ICollidable>>();
			objects.add(list);
			for(int j = 0; j < cols; j++){
				ArrayList<ICollidable> listInner = new ArrayList<ICollidable>();
				list.add(listInner);
			}
		}
	}
	
	void insertAt(int row, int col, ICollidable collidable){
		boolean xOutOfRange = col < 0 || col >= cols;
		boolean yOutOfRange = row < 0 || row >= rows;
		
		if(xOutOfRange || yOutOfRange)
			return;
		
		ArrayList<ICollidable> list = objects.get(row).get(col);
		
		if(!list.contains(collidable))
			objects.get(row).get(col).add(collidable);
	}
	
	public void insert(ICollidable collidable){
		Vector2f position = collidable.getTransform().getTranslation();
		
		float r = collidable.getRadius();
		float x = position.x + offsetX;
		float y = position.y + offsetY;
		int row = (int) Math.floor(y / cellHeight);
		int col = (int) Math.floor(x / cellWidth);
		insertAt(row, col, collidable);
		
		x = position.x + offsetX + r;
		y = position.y + offsetY;
		row = (int) Math.floor(y / cellHeight);
		col = (int) Math.floor(x / cellWidth);
		insertAt(row, col, collidable);
		
		x = position.x + offsetX + r;
		y = position.y + offsetY + r;
		row = (int) Math.floor(y / cellHeight);
		col = (int) Math.floor(x / cellWidth);
		insertAt(row, col, collidable);
		
		x = position.x + offsetX;
		y = position.y + offsetY + r;
		row = (int) Math.floor(y / cellHeight);
		col = (int) Math.floor(x / cellWidth);
		insertAt(row, col, collidable);
	}
	
	public void query(float posX, float posY, ArrayList<ICollidable> list){
		float x = posX + offsetX;
		float y = posY + offsetY;
		
		int row = (int) Math.floor(y / cellHeight);
		int col = (int) Math.floor(x / cellWidth);
		
		boolean xOutOfRange = col < 0 || col >= cols;
		boolean yOutOfRange = row < 0 || row >= rows;
		
		if(xOutOfRange || yOutOfRange)
			return;
		
		ArrayList<ICollidable> cell = objects.get(row).get(col);
		list.addAll(cell);
	}
	
	public void clear(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				objects.get(i).get(j).clear();
			}
		}
	}
	
}
