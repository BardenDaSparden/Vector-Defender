package com.vecdef.objects;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.Vector2f;

import com.vecdef.util.Masks;

public class EnemySpawnEffect extends Entity {
	
	final int EFFECT_DURATION_MS = 40;
	Timer expiryTimer = new Timer(EFFECT_DURATION_MS);
	
	public EnemySpawnEffect(Scene scene, Enemy enemy) {
		super(scene);
		transform.set(enemy.transform);
		model = enemy.getModel();
		expiryTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				expire();
			}
		});
		expiryTimer.start();
		scene.add(this);
	}

	@Override
	public void update() {
		expiryTimer.tick();
		float opacity = (float) Math.sin( (1 - expiryTimer.percentComplete()) * Math.PI / 2f );
		setOpacity(opacity);
		transform.getScale().addi(new Vector2f(0.07f, 0.07f));
	}

	@Override
	public void destroy(){
		scene.remove(this);
	}
	
	@Override
	public int getEntityType() {
		return Masks.Entities.OTHER;
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
