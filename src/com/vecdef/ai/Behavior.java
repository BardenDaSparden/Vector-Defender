package com.vecdef.ai;

import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;

public interface Behavior{
	
	public void onUpdate(Entity object, Grid grid);
	public void onCollision(Entity object, Entity other);
	public void onDestroy(Entity object);
	
}