package org.javatroid.math;

import java.nio.FloatBuffer;

public class Vector3f {

	public float x, y, z;
	
	public Vector3f(){
		this(0, 0, 0);
	}
	
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector3f clone(){
		return new Vector3f(x, y, z);
	}
	
	public Vector3f add(Vector3f v){
		return new Vector3f(x + v.x, y + v.y, z + v.z);
	}
	
	public Vector3f addi(Vector3f v){
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	
	public Vector3f sub(Vector3f v){
		return new Vector3f(x - v.x, y - v.y, z - v.z);
	}
	
	public Vector3f subi(Vector3f v){
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public Vector3f mul(Vector3f v){
		return new Vector3f(x / v.x, y / v.y, z / v.z);
	}
	
	public Vector3f muli(Vector3f v){
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	public Vector3f div(Vector3f v){
		return new Vector3f(this.x / v.x, this.y / v.y, this.z / v.z);
	}
	
	public Vector3f divi(Vector3f v){
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}
	
	public Vector3f scale(float s){
		return new Vector3f(x * s, y * s, z * s);
	}
	
	public Vector3f scalei(float s){
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	
	public Vector3f normalize(){
		float length = length();
		return new Vector3f(x / length, y / length, z / length);
	}
	
	public Vector3f normalizei(){
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		return this;
	}
	
	public Vector3f negate(){
		return new Vector3f(-x, -y, -z);
	}
	
	public Vector3f negatei(){
		x *= -1;
		y *= -1;
		z *= -1;
		return this;
	}
	
	public void set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void store(FloatBuffer buffer){
		buffer.put(x).put(y).put(z);
	}
	
	public float length(){
		return FastMath.sqrt(lengthSquared());
	}
	
	public float lengthSquared(){
		return x * x + y * y + z * z;
	}
	
	public String toString(){
		return "Vector3f ["+x+", "+y+", "+z+"]";
	}
	
	public static float dot(Vector3f A, Vector3f B){
		return (A.x * B.x + A.y * B.y + A.z * B.z);
	}
	
	public static Vector3f cross(Vector3f A, Vector3f B){
		Vector3f v = new Vector3f();
		v.x = (A.y * B.z - A.z * B.y);
		v.y = (A.z * B.x - A.x * B.z);
		v.z = (A.x * B.y - A.y * B.x);
		return v;
	}
	
}
