package org.javatroid.math;

import java.nio.FloatBuffer;

public class Matrix4f {

	private float[] data;
	
	public Matrix4f(){
		data = new float[16];
	}
	
	public float get(int i, int j){
		return data[j * 4 + i];
	}
	
	public void set(int i , int j , float v){
		data[j * 4 + i] = v;
	}
	
	public Matrix4f identity(){
		
		Matrix4f result = new Matrix4f();
		
		for(int j = 0; j < 4; j++){
			for(int i = 0; i < 4; i++){
				if(i == j)
					result.set(i, j, 1);
				else
					result.set(i, j, 0);
			}
		}
		
		return result;
	}
	
	public Matrix4f transpose(){
		Matrix4f result = new Matrix4f();
		
		for(int j = 0; j < 4; j++){
			for(int i = 0; i < 4; i++){
				if(i == j)
					result.set(i, j, get(i, j));
				else 
					result.set(i, j, get(j, i));
			}
		}
		
		return result;
	}
	
	/*
	public Matrix4f adjugate(){
		
	}
	*/
	
	/*
	public float determinant(){
		
	}
	 */
	
	/*
	public void inverse(){
		
	}
	*/
	
	public Matrix4f add(Matrix4f m){
		
		Matrix4f result = new Matrix4f();
		
		for(int j = 0; j < 4; j++){
			for(int i = 0; i < 4; i++){
				float value = get(i, j) + m.get(i, j);
				result.set(i, j, value);
			}
		}
		
		return result;
		
	}
	
	public Matrix4f sub(Matrix4f m){
		
		Matrix4f result = new Matrix4f();
		
		for(int j = 0; j < 4; j++){
			for(int i = 0; i < 4; i++){
				float value = get(i, j) - m.get(i, j);
				result.set(i, j, value);
			}
		}
		
		return result;
		
	}
	
	public Matrix4f mul(Matrix4f m){
		
		Matrix4f result = new Matrix4f();
		
		for(int j = 0; j < 4; j++){
			for(int i = 0; i < 4; i++){
				float value = 	get(0, j) * m.get(i, 0) + 
								get(1, j) * m.get(i, 1) + 
								get(2, j) * m.get(i, 2) + 
								get(3, j) * m.get(i, 3);
				
				result.set(i, j, value);
			}
		}
		
		return result;
		
	}
	
	public Vector4f mul(Vector4f v){
		
		Vector4f result = new Vector4f();
		
		float x = get(0, 0) * v.x + get(0, 1) * v.y + get(0, 2) * v.z + get(0, 3) * v.w;
		float y = get(1, 0) * v.x + get(1, 1) * v.y + get(1, 2) * v.z + get(1, 3) * v.w;
		float z = get(2, 0) * v.x + get(2, 1) * v.y + get(2, 2) * v.z + get(2, 3) * v.w;
		float w = get(3, 0) * v.x + get(3, 1) * v.y + get(3, 2) * v.z + get(3, 3) * v.w;
		
		result.x = x;
		result.y = y;
		result.z = z;
		result.w = w;
		
		return result;
		
	}
	
	public Matrix4f div(Matrix4f m){
		
		Matrix4f result = new Matrix4f();
		
		for(int j = 0; j < 4; j++){
			for(int i = 0; i < 4; i++){
				float value = 	get(0, j) / m.get(i, 0) + 
								get(1, j) / m.get(i, 1) + 
								get(2, j) / m.get(i, 2) + 
								get(3, j) / m.get(i, 3);
				
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
	
	public static Matrix4f createTranslation(Vector3f translation){
		Matrix4f result = new Matrix4f().identity();
		
		result.set(3, 0, translation.x);
		result.set(3, 1, translation.y);
		result.set(3, 2, translation.z);
		
		return result;
	}
	
	
	public static Matrix4f createRotationX(float angleInDegrees){
		
		Matrix4f result = new Matrix4f().identity();
		
		float s = FastMath.sind(angleInDegrees);
		float c = FastMath.cosd(angleInDegrees);
		
		result.set(1, 1, c);
		result.set(2, 1, -s);
		result.set(1, 2, s);
		result.set(2, 2, c);
		
		return result;
		
	}
	
	public static Matrix4f createRotationY(float angleInDegrees){
		
		Matrix4f result = new Matrix4f().identity();
		
		float s = FastMath.sind(angleInDegrees);
		float c = FastMath.cosd(angleInDegrees);
		
		result.set(0, 0, c);
		result.set(2, 0, s);
		result.set(0, 2, -s);
		result.set(2, 2, c);
		
		return result;
		
	}
	
	
	public static Matrix4f createRotationZ(float angleInDegrees){
		
		Matrix4f result = new Matrix4f().identity();
		
		float s = FastMath.sind(angleInDegrees);
		float c = FastMath.cosd(angleInDegrees);
		
		result.set(0, 0, c);
		result.set(1, 0, -s);
		result.set(0, 1, s);
		result.set(1, 1, c);
		
		return result;
		
	}
	
	public static Matrix4f createRotation(Quaternion q){
		
		Matrix4f result = new Matrix4f().identity();
		
		q = (Quaternion) q.normalize();
		
		result.set(0, 0, 1 - 2 * q.y * q.y - 2 * q.z * q.z);
		result.set(1, 0, 2 * q.x * q.y - 2 * q.z * q.w);
		result.set(2, 0, 2 * q.x * q.z + 2 * q.y * q.w);
		
		result.set(0, 1, 2 * q.x * q.y + 2 * q.z * q.w);
		result.set(1, 1, 1 - 2 * q.x * q.x - 2 * q.z * q.z);
		result.set(2, 1, 2 * q.y * q.z - 2 * q.x * q.w);
		
		result.set(0, 2, 2 * q.x * q.z - 2 * q.y * q.w);
		result.set(1, 2, 2 * q.y * q.z + 2 * q.x * q.w);
		result.set(2, 2, 1 - 2 * q.x * q.x - 2 * q.y * q.y);
		
		return result;
		
	}
	
	public static Matrix4f createScale(Vector3f scale){
		
		Matrix4f result = new Matrix4f().identity();
		
		result.set(0, 0, scale.x);
		result.set(1, 1, scale.y);
		result.set(2, 2, scale.z);
		
		return result;
		
	}
	
	public static Matrix4f createOrthographicProjection(float left, float right, float bottom, float top, float zNear, float zFar){
		
		Matrix4f mat = new Matrix4f().identity();
		
		mat.set(0, 0, (2.0f / (right - left)));
		mat.set(1, 1, (2.0f / (top - bottom)));
		mat.set(2, 2, (-2.0f / (zFar - zNear)));
		
		mat.set(3, 0, -(right + left) / (right - left));
		mat.set(3, 1, -(top + bottom) / (top - bottom));
		mat.set(3, 2, -(zFar + zNear) / (zFar - zNear));
		
		return mat;
		
	}
	
	/*
	public static Matrix4f createPerspectiveProjection(){
		
	}
	*/
	
	public String toString(){
		String s = "";
		
		s += data[0] + " " + data[1] + " " + data[2] + " " + data[3] + "\n";
		s += data[4] + " " + data[5] + " " + data[6] + " " + data[7] + "\n";
		s += data[8] + " " + data[9] + " " + data[10] + " " + data[11] + "\n";
		s += data[12] + " " + data[13] + " " + data[14] + " " + data[15] + "\n";
		
		return s;
	}
	
}
