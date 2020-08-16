package com.vecdef.objects;

public interface IPoolable {

	public void reuse();
	public void recycle();
	public boolean isRecycled();
	
}
