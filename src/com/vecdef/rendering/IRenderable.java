package com.vecdef.rendering;

import com.vecdef.model.Model;
import com.vecdef.model.Transform;

public interface IRenderable {
	
	public Model getModel();
	
	public Transform getTransform();
	
	public float getOpacity();
	public void setOpacity(float opacity);
	
	public boolean isVisible();
	public void setVisible(boolean visible);
	
}
