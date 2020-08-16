package com.vecdef.objects;

import java.util.ArrayList;

public abstract class ObjectPool {

	int numObjects;
	ArrayList<IPoolable> objects;
	ArrayList<Integer> recycledIdx;
	int nextIdx = 0;
	int numUsed = 0;
	
	public ObjectPool(int numObjects){
		this.numObjects = numObjects;
		objects = new ArrayList<IPoolable>();
		recycledIdx = new ArrayList<Integer>();
	}
	
	int findNextIdx(){
		if(recycledIdx.size() > 0){
			nextIdx = recycledIdx.get(recycledIdx.size() - 1);
			recycledIdx.remove(recycledIdx.size() - 1);
		} else {
			for(int i = 0; i < numObjects; i++){
				IPoolable obj = objects.get(i);
				if(obj.isRecycled()){
					nextIdx = i;
					break;
				}
			}
		}
		return nextIdx;
	}
	
	public boolean hasAvailable(){
		return numUsed + 1 < numObjects;
	}
	
	public IPoolable getNext(){
		IPoolable obj = objects.get(nextIdx);
		obj.reuse();
		numUsed++;
		nextIdx = findNextIdx();
		return obj;
	}
	
	public void recycle(IPoolable object){
		if(objects.contains(object)){
			int idx = objects.indexOf(object);
			recycledIdx.add(idx);
			object.recycle();
			numUsed--;
		}
	}
}