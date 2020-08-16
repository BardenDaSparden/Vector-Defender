package org.javatroid.particle;

import java.util.ArrayList;

import org.javatroid.graphics.SpriteBatch;

public class ParticleSystem {

	public ArrayList<Emitter> emitters = new ArrayList<Emitter>();
	
	public ParticleSystem(){
		
	}
	
	public void update(){
		for(int i = 0; i < emitters.size(); i++){
			Emitter emitter = emitters.get(i);
			if(emitter.isDead())
				removeEmitter(i);
			else
				emitter.update();
		}
	}
	
	public void draw(SpriteBatch renderer){
		for(int i = 0; i < emitters.size(); i++){
			Emitter emitter = emitters.get(i);
			emitter.draw(renderer);
		}
	}
	
	public Emitter addEmitter(Emitter emitter){
		emitters.add(emitter);
		return emitter;
	}
	
	public void removeEmitter(Emitter emitter){
		removeEmitter(emitters.indexOf(emitter));
	}
	
	private void removeEmitter(int i){
		emitters.remove(i);
	}
	
	public void clear(){
		emitters.clear();
	}
	
}
