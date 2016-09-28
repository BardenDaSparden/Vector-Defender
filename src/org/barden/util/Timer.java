package org.barden.util;

public class Timer {

	int maxTick;
	int ticks;
	TimerCallback callback;
	
	public Timer(int tickLength){
		this.maxTick = tickLength;
	}
	
	public void tick(){
		if(ticks == maxTick){
			if(callback != null)
				callback.onTimerComplete();
		} else if(ticks < maxTick){
			ticks++;
		}
	}
	
	public boolean isStarted(){
		return ticks > 0;
	}
	
	public void reset(){
		ticks = 0;
	}
	
	public float percent(){
		return (float)ticks / (float)maxTick;
	}
	
	public void onComplete(TimerCallback callback){
		this.callback = callback;
	}
	
}
