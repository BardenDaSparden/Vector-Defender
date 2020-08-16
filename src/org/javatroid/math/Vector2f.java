package org.javatroid.math;

import java.nio.FloatBuffer;

public class Vector2f{
	
	public float x, y;
	
	public Vector2f(){
		this(0, 0);
	}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f v){
		x = v.x;
		y = v.y;
	}
	
	public Vector2f clone(){
		return new Vector2f(x, y);
	}
	
	public Vector2f add(Vector2f v){
		return new Vector2f(x + v.x, y + v.y);
	}
	
	public Vector2f addi(Vector2f v){
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vector2f sub(Vector2f v){
		return new Vector2f(x - v.x, y - v.y);
	}
	
	public Vector2f subi(Vector2f v){
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	public Vector2f mul(Vector2f v){
		return new Vector2f(x * v.x, y * v.y);
	}
	
	public Vector2f muli(Vector2f v){
		x *= v.x;
		y *= v.y;
		return this;
	}
	
	public Vector2f div(Vector2f v){
		return new Vector2f(x / v.x, y / v.y);
	}
	
	public Vector2f divi(Vector2f v){
		x /= v.x;
		y /= v.y;
		return this;
	}
	
	public Vector2f scale(float s){
		return new Vector2f(x * s, y * s);
	}
	
	public Vector2f scalei(float s){
		x *= s;
		y *= s;
		return this;
	}
	
	public Vector2f normalize(){
		float length = length();
		return new Vector2f(x / length, y / length);
	}
	
	public Vector2f normalizei(){
		float length = length();
		x /= length;
		y /= length;
		return this;
	}
	
	public Vector2f negate(){
		float _x = -x;
		float _y = -y;
		return new Vector2f(_x, _y);
	}
	
	public Vector2f negatei(){
		x *= -1;
		y *= -1;
		return this;
	}
	
	public Vector2f rotate(float angle){
		float c = FastMath.cosd(angle);
		float s = FastMath.sind(angle);
		float _x = c * x - s * y;
		float _y = s * x + c * y;
		return new Vector2f(_x, _y);
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void set(Vector2f v){
		this.x = v.x;
		this.y = v.y;
	}
	
	public float lengthSquared(){
		return x * x + y * y;
	}
	
	public float length(){
		return FastMath.sqrt(lengthSquared());
	}
	
	public float direction(){
		return FastMath.getAngleInDegrees(x, y);
	}
	
//	public Vector2f round(){
//		
//		Vector2f result = new Vector2f();	
//		result.x = FastMath.roundf(this.x);
//		result.y = FastMath.roundf(this.y);
//		
//		return result;
//	}
	
	public void store(FloatBuffer buffer){
		buffer.put(x).put(y);
	}
	
	public String toString(){
		return "Vector2f [" + x + ", " + y + " ]";
	}
	
//	public boolean equals(Vector2f other){
//		
//		Vector2f rounded = round();
//		Vector2f otherRounded = other.round();
//		
//		return rounded.x == otherRounded.x && rounded.y == otherRounded.y;
//		
//	}
	
	public static float dot(Vector2f a, Vector2f b){
		return (a.x * b.x) + (a.y * b.y);
	}
	
	public static float cross(Vector2f a, Vector2f b){
		return (a.x * b.y) - (a.y * b.x);
	}
	
	public static Vector2f cross(Vector2f v, float s){
		return new Vector2f(v.y * s, v.x * -s);
	}
	
	public static Vector2f cross(float s, Vector2f v){
		return new Vector2f(v.y * -s, v.x * s);
	}
	
	public static Vector2f reflect(Vector2f v, Vector2f n){
		Vector2f vec = v.sub(n.scale(2 * Vector2f.dot(v, n)));
		return vec;
	}
	
}
