package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

import com.vecdef.gamestate.Scene;
import com.vecdef.model.DefaultMesh;
import com.vecdef.model.Mesh;
import com.vecdef.model.Transform2D;

public abstract class Entity implements ICollidable, IPhysics, IRenderable{
	
	//IRenderable Dependencies
	protected Transform2D transform;
	protected Mesh mesh;
	protected float opacity;
	
	//IPhysics Dependencies
	protected Vector2f velocity;
	protected Vector2f acceleration;
	protected float angularVelocity;
	protected float torque;
	
	//ICollidable Dependencies
	protected ArrayList<ContactEventListener> contactListeners;
	
	protected boolean bExpired = false;
	protected static Scene scene = null;
	
	public Entity(){
		transform = new Transform2D();
		mesh = new DefaultMesh();
		opacity = 1;
		
		velocity = new Vector2f();
		acceleration = new Vector2f();
		angularVelocity = 0;
		torque = 0;
		
		contactListeners = new ArrayList<ContactEventListener>();
	}
	
	public abstract void update(Grid grid);
	public abstract void destroy();
	public abstract int getEntityType();
	
	@Override
	public Mesh getMesh(){
		return mesh;
	}
	
	@Override
	public Transform2D getTransform(){
		return transform;
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
		return bExpired;
	}
	
	public static void setScene(Scene scene){
		Entity.scene = scene;
	}
	
	public static Scene getScene(){
		return scene;
	}
	
}