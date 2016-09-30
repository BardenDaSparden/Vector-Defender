package com.vecdef.rendering;

import org.javatroid.math.Vector4f;

import com.vecdef.model.Model;
import com.vecdef.model.Transform;

public interface IRenderable {
	
	public Model getModel();
	
	public Transform getTransform();
	
	public float getOpacity();
	public void setOpacity(float opacity);
	
	public boolean isVisible();
	public void setVisible(boolean visible);
	
	public boolean useOverrideColor();
	public Vector4f getOverrideColor();
	
}
