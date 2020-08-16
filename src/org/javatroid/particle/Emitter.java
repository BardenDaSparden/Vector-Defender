package org.javatroid.particle;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

public class Emitter {

	ArrayList<EmitterBehaviour> emitterBehaviours = new ArrayList<EmitterBehaviour>();
	ArrayList<ParticleBehaviour> particleBehaviours = new ArrayList<ParticleBehaviour>();
	
	ArrayList<Particle> particles = new ArrayList<Particle>();
	
	Particle particleType;
	
	private float x, y;
	private float width, height;
	
	private int emissionDelay;
	int delay = 0;
	
	boolean isEnabled = true;
	boolean isDead = false;
	boolean additive = true;
	
	public Emitter(float x, float y, float width, float height, int emissionDelay){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.emissionDelay = emissionDelay;
		this.delay = emissionDelay;
	}
	
	public void emit(){
		for(int i = 0; i < emitterBehaviours.size(); i++){
			EmitterBehaviour behaviour = emitterBehaviours.get(i);
			behaviour.update(this);
		}
	}
	
	public void update(){
		
		if(isEnabled){
			if(canEmit()){
				emit();
				delay = emissionDelay;
			} else {
				delay--;
			}
		}
		
		for(int i = 0; i < particles.size(); i++){
			Particle particle = particles.get(i);
			
			if(particle.isDead()){
				particles.remove(i);
				continue;
			}
			
			particle.update();
			
			for(int j = 0; j < particleBehaviours.size(); j++){
				ParticleBehaviour behaviour = particleBehaviours.get(j);
				behaviour.update(particle);
			}
			
		}
		
	}
	
	public void draw(SpriteBatch renderer){
		
		BlendState state = (additive) ? BlendState.ADDITIVE : BlendState.ALPHA;
		renderer.begin(state);
		
		for(int i = 0; i < particles.size(); i++){
			Particle particle = particles.get(i);
			particle.draw(renderer);
		}
		renderer.end();
	}
	
	public void setPosition(float x, float y){
		setPosition(new Vector2f(x, y));
	}
	
	public void setPosition(Vector2f position){
		this.x = position.x;
		this.y = position.y;
	}
	
	public void setEmissionDelay(int emissionDelay){
		this.emissionDelay = emissionDelay;
	}
	
	public float nextPositionX(){
		return x + FastMath.random() * width;
	}
	
	public float nextPositionY(){
		return y + FastMath.random() * height;
	}
	
	public Particle getParticleType(){
		return particleType;
	}
	
	public void setParticleType(Particle particle){
		this.particleType = particle;
	}
	
	public void addParticle(Particle particle){
		particles.add(particle);
	}
	
	public void addEmitterBehaviour(EmitterBehaviour behaviour){
		emitterBehaviours.add(behaviour);
	}
	
	public void addParticleBehaviour(ParticleBehaviour behaviour){
		particleBehaviours.add(behaviour);
	}
	
	public void clearBehaviours(){
		emitterBehaviours.clear();
		particleBehaviours.clear();
	}
	
	public void clearParticles(){
		particles.clear();
	}
	
	public void clear(){
		clearBehaviours();
		clearParticles();
	}
	
	public void enable(){
		isEnabled = true;
	}
	
	public void disable(){
		isEnabled = false;
	}
	
	public void destroy(){
		isDead = true;
	}
	
	public boolean isDead(){
		return isDead && particles.size() == 0;
	}
	
	public void setAdditive(boolean value){
		this.additive = value;
	}
	
	public boolean canEmit(){
		
		if(particleType == null){
			throw new IllegalArgumentException("Emitter must call setParticleType( Particle type )");
		}
		
		return delay <= 0;
	}
	
}
