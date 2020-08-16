package org.javatroid.math;

import java.nio.FloatBuffer;

public class Vector4f {

	public float x, y, z, w;
	
	public Vector4f(){
		this(0, 0, 0, 0);
	}
	
	public Vector4f(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(Vector4f v){
		this(v.x, v.y, v.z, v.w);
	}
	
	public Vector4f clone(){
		return new Vector4f(x, y, z, w);
	}
	
	public Vector4f add(Vector4f v){
		return new Vector4f(x + v.x, y + v.y, z + v.z, w + v.w);
	}
	
	public Vector4f addi(Vector4f v){
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
		return this;
	}
	
	public Vector4f sub(Vector4f v){
		return new Vector4f(x - v.x, y - v.y, z - v.z, w - v.w);
	}
	
	public Vector4f subi(Vector4f v){
		x -= v.x;
		y -= v.y;
		z -= v.z;
		w -= v.w;
		return this;
	}
	
	public Vector4f mul(Vector4f v){
		return new Vector4f(x * v.x, y * v.y, z * v.z, w * v.w);
	}
	
	public Vector4f muli(Vector4f v){
		x *= v.x;
		y *= v.y;
		z *= v.z;
		w *= v.w;
		return this;
	}
	
	public Vector4f div(Vector4f v){
		return new Vector4f(x / v.x, y / v.y, z / v.z, w / v.w);
	}
	
	public Vector4f divi(Vector4f v){
		x /= v.x;
		y /= v.y;
		z /= v.z;
		w /= v.w;
		return this;
	}
	
	public Vector4f scale(float s){
		return new Vector4f(x * s, y * s, z * s, w * s);
	}
	
	public Vector4f scalei(float s){
		x *= s;
		y *= s;
		z *= s;
		w *= s;
		return this;
	}
	
	public Vector4f normalize(){
		float length = length();
		return new Vector4f(x / length, y / length, z / length, w / length);
	}
	
	public Vector4f normalizei(){
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		return this;
	}
	
	public Vector4f negate(){
		return new Vector4f(-x, -y, -z, -w);
	}
	
	public Vector4f negatei(){
		x *= -1;
		y *= -1;
		z *= -1;
		w *= -1;
		return this;
	}
	
	public void set(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set(Vector4f v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}
	
	public void store(FloatBuffer buffer){
		buffer.put(x).put(y).put(z).put(w);
	}
	
	public float lengthSquared(){
		return (x * x + y * y + z * z + w * w);
	}
	
	public float length(){
		return FastMath.sqrt(lengthSquared());
	}
	
	public String toString(){
		return "Vector4f [" + x + ", " + y + ", " + z + ", " + w + "]";
	}
	
}
