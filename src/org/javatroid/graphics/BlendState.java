package org.javatroid.graphics;

import static org.lwjgl.opengl.GL11.*;

public enum BlendState {

	ALPHA(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA), MULTIPLY(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA), ADDITIVE(GL_SRC_ALPHA, GL_ONE), SCREEN(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
	
	public int sFactor;
	public int dFactor;
	
	private BlendState(int src, int dst){
		this.sFactor = src;
		this.dFactor = dst;
	}
	
}
