package org.barden.util;
import java.text.DecimalFormat;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL15.*;

public class GPUProfileTask {
	
	static DecimalFormat format = new DecimalFormat("#.####");
	
	protected GPUProfileTask parent;
	protected String name;
	protected ArrayList<GPUProfileTask> children;
	protected int startHandle;
	protected int endHandle;
	
	public GPUProfileTask(GPUProfileTask parent, String name){
		this.parent = parent;
		this.name = name;
		children = new ArrayList<GPUProfileTask>();
		if(parent != null)
			parent.children.add(this);
	}
	
	public void start(int query){
		this.startHandle = query;
		glQueryCounter(query, GL_TIMESTAMP);
	}
	
	public void end(int query){
		this.endHandle = query;
		glQueryCounter(query, GL_TIMESTAMP);
	}
	
	public long getStartTime(){
		return glGetQueryObjecti64(startHandle, GL_QUERY_RESULT);
	}
	
	public long getEndTime(){
		return glGetQueryObjecti64(endHandle, GL_QUERY_RESULT);
	}
	
	public long getElapsedTime(){
		return getEndTime() - getStartTime();
	}
	
	public void dump(){
		dump(0);
	}
	
	private void dump(int indent){
		for(int i = 0; i < indent; i++){
			System.out.print("    ");
		}
		
		double time = getElapsedTime() / 1000D / 1000D;
		System.out.println("[" + name + "] : " + format.format(time) + "ms");
		for(int i = 0; i < children.size(); i++){
			children.get(i).dump(indent + 1);
		}
	}
	
}