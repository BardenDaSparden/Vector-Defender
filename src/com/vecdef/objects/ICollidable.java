package com.vecdef.objects;

import com.vecdef.model.Transform2D;

public interface ICollidable {

	public Transform2D getTransform();
	public int getRadius();
	public void addContactListener(ContactEventListener event);
	public void onContact(ContactEvent event);
	public int getGroupMask();
	public int getCollisionMask();
	
}
