package com.vecdef.audio;

import ddf.minim.AudioPlayer;

public class TrackChangeEvent {

	public AudioPlayer track;
	
	public TrackChangeEvent(AudioPlayer track){
		this.track = track;
	}
	
}
