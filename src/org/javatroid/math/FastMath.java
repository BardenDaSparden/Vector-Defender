package org.javatroid.math;

import java.text.DecimalFormat;

public class FastMath {

	public static final float DEGREE_TO_RADIAN = 0.01745329251f;
	public static final float RADIAN_TO_DEGREE = 57.2957795131f;
	public static final float PI = 3.14159265359f;
	public static final float TWO_PI = 6.28318530718f;
	public static final DecimalFormat df = new DecimalFormat("#.##");
	
	public static float toDegrees(float radians){
		return RADIAN_TO_DEGREE * radians;
	}
	
	public static float toRadians(float degrees){
		return DEGREE_TO_RADIAN * degrees;
	}
	
	public static float sqrt(float n){
		return (float) Math.sqrt(n);
	}
	
	public static float sin(float radians){
		return (float) Math.sin(radians);
	}
	
	public static float cos(float radians){
		return (float) Math.cos(radians);
	}
	
	public static float sind(float degrees){
		return sin(toRadians(degrees));
	}
	
	public static float cosd(float degrees){
		return cos(toRadians(degrees));
	}
	
	public static float min(float v1, float v2){
		return (float)Math.min(v1, v2);
	}
	
	public static float max(float v1, float v2){
		return (float)Math.max(v1, v2);
	}
	
	public static float abs(float b){
		return (float)Math.abs(b);
	}
	
	public static float clamp(float min, float max, float value){
		if(value <= min)
			return min;
		else if(value >= max)
			return max;
		else 
			return value;
			
	}
	
	public static float approach(float start, float end, float w){
		return start + (end - start) * w;
	}
	
	public static float getAngle(float x, float y){
		return (float)Math.atan2(y, x);
	}
	
	public static float getAngleInDegrees(float x, float y){
		float a = toDegrees((float) Math.atan2(y, x));
		if(a < 0)
			a = 180 - abs(a) + 180;
		return a;
		
	}
	
	public static float randomf(float low, float high){
		return (float) (low + ((high - low) * Math.random()));
	}
	
	public static int randomi(int low, int high){
		return (int) (low + ((high - low) * Math.random()));
	}
	
	public static float random(){
		return (float)Math.random();
	}
	
	private static float hue2rgb(float p, float q, float t){
        if(t < 0) t += 1;
        if(t > 1) t -= 1;
        if(t < 1/6) return p + (q - p) * 6 * t;
        if(t < 1/2) return q;
        if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
        return p;
    }
	
	public static float[] hslToRgb(float h, float s, float l){
	    float r, g, b;

	    if(s == 0){
	        r = g = b = l; // achromatic
	    }else{
	       

	        float q = l < 0.5 ? l * (1 + s) : l + s - l * s;
	        float p = 2 * l - q;
	        r = hue2rgb(p, q, h + 1/3);
	        g = hue2rgb(p, q, h);
	        b = hue2rgb(p, q, h - 1/3);
	    }

	    return new float[]{r * 255, g * 255, b * 255};
	}
	
	public static float roundf(float n){
		return Float.valueOf(df.format(n));
	}
	
}
