package org.javatroid.audio;

import org.javatroid.math.FastMath;
import static org.lwjgl.openal.AL10.*;

public class Sound {

	private final int sourceHandle;
	private final int bufferHandle;
	private float pitch;
	private float gain;
	private float panning;
	
	public Sound(int source, int buffer){
		this.bufferHandle = buffer;
		this.sourceHandle = source;
		this.pitch = 1.0f;
		this.gain = 1.0f;
		this.panning = 0.0f;
	}
	
	public float getPitch(){
		return pitch;
	}
	
	public void setPitch(float pitch){
		pitch = FastMath.clamp(0.0f, 1.0f, pitch);
		this.pitch = pitch;
	}
	
	public float getGain(){
		return gain;
	}
	
	public void setGain(float gain){
		gain = FastMath.clamp(0.0f, 1.0f, gain);
		this.gain = gain;
	}
	
	public float getPanning(){
		return panning;
	}
	
	public void setPanning(float panning){
		panning = FastMath.clamp(-1.0f, 1.0f, panning);
		this.panning = panning;
	}
	
	public void setLooping(boolean value){
		int b = (value) ? 1 : 0;
		alSourcef(sourceHandle, AL_LOOPING, b);
	}
	
	public int getSourceHandle(){
		return sourceHandle;
	}
	
	public int getBufferHandle(){
		return bufferHandle;
	}
}
