package org.javatroid.core;

public class Timer {

	int duration;
	int time = 0;
	boolean doTick = false;
	
	TimerCallback callback;
	
	public Timer(int duration){
		this.duration = duration;
		
		setCallback(new TimerCallback() {
			
			public void execute(Timer timer) {
				System.out.println("From default timer callback. Timer complete");
			}
		});
		
	}
	
	public void tick(){
		if(doTick){
			time++;
			if(time == duration){
				doTick = false;
				callback.execute(this);
			}
		}
	}
	
	public void setCallback(TimerCallback callback){
		this.callback = callback;
	}
	
	public void start(){
		doTick = true;
	}
	
	public void restart(){
		time = 0;
		doTick = true;
	}
	
	public boolean isComplete(){
		return time >= duration;
	}
	
	public boolean hasStarted(){
		return doTick;
	}
	
	public float percentComplete(){
		return ((float)time) / ((float)duration);
	}
	
	public void reset(){
		doTick = false;
		time = 0;
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}
	
}
