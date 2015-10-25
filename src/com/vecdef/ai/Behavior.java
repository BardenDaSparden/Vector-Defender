package com.vecdef.ai;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Enemy;

public abstract class Behavior{
	
	protected Scene scene;
	protected Enemy self;
	
	public Behavior(Scene scene, Enemy enemy){
		this.scene = scene;
		this.self = enemy;
	}
	
	public abstract void create();
	public abstract void update();
	public abstract void destroy();
	
}