package org.javatroid.graphics;

import org.javatroid.math.Vector4f;

public class HorizontalGradient implements Gradient{

	Vector4f[] colors = new Vector4f[4];
	
	public HorizontalGradient(Vector4f color){
		this(color, color);
	}
	
	public HorizontalGradient(Vector4f left, Vector4f right){
		colors[0] = new Vector4f(left);
		colors[1] = new Vector4f(left);
		colors[2] = new Vector4f(right);
		colors[3] = new Vector4f(right);
	}
	
	public Vector4f[] getColors(){
		return colors;
	}
	
}
