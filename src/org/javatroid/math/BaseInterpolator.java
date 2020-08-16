package org.javatroid.math;

public abstract class BaseInterpolator implements Interpolator{

	public abstract float interpolate(float start, float end, float weight);
	
	@Override
	public Vector2f interpolate(Vector2f start, Vector2f end, float weight) {
		float x = interpolate(start.x, end.x, weight);
		float y = interpolate(start.y, end.y, weight);
		return new Vector2f(x, y);
	}

	@Override
	public Vector3f interpolate(Vector3f start, Vector3f end, float weight) {
		float x = interpolate(start.x, end.x, weight);
		float y = interpolate(start.y, end.y, weight);
		float z = interpolate(start.z, end.z, weight);
		return new Vector3f(x, y, z);
	}

	@Override
	public Vector4f interpolate(Vector4f start, Vector4f end, float weight) {
		float x = interpolate(start.x, end.x, weight);
		float y = interpolate(start.y, end.y, weight);
		float z = interpolate(start.z, end.z, weight);
		float w = interpolate(start.w, end.w, weight);
		return new Vector4f(x, y, z, w);
	}
	
}
