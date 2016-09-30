package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.model.BulletModel;
import com.vecdef.model.Model;
import com.vecdef.model.Transform;
import com.vecdef.physics.IPhysics;
import com.vecdef.rendering.IRenderable;

public abstract class Entity implements ICollidable, IPhysics, IRenderable{
	
	//IRenderable Dependencies
	protected Transform transform;
	protected Model model;
	protected float opacity;
	protected boolean isVisible;
	protected Vector4f overrideColor;
	
	//IPhysics Dependencies
	protected Vector2f velocity;
	protected Vector2f acceleration;
	protected float angularVelocity;
	protected float torque;
	
	//ICollidable Dependencies
	protected int radius = 8;
	protected ArrayList<ContactEventListener> contactListeners;
	
	private boolean isExpired;
	protected Scene scene;
	
	public Entity(Scene scene){
		this.scene = scene;
		transform = new Transform();
		model = BulletModel.get();
		opacity = 1;
		isVisible = true;
		overrideColor = new Vector4f(1, 1, 1, 1);
		
		velocity = new Vector2f();
		acceleration = new Vector2f();
		angularVelocity = 0;
		torque = 0;
		
		contactListeners = new ArrayList<ContactEventListener>();
		
		isExpired = false;
	}
	
	public abstract void update();
	public abstract void destroy();
	
	public abstract int getEntityType();
	
	@Override
	public Transform getTransform(){
		return transform;
	}
	
	@Override
	public Model getModel(){
		return model;
	}
	
	@Override
	public float getOpacity(){
		return opacity;
	}
	
	@Override
	public void setOpacity(float opacity){
		this.opacity = opacity;
	}
	
	@Override
	public boolean isVisible(){
		return isVisible;
	}
	
	public void setVisible(boolean bDraw){
		isVisible = bDraw;
	}
	
	@Override
	public boolean useOverrideColor(){
		return false;
	}
	
	@Override
	public Vector4f getOverrideColor(){
		return overrideColor;
	}
	
	@Override
	public Vector2f getVelocity(){
		return velocity;
	}
	
	@Override
	public Vector2f getAcceleration(){
		return acceleration;
	}
	
	@Override
	public float getAngularVelocity(){
		return angularVelocity;
	}
	
	@Override
	public float getTorque(){
		return torque;
	}
	
	@Override
	public void setAngularVelocity(float av){
		this.angularVelocity = av;
	}
	
	@Override
	public void setTorque(float t){
		this.torque = t;
	}
	
	@Override
	public void addContactListener(ContactEventListener listener){
		contactListeners.add(listener);
	}
	
	@Override
	public void removeContactListener(ContactEventListener listener){
		contactListeners.remove(listener);
	}
	
	@Override
	public void onContact(ContactEvent event){
		int n = contactListeners.size();
		for(int i = 0; i < n; i++){
			ContactEventListener listener = contactListeners.get(i);
			listener.process(event);
		}
	}
	
	public boolean isExpired(){
		return isExpired;
	}
	
	public void expire(){
		isExpired = true;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public void setRadius(int r){
		this.radius = r;
	}
}