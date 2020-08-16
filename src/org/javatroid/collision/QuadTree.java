
package org.javatroid.collision;

/*
import java.util.ArrayList;

import org.javatroid.graphics.GL;
import org.javatroid.math.Vector2f;

*/

public class QuadTree {

	/*
	private static final int MAX_LEVELS = 6;
	private static final int MAX_OBJECTS = 15;
	
	private int currentLevel;
	private ArrayList<ICollidable> objects;
	private AABB bounds;
	private QuadTree[] nodes;
	
	public QuadTree(int level, AABB bounds){
		this.currentLevel = level;
		this.objects = new ArrayList<ICollidable>();
		this.bounds = bounds;
		this.nodes = new QuadTree[4];
	}
	
	public void debugDraw(){
		GL.setColor(1, 0, 0, 1);
		bounds.draw();
		for(int i = 0; i < 4; i++){
			if(nodes[i] != null){
				nodes[i].debugDraw();
			}
		}
	}
	
	public void clear(){
		objects.clear();
		
		for(int i = 0; i < 4; i++){
			if(nodes[i] != null){
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}
	
	public void split(){
		float halfWidth = bounds.getWidth() / 2;
		float halfHeight = bounds.getHeight() / 2;
		
		float x = bounds.getPosition().x;
		float y = bounds.getPosition().y;
		
		nodes[0] = new QuadTree(currentLevel + 1, new AABB(x + halfWidth / 2, y + halfHeight / 2, halfWidth, halfHeight));
		nodes[1] = new QuadTree(currentLevel + 1, new AABB(x - halfWidth / 2, y + halfHeight / 2, halfWidth, halfHeight));
		nodes[2] = new QuadTree(currentLevel + 1, new AABB(x - halfWidth / 2, y - halfHeight / 2, halfWidth, halfHeight));
		nodes[3] = new QuadTree(currentLevel + 1, new AABB(x + halfWidth / 2, y - halfHeight / 2, halfWidth, halfHeight));
	}
	
	private int getIndex(ICollidable object){
		Vector2f min = object.getAABB().min;
		Vector2f max = object.getAABB().max;
		
		int index = -1;
		
		boolean top = bounds.getPosition().y <= min.y;
		boolean bottom = bounds.getPosition().y > max.y;
		
		//Left
		if(bounds.getPosition().x >= max.x){
			if(top){
				index = 1;
			} else if(bottom){
				index = 2;
			}
		} 
		//Right
		else if(bounds.getPosition().x < min.x){
			if(top){
				index = 0;
			} else if(bottom){
				index = 3;
			}
		}
		
		return index;
		
	}
	
	public void insert(ICollidable object){
		if(nodes[0] != null){
			int index = getIndex(object);
			
			if(index != -1){
				nodes[index].insert(object);
				return;
			}
		}
		
		objects.add(object);
		
		if(objects.size() > MAX_OBJECTS && currentLevel < MAX_LEVELS){
			if(nodes[0] == null){
				split();
			}
			
			int i = 0;
			while(i < objects.size()){
				int index = getIndex(objects.get(i));
				if(index != -1){
					nodes[index].insert(objects.get(i));
					objects.remove(i);
				} else {
					i++;
				}
			}
		}
	}
	
	
	public ArrayList<ICollidable> retrieve(ArrayList<ICollidable> listOfObjects, ICollidable object){
		int index = getIndex(object);
		if(index != -1 && nodes[0] != null){
			nodes[index].retrieve(listOfObjects, object);
		}
		 
		listOfObjects.addAll(objects);
		 
		 return listOfObjects;
	}
	*/
	
}

