package org.javatroid.graphics;

import org.javatroid.math.Vector4f;

public class VerticalGradient implements Gradient{

	Vector4f[] colors = new Vector4f[4];
	
	public VerticalGradient(Vector4f color){
		this(color, color);
	}
	
	public VerticalGradient(Vector4f bottomColor, Vector4f topColor){
		colors[0] = new Vector4f(bottomColor);
		colors[1] = new Vector4f(topColor);
		colors[2] = new Vector4f(topColor);
		colors[3] = new Vector4f(bottomColor);
	}
	
	public Vector4f[] getColors(){
		return colors;
	}
	
}
