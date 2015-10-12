package com.vecdef.objects;

import org.javatroid.math.Vector2f;

import com.vecdef.gamestate.Scene;
import com.vecdef.model.DefaultMesh;
import com.vecdef.model.Mesh;
import com.vecdef.model.Transform2D;

public abstract class Entity implements IRenderable, IPhysics{
	
	//Render Dependencies
	protected Transform2D transform;
	protected Mesh mesh;
	protected float opacity;
	
	//Physics Dependencies
	protected Vector2f velocity = new Vector2f();
	protected Vector2f acceleration = new Vector2f();
	protected float angularVelocity = 0;
	protected float torque = 0;
	public float radius = 20;
	
	protected boolean bExpired = false;
	
	protected static Scene scene = null;
	
	public Entity(){
		transform = new Transform2D();
		mesh = new DefaultMesh();
		opacity = 1;
	}
	
	public abstract void update(Grid grid, float dt);
	public abstract void collision(Entity other);
	public abstract void destroy();
	
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
	
	public Vector2f getVelocity()		{return velocity;}
	public Vector2f getAcceleration()	{return acceleration;}
	public float getAngularVelocity()	{return angularVelocity;}
	public float getTorque()			{return torque;}
	
	public void setVelocity(Vector2f velocity){
		this.velocity = velocity;
	}
	
	public void setAcceleration(Vector2f acceleration){
		this.acceleration = acceleration;
	}
	
	public void setAngularVelocity(float av){
		this.angularVelocity = av;
	}
	
	public void setTorque(float t){
		this.torque = t;
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