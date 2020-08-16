package org.javatroid.math;


public class Quaternion extends Vector4f{
	
	public Quaternion(float x, float y, float z, float w){
		super(x, y, z, w);
	}
	
	public Quaternion(Vector3f n, float theta){
		
		float c = FastMath.cosd(theta / 2.0f);
		float s = FastMath.sind(theta / 2.0f);
		
		this.x = n.x * s;
		this.y = n.y * s;
		this.z = n.z * s;
		this.w = c;
		
	}
	
	public Quaternion conjugate(){
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion mul(Quaternion q){
		
		float _w = w * q.w - x * q.x - y * q.y - z * q.z;
		float _x = x * q.w + w * q.x + y * q.z - z * q.y;
		float _y = y * q.w + w * q.y + z * q.x - x * q.z;
		float _z = z * q.w + w * q.z + x * q.y - y * q.x;
		
		
		return new Quaternion(_x, _y, _z, _w);
		
	}
	
}
