package com.vecdef.objects;

import org.javatroid.audio.AudioPlayer;
import org.javatroid.audio.Sound;
import org.javatroid.core.Resources;
import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;

public class HUD {

	Scene scene;
	
	protected boolean bDrawCountdown = false;
	final int FRAMES_PER_SECOND = 60;
	final int COUNTDOWN_TIME = FRAMES_PER_SECOND;
	Timer countdownTimer = new Timer(COUNTDOWN_TIME);
	protected int countdownTime = 4;
	protected String countdownStr = "";
	Sound countdownBeep;
	Sound countdownComplete;
	
	public HUD(Scene scene){
		this.scene = scene;
		
		AudioPlayer player = AudioPlayer.instance();
		countdownTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				countdownTime--;
				if(countdownTime != 0){
					countdownStr = countdownTime - 1 + "";
					countdownTimer.restart();
					player.play(countdownBeep);
				}
				if(countdownTime == 1){
					countdownStr = "Begin";
					player.play(countdownComplete);
				}
				
				if(countdownTime == 0){
					bDrawCountdown = false;
				}
			}
		});
		countdownTimer.start();
		countdownStr = countdownTime - 1 + "";
		countdownBeep = Resources.getSound("CountdownBeep");
		countdownComplete = Resources.getSound("CountdownComplete");
		player.play(countdownBeep);
	}
	
	public void update(){
		countdownTimer.tick();
	}
	
	public void onStateStart(){
		bDrawCountdown = true;
	}
	
	public boolean isCountdownDrawn(){
		return bDrawCountdown;
	}
	
	public String getCountdownValue(){
		return countdownStr;
	}
	
	public float countdownOpacity(){
		return countdownTimer.percentComplete();
	}
	
	public boolean isCountdownComplete(){
		return !bDrawCountdown;
	}
	
	public Scene getScene(){
		return scene;
	}
	
}