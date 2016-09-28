package com.vecdef.collision;

import com.vecdef.model.Transform;

public interface ICollidable {

	public Transform getTransform();
	public int getRadius();
	public void setRadius(int radius);
	public void addContactListener(ContactEventListener listener);
	public void removeContactListener(ContactEventListener listener);
	public void onContact(ContactEvent event);
	public int getGroupMask();
	public int getCollisionMask();
	
}
