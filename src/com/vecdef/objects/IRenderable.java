package com.vecdef.objects;

import com.vecdef.model.Mesh;
import com.vecdef.model.Transform2D;

public interface IRenderable {
	
	public Mesh getMesh();
	public Transform2D getTransform();
	public float getOpacity();
	public void setOpacity(float opacity);
	
}
