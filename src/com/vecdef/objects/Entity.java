package com.vecdef.objects;

import org.javatroid.math.Vector2f;

import com.vecdef.gamestate.Scene;
import com.vecdef.model.DefaultMesh;
import com.vecdef.model.Mesh;
import com.vecdef.model.Transform2D;

public abstract class Entity implements IRenderable, IPhysics{
	
	//Render Dependencies
	public Transform2D transform = new Transform2D();
	public Mesh mesh = new DefaultMesh();
	public boolean isDrawn = true;
	public float opacity = 1;
	
	//Physics Dependencies
	protected Vector2f velocity = new Vector2f();
	protected Vector2f acceleration = new Vector2f();
	protected float angularVelocity = 0;
	protected float torque = 0;
	public float radius = 20;
	
	protected boolean bExpired = false;
	
	protected static Scene scene = null;
	
	public abstract void update(Grid grid, float dt);
	public abstract void collision(Entity other);
	public abstract void destroy();
	
	public Mesh getMesh()				{return mesh;}
	public Transform2D getTransform()	{return transform;}
	public boolean isDrawn()			{return isDrawn;}
	public float getOpacity() 			{return opacity;}
	
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