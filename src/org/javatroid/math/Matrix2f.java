package org.javatroid.math;

import java.nio.FloatBuffer;

public class Matrix2f {

	private float[] data;
	
	public Matrix2f(){
		data = new float[4];
	}
	
	public float get(int i, int j){
		return data[j * 2 + i];
	}
	
	public void set(int i , int j, float value){
		data[j * 2 + i] = value;
	}
	
	public Matrix2f identity(){
		
		Matrix2f result = new Matrix2f();
		result.set(0, 0, 1);
		result.set(1, 0, 0);
		result.set(0, 1, 0);
		result.set(1, 1, 1);
		
		return result;
	}
	
	public Matrix2f transpose(){
		Matrix2f result = new Matrix2f();
		
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 2; i++){
				if(i == j)
					result.set(i, j, get(i, j));
				else 
					result.set(i, j, get(j, i));
			}
		}
		
		return result;
	}
	
	public Matrix2f add(Matrix2f m){
		
		Matrix2f result = new Matrix2f();
		
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 2 ; i++){
				float value = get(i, j) + m.get(i, j);
				result.set(i, j, value);
			}
		}
		
		return result;
	}
	
	public Matrix2f sub(Matrix2f m){
		
		Matrix2f result = new Matrix2f();
		
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 2 ; i++){
				float value = get(i, j) - m.get(i, j);
				result.set(i, j, value);
			}
		}
		
		return result;
	}
	
	public Matrix2f mul(Matrix2f m){
		
		Matrix2f result = new Matrix2f();
		
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 2 ; i++){
				float value = 	get(0, j) * m.get(i, 0) +
								get(1, j) * m.get(i, 1);
				result.set(i, j, value);
			}
		}
		
		return result;
	}
	
	public Matrix2f div(Matrix2f m){
		
		Matrix2f result = new Matrix2f();
		
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 2 ; i++){
				float value = 	get(0, j) / m.get(i, 0) +
								get(1, j) / m.get(i, 1);
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
	
	public String toString(){
		String s = 	"[" + data[0] + " " + data[1] + "]\n";
		s += 		"[" + data[2] + " " + data[3] + "]";
		return s;
	}
	
}
