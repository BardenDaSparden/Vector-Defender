package com.vecdef.gamestate;

import java.util.ArrayList;

import org.javatroid.core.Input;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.audio.AudioAnalyzer;
import com.vecdef.audio.MusicPlayer;
import com.vecdef.core.MinimFileHandler;

import ddf.minim.Minim;

public class GameState {

	protected Minim minim;
	protected MusicPlayer player;
	protected AudioAnalyzer analyzer;
	protected Input input;
	protected Gamepad gamepad;
	
	ArrayList<GState> allStates;
	int idx;
	
	public GameState(){
		MinimFileHandler fHandler = new MinimFileHandler();
		minim = new Minim(fHandler);
		
		player = new MusicPlayer(minim);
		analyzer = new AudioAnalyzer(player);
		
		input = new Input();
		input.setMouseGrabbed(true);
		
		gamepad = new Gamepad();
		
		allStates = new ArrayList<GState>();
		idx = 0;
	}
	
	public void initialize(){
		allStates.get(idx).initialize();
	}
	
	public void update(){
		if(gamepad.isButtonPressed(Gamepad.DPAD_RIGHT_BUTTON)){
			player.nextTrack();
		}
		
		if(gamepad.isButtonPressed(Gamepad.DPAD_LEFT_BUTTON)){
			player.previousTrack();
		}
		
		input.poll();
		gamepad.poll();
		player.poll();
		
		allStates.get(idx).update();
	}
	
	public void draw(){
		allStates.get(idx).draw();
	}
	
	public void destroy(){
		GState state = allStates.get(idx);
		state.destroy();
		player.destroy();
	}
	
	public void next(){
		GState state = allStates.get(idx);
		state.destroy();
		idx = (idx + 1) % allStates.size();
		state = allStates.get(idx);
		state.initialize();
	}
	
	public void previous(){
		GState state = allStates.get(idx);
		state.destroy();
		if(idx > 0)
			idx--;
		else
			idx = allStates.size() - 1;
		state = allStates.get(idx);
		state.initialize();
	}
	
	public void registerGState(GState state){
		allStates.add(state);
	}
}
