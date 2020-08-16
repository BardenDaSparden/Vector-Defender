package org.javatroid.math;

public class LinearInterpolator extends BaseInterpolator{
	
	public float interpolate(float start, float end, float weight){
		if(weight == 0){
			return start;
		} else if (weight == 1){
			return end;
		} else {
			return start + ((end - start) * weight);
		}
	}
}
