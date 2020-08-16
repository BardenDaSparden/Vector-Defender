package org.javatroid.collision;

import org.javatroid.math.FastMath;

public class Projection {

	public float min, max;
	
	public Projection(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	public boolean overlap(Projection other){
		if(min >= other.max || max <= other.min){
			return false;
		} else {
			return true;
		}
	}
	
	public float getOverlap(Projection other){
		return FastMath.min((max - other.min), (other.max - min));
	}
	
}
