package org.javatroid.graphics;

import org.javatroid.math.Vector4f;

public class Color extends Vector4f{

	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color ORANGE = new Color(1, 0.6f, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0, 1);
	public static final Color GREEN = new Color(0, 1, 0, 1);
	public static final Color BLUE = new Color(0, 0, 1, 1);
	public static final Color CYAN = new Color(0, 1, 1, 1);
	public static final Color PURPLE = new Color(0.6f, 0, 1, 1);
	public static final Color MAGENTA = new Color(1, 0, 1, 1);
	public static final Color WHITE = new Color(1, 1, 1, 1);
	public static final Color LIGHT_GREY = new Color(0.3f, 0.3f, 0.3f, 1);
	public static final Color DARK_GREY = new Color(0.7f, 0.7f, 0.7f, 1);
	public static final Color BLACK = new Color(0, 0, 0, 1);
	public static final Color BLACK_NO_ALPHA = new Color(0, 0, 0, 0);
	
	public Color(float r, float g, float b, float a){
		super(r, g, b, a);
	}
	
}
