package debug.physics;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

public class LooseGrid<E extends RigidBody>{
	
	private ArrayList<ArrayList<E>> elements;
	private AABB[] bounds;
	private int cols, rows;
	
	public LooseGrid(int cellWidth, int cellHeight, int screenWidth, int screenHeight){
		
		boolean colRemainder = (screenWidth % cellWidth != 0) ? true : false;
		boolean rowRemainder = (screenHeight % cellHeight != 0) ? true : false;
		
		if(colRemainder)
			this.cols = (screenWidth / cellWidth) + 1;
		else
			this.cols = (screenWidth / cellWidth);
		
		if(rowRemainder)
			this.rows = (screenHeight / cellHeight) + 1;
		else
			this.rows = (screenHeight / cellHeight);
		
		this.elements = new ArrayList<ArrayList<E>>();
		
		for(int i = 0; i < rows * cols; i++){
			elements.add(new ArrayList<E>());
		}
		
		this.bounds = new AABB[rows * cols];
		
		//Top Left of screen in pixels
		float startX = -screenWidth / 2 + cellWidth / 2;
		float startY = screenHeight / 2 - cellHeight / 2;
		
		for(int i = 0; i < bounds.length; i++){
			int ax = i % cols;
			int ay = i / cols;
			
			AABB box = new AABB(startX + (cellWidth * ax), startY - (cellHeight * ay), cellWidth, cellHeight);
			bounds[i] = box;
		}
	}
	
//	public void draw(Renderer2D renderer){
//			for(int i = 0; i < bounds.length; i++){
//				if(elements.get(i).size() > 0){
//					AABB box = bounds[i];
//					Vector2f min = box.getMin();
//					Vector2f max = box.getMax();
//					
//					Vertex[] vertices = new Vertex[4];
//					vertices[0] = new Vertex(new org.javatroid.math.Vector2f(min.x, min.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 0, 1));
//					vertices[1] = new Vertex(new org.javatroid.math.Vector2f(max.x, min.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 0, 1));
//					vertices[2] = new Vertex(new org.javatroid.math.Vector2f(max.x, max.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 0, 1));
//					vertices[3] = new Vertex(new org.javatroid.math.Vector2f(min.x, max.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 0, 1));
//					
//					renderer.begin(DrawMode.LINE_LOOP);
//					renderer.draw(vertices);
//					renderer.end();
//				}
//			}
//		
//	}
	
	public void get(AABB bound, ArrayList<E> list){
		for(int i = 0; i < bounds.length; i++){
			AABB box = bounds[i];
			
			if(!box.intersects(bound))
				continue;
				
			ArrayList<E> objects = elements.get(i);
			list.addAll(objects);
			
		}
	}
	
	public void insert(E object){
		Vector2f position = object.getPosition();
		
		for(int i = 0; i < bounds.length; i++){
			
			AABB box = bounds[i];
			
			if(!box.contains(position))
				continue;
			
			
			elements.get(i).add(object);
			return;
			
		}
	}
	
	public void clear(){
		for(int i = 0; i < elements.size(); i++){
			ArrayList<E> list = elements.get(i);
			list.clear();
		}
	}
	
	public String toString(){
		
		String s = "";
		
		for(int i = 0; i < elements.size(); i++){
			s += i + " = " + elements.get(i).size() + "\n";
		}
		
		return s;
		
	}
	
}
