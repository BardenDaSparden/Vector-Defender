package com.vecdef.objects;

public class ContactEvent {

	public ICollidable self;
	public ICollidable other;
	
	public ContactEvent(ICollidable objectA, ICollidable objectB){
		this.self = objectA;
		this.other = objectB;
	}
	
}
