package org.javatroid.particle;

import java.util.ArrayList;

import org.javatroid.graphics.SpriteBatch;

public class ParticleScene {

	private ArrayList<ParticleSystem> systems = new ArrayList<ParticleSystem>();
	
	public void update(){
		for(int i = 0; i < systems.size(); i++){
			ParticleSystem system = systems.get(i);
			system.update();
		}
	}
	
	public void draw(SpriteBatch renderer){
		for(int i = 0; i < systems.size(); i++){
			ParticleSystem system = systems.get(i);
			system.draw(renderer);
		}
	}
	
	public void addSystem(ParticleSystem system){
		systems.add(system);
	}
	
	public void removeSystem(ParticleSystem system){
		systems.remove(system);
	}
	
	public void clear(){
		systems.clear();
	}
	
	public ArrayList<Particle> getAllParticles(){
		ArrayList<Particle> particles = new ArrayList<Particle>();
		
		for(ParticleSystem system : systems){
			for(Emitter emitter : system.emitters){
				particles.addAll(emitter.particles);
			}
		}
		
		return particles;
	}
	
}
