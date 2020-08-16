package org.javatroid.audio;

import java.nio.FloatBuffer;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

public class AudioPlayer {

	private FloatBuffer listenerPosition;
	private FloatBuffer listenerVelocity;
	private FloatBuffer listenerOrientation;
	private FloatBuffer sourcePosition;
	
	private boolean updateListener = false;
	private float volume;
	
	private static AudioPlayer instance = null;
	
	private AudioPlayer(){		
		listenerPosition = BufferUtils.createFloatBuffer(3);
		listenerPosition.put(new float[] {0, 0, 0});
		listenerPosition.flip();
		
		listenerVelocity = BufferUtils.createFloatBuffer(3);
		listenerVelocity.put(new float[] {0, 0, 0});
		listenerVelocity.flip();
		
		listenerOrientation = BufferUtils.createFloatBuffer(6);
		listenerOrientation.put(new float[]{0, 0, 0, 0, 1, 0});
		listenerOrientation.flip();
		
		sourcePosition = BufferUtils.createFloatBuffer(3);
		sourcePosition.put(new float[] {0, 1, 0});
		sourcePosition.flip();
		
		this.volume = 1.0f;
		
		updateListener();
		
		alDistanceModel(AL_LINEAR_DISTANCE);
		
	}
	
	public static AudioPlayer instance(){
		if(instance == null){
			instance = new AudioPlayer();
		}
		
		return instance;
	}
	
	public void play(Sound sound){
		if(updateListener){
			updateListener();
		}
		
		sourcePosition.put(0, sound.getPanning());
		
		alSourcef(sound.getSourceHandle(), AL_PITCH, sound.getPitch());
		alSourcef(sound.getSourceHandle(), AL_GAIN, sound.getGain() * volume);
		//alSource(sound.getSourceHandle(), AL_POSITION, sourcePosition);
		
		alSourcePlay(sound.getSourceHandle());
		
	}
	
	public void stop(Sound sound){
		alSourceStop(sound.getSourceHandle());
	}
	
	public void pause(Sound sound){
		alSourcePause(sound.getSourceHandle());
	}
	
	
	private void updateListener(){
		
		//alListener(AL_POSITION, listenerPosition);
		//alListener(AL_VELOCITY, listenerVelocity);
		//alListener(AL_ORIENTATION, listenerOrientation);
		updateListener = false;
		
	}
	
	public void setListenerPosition(Vector2f position){
		setListenerPosition(new Vector3f(position.x, -1, position.y));
	}
	
	public void setListenerPosition(Vector3f position){
		listenerPosition.put(0, position.x);
		listenerPosition.put(1, position.y);
		listenerPosition.put(2, position.z);
		
		listenerOrientation.put(0, position.x);
		listenerOrientation.put(1, position.y);
		listenerOrientation.put(2, position.z);
		
		updateListener = true;
	}
	
	public void setListenerVelocity(Vector3f velocity){
		listenerVelocity.put(0, velocity.x);
		listenerVelocity.put(1, velocity.y);
		listenerVelocity.put(2, velocity.z);
		
		updateListener = true;
	}
	
	public void setListenerDirection(Vector2f direction){
		setListenerDirection(new Vector3f(direction.x, 1, direction.y));
	}
	
	public void setListenerDirection(Vector3f direction){
		listenerOrientation.put(3, direction.x);
		listenerOrientation.put(4, direction.y);
		listenerOrientation.put(5, direction.z);
		
		updateListener = true;
	}
	
	public void setVolume(float volume){
		this.volume = volume;
	}
	
}
