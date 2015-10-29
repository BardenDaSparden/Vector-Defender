package com.vecdef.gamestate;

import java.util.ArrayList;

import org.javatroid.core.Input;

import com.toolkit.inputstate.Gamepad;

public class GameState {

	protected Input input;
	protected Gamepad gamepad;
	ArrayList<GState> allStates;
	int idx;
	
	public GameState(Input input, Gamepad gamepad){
		this.input = input;
		this.gamepad = gamepad;
		allStates = new ArrayList<GState>();
		idx = 0;
	}
	
	public void initialize(){
		allStates.get(idx).initialize();
	}
	
	public void update(){
		allStates.get(idx).update();
	}
	
	public void draw(){
		allStates.get(idx).draw();
	}
	
	public void destroy(){
		GState state = allStates.get(idx);
		state.destroy();
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
