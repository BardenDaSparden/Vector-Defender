package com.vecdef.audio;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class MusicPlayer {
	
	Minim minim;
	final int bufferSize = 1024;
	final int sampleRate = 44100;
	ArrayList<String> trackFilenames;
	ArrayList<AudioPlayer> trackList;
	ArrayList<TrackEventListener> listeners;
	int trackIdx = 0;
	
	public MusicPlayer(Minim minim){
		this.minim = minim;
		
		trackFilenames = new ArrayList<String>();
		trackList = new ArrayList<AudioPlayer>();
		listeners = new ArrayList<TrackEventListener>();
		
		InputStream stream = ClassLoader.getSystemResourceAsStream("music/music.def");
		Scanner scanner = new Scanner(stream);
		while(scanner.hasNext()){
			String track = scanner.nextLine().trim();
			trackFilenames.add(track);
		}
		scanner.close();
		
		int n = trackFilenames.size();
		for(int i = 0; i < n; i++){
			String filename = trackFilenames.get(i);
			AudioPlayer track = minim.loadFile(filename);
			trackList.add(track);
		}
		
		AudioPlayer player = trackList.get(0);
		player.play();
	}
	
	public void previousTrack(){
		AudioPlayer track = trackList.get(trackIdx);
		track.pause();
		track.rewind();
		
		if(trackIdx == 0)
			trackIdx = trackList.size() - 1;
		else
			trackIdx--;
		
		track = trackList.get(trackIdx);
		track.play();
		
		onTrackChange(new TrackChangeEvent(track));
	}
	
	public void nextTrack(){
		AudioPlayer track = trackList.get(trackIdx);
		track.pause();
		track.rewind();
		
		trackIdx = (trackIdx + 1) % trackList.size();
		track = trackList.get(trackIdx);
		track.play();
		
		onTrackChange(new TrackChangeEvent(track));
	}
	
	public void poll(){
		AudioPlayer track = trackList.get(trackIdx);
		if(track == null)
			return;
		if(!track.isPlaying()){
			nextTrack();
		}
	}
	
	public AudioPlayer getCurrentTrack(){
		return trackList.get(trackIdx);
	}
	
	public int getBufferSize(){
		return bufferSize;
	}
	
	public int getSampleRate(){
		return sampleRate;
	}
	
	void onTrackChange(TrackChangeEvent event){
		int n = listeners.size();
		for(int i = 0; i < n; i++){
			TrackEventListener listener = listeners.get(i);
			listener.process(event);
		}
	}
	
	public void addListener(TrackEventListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(TrackEventListener listener){
		listeners.remove(listener);
	}
	
	public void destroy(){
		int n = trackList.size();
		for(int i = 0; i < n; i++){
			AudioPlayer player = trackList.get(i);
			player.close();
		}
	}
	
}
