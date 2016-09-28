package org.barden.util;

import static org.lwjgl.opengl.GL15.*;

public class GPUProfiler {

	static final boolean PROFILING_ENABLED = true;
	static GPUProfiler instance = null;
	
	private int[] queries;
	private int queryIdx;
	private GPUProfileTask currentTask;
	private GPUProfileTask completedFrame;
	private int frameCounter;
	
	private GPUProfiler(){
		currentTask = null;
		completedFrame = null;
		frameCounter = 0;
		queries = new int[100];
		for(int i = 0; i < 100; i++){
			queries[i] = glGenQueries();
		}
		queryIdx = 0;
	}
	
	public static GPUProfiler get(){
		if(instance == null)
			instance = new GPUProfiler();
		return instance;
	}
	
	public void beginFrame(){
		if(!PROFILING_ENABLED)
			return;
		
		currentTask = new GPUProfileTask(null, "Frame: " + ++frameCounter);
		currentTask.start(getQuery());
	}
	
	public void endFrame(){
		if(!PROFILING_ENABLED)
			return;
		
		currentTask.end(getQuery());
		completedFrame = currentTask;
		queryIdx = 0;
	}
	
	public void startTask(String taskName){
		if(!PROFILING_ENABLED)
			return;
		
		currentTask = new GPUProfileTask(currentTask, taskName);
		currentTask.start(getQuery());
	}
	
	public void endTask(){
		if(!PROFILING_ENABLED)
			return;
		
		currentTask.end(getQuery());
		currentTask = currentTask.parent;
	}
	
	private int getQuery(){
		return queries[queryIdx++];
	}
	
	public GPUProfileTask getResults(){
		return completedFrame;
	}
	
}