package com.vecdef.ai;

import com.vecdef.objects.Enemy;
import com.vecdef.objects.Scene;

public abstract class Behaviour{
	
	protected Scene scene;
	protected Enemy self;
	
	public Behaviour(Scene scene, Enemy enemy){
		this.scene = scene;
		this.self = enemy;
	}
	
	public abstract void create();
	public abstract void update();
	public abstract void destroy();
	
}