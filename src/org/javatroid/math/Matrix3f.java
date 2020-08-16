package org.javatroid.math;

import java.nio.FloatBuffer;

public class Matrix3f {

	private float[] data;
	
	public Matrix3f(){
		data = new float[9];
	}
	
	public float get(int i, int j){
		return data[j * 3 + i];
	}
	
	public void set(int i , int j , float v){
		data[j * 3 + i] = v;
	}
	
	public Matrix3f identity(){
		
		Matrix3f result = new Matrix3f();
		
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 3; i++){
				if(i == j)
					result.set(i, j, 1);
				else
					result.set(i, j, 0);
			}
		}
		
		return result;
	}
	
	public Matrix3f transpose(){
		Matrix3f result = new Matrix3f();
		
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 3; i++){
				if(i == j)
					result.set(i, j, get(i, j));
				else 
					result.set(i, j, get(j, i));
			}
		}
		
		return result;
	}
	
	public Matrix3f add(Matrix3f m){
		
		Matrix3f result = new Matrix3f();
		
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 3; i++){
				float value = get(i, j) + m.get(i, j);
				result.set(i, j, value);
			}
		}
		
		return result;
	}
	
	public Matrix3f sub(Matrix3f m){
		
		Matrix3f result = new Matrix3f();
		
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 3; i++){
				float value = get(i, j) - m.get(i, j);
				result.set(i, j, value);
			}
		}
		
		return result;
	}
	
	public Matrix3f mul(Matrix3f m){
		
		Matrix3f result = new Matrix3f();
		
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 3; i++){
				float value = 	get(0, j) * m.get(i, 0) + 
								get(1, j) * m.get(i, 1) + 
								get(2, j) * m.get(i, 2);
				result.set(i, j, value);
			}
		}
		
		return result;
	}
	
	public Matrix3f div(Matrix3f m){
		
		Matrix3f result = new Matrix3f();
		
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 3; i++){
				float value = 	get(0, j) / m.get(i, 0) + 
								get(1, j) / m.get(i, 1) + 
								get(2, j) / m.get(i, 2);
				result.set(i, j, value);
			}
		}
		
		return result;
	}
	
	public void store(FloatBuffer buffer){
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				buffer.put(get(i, j));
			}
		}
	}
	
	public static Matrix3f createTranslation(Vector2f position){
		Matrix3f mat = new Matrix3f();
		mat.identity();
		
		mat.set(2, 0, position.x);
		mat.set(2, 1, position.y);
		mat.set(2, 2, 1);
		
		return mat;
	}
	
	public static Matrix3f createRotation(float angleInDegrees){
		Matrix3f mat = new Matrix3f().identity();
		
		float angle = FastMath.toRadians(angleInDegrees);
		float c = FastMath.roundf(FastMath.cos(angle));
		float s = FastMath.roundf(FastMath.sin(angle));
		
		mat.set(0, 0, c);
		mat.set(1, 0, -s);
		mat.set(0, 1, s);
		mat.set(1, 1, c);
		
		return mat;
		
	}
	
	public static Matrix3f createScale(Vector2f scale){
		Matrix3f mat = new Matrix3f().identity();
		
		mat.set(0, 0, scale.x);
		mat.set(1, 1, scale.y);
		mat.set(2, 2, 1);
		
		return mat;
	}
	
	public String toString(){
		String s = "";
		s += data[0] + " " + data[1] + " " + data[2] + "\n";
		s += data[3] + " " + data[4] + " " + data[5] + "\n";
		s += data[6] + " " + data[7] + " " + data[8];
		return s;
	}
	
}
