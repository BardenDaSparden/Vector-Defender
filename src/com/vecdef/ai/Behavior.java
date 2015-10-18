package com.vecdef.ai;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Entity;

public abstract class Behavior{
	
	Scene scene;
	
	public Behavior(Scene scene){
		this.scene = scene;
	}
	
	public abstract void create(Entity self);
	public abstract void update(Entity self);
	public abstract void destroy(Entity self);
	
}