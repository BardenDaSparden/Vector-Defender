package com.vecdef.objects;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.Vector2f;

import com.vecdef.util.Masks;

public class SpawnEffect extends Entity {
	
	final int EFFECT_DURATION_MS = 40;
	Timer expiryTimer = new Timer(EFFECT_DURATION_MS);
	
	public SpawnEffect(Scene scene) {
		super(scene);
		expiryTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				destroy();
			}
		});
		expiryTimer.start();
		radius = 0;
	}

	public void setBase(Entity object){
		transform.set(object.transform);
		transform.setOrientation(object.getTransform().getOrientation());
		this.overrideColor.set(object.getOverrideColor());
		model = object.getModel();
	}
	
	@Override
	public void update() {
		expiryTimer.tick();
		float opacity = (float) Math.sin( (1 - expiryTimer.percentComplete()) * Math.PI / 2f );
		setOpacity(opacity);
		transform.getScale().addi(new Vector2f(0.07f, 0.07f));
	}

	@Override
	public void reuse(){
		super.reuse();
	}
	
	@Override
	public void recycle(){
		super.recycle();
		expiryTimer.restart();
		transform.getScale().set(1, 1);
	}
	
	@Override
	public void destroy(){
		if(!super.isRecycled())
			scene.getSpawnEffectPool().recycle(this);
	}
	
	@Override
	public int getEntityType() {
		return Masks.Entities.OTHER;
	}
	
	@Override
	public boolean useOverrideColor(){
		return true;
	}
	
	@Override
	public int getRadius() {
		return 0;
	}

	@Override
	public int getGroupMask() {
		return Masks.NONE;
	}

	@Override
	public int getCollisionMask() {
		return Masks.NONE;
	}
	
}
