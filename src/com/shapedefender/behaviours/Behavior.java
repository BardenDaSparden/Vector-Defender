package com.shapedefender.behaviours;

import com.shapedefender.objects.Entity;
import com.shapedefender.objects.Grid;

public interface Behavior{
	
	public void onUpdate(Entity object, Grid grid, float dt);
	public void onCollision(Entity object, Entity other);
	public void onDestroy(Entity object);
	
}