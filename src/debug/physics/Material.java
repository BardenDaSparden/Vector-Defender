package debug.physics;

import org.javatroid.math.FastMath;

public class Material {
	
	private float density;
	private float restitution;
	private float staticFriction = 0.1f;
	private float kineticFriction = 0.1f;
	
	public Material(float density, float restitution){
		this.density = density;
		this.restitution = FastMath.clamp(0, 1, restitution);
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = FastMath.clamp(0, 1, restitution);
	}

	public float getStaticFriction() {
		return staticFriction;
	}

	public void setStaticFriction(float staticFriction) {
		this.staticFriction = FastMath.clamp(0, 1, staticFriction);
	}

	public float getKineticFriction() {
		return kineticFriction;
	}

	public void setKineticFriction(float kineticFriction) {
		this.kineticFriction = FastMath.clamp(0, 1, kineticFriction);
	}
	
}
