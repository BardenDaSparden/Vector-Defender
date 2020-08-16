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

public abstract class Entity implements ICollidable, IPoolable, IPhysics, IRenderable {
	
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
	
	//IPoolable
	protected boolean isUsable;
	
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
		
		isUsable = true;
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
	public void reuse(){
		isUsable = false;
	}
	
	@Override
	public void recycle(){
		isUsable = true;
	}
	
	@Override
	public boolean isRecycled(){
		return isUsable;
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
		for(int i = 0; i < contactListeners.size(); i++){
			ContactEventListener listener = contactListeners.get(i);
			listener.process(event);
		}
	}
	
	@Override
	public int getRadius(){
		return radius;
	}
	
	@Override
	public void setRadius(int r){
		this.radius = r;
	}
}