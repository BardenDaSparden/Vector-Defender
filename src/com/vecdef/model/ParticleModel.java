package com.vecdef.model;

public class ParticleModel extends Model {

	private ParticleModel(){
		
	}
	
	static ParticleModel instance = null;
	
	public static ParticleModel get(){
		if(instance == null)
			instance = new ParticleModel();
		return instance;
	}
	
}
