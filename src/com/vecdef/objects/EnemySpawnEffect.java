package com.vecdef.objects;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.model.Mesh;
import com.vecdef.model.Transform2D;

public class EnemySpawnEffect extends Entity{
	
	final int EFFECT_DURATION_MS = 25;
	
	float fMagnitude;
	float fRadius;
	
	IRenderable renderable;
	
	Timer expiryTimer = new Timer(EFFECT_DURATION_MS);
	
	public EnemySpawnEffect(Scene scene, Enemy enemy, float fRadius, float fMag) {
		super(scene);
		
		Transform2D enemyTransform = new Transform2D(enemy.getTransform());
		Mesh enemyMesh = enemy.getMesh();
		this.transform = enemyTransform;
		this.mesh = enemyMesh;
		
		renderable = new IRenderable() {
			
			float opacity = 1;
			boolean drawn = true;
			Transform2D t = enemyTransform;
			Mesh m = enemyMesh;
			
			@Override
			public void setOpacity(float opacity) {
				this.opacity = opacity;
			}
			
			@Override
			public void setDraw(boolean bDraw) {
				this.drawn = bDraw;
			}
			
			@Override
			public boolean isDrawn() {
				return drawn;
			}
			
			@Override
			public Transform2D getTransform() {
				return t;
			}
			
			@Override
			public float getOpacity() {
				return opacity;
			}
			
			@Override
			public Mesh getMesh() {
				return m;
			}
		};
		
		expiryTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				expire();
			}
		});
		
		expiryTimer.start();
		
		scene.addRenderable(renderable);
	}

	@Override
	public void update() {
		float opacity = (float) Math.sin( (1 - expiryTimer.percentComplete()) * Math.PI / 2f );
		renderable.setOpacity(opacity);
		expiryTimer.tick();
		transform.getScale().addi(new Vector2f(0.15f, 0.15f));
	}

	@Override
	public void destroy(){
		Vector2f position = transform.getTranslation();
		scene.getGrid().applyExplosiveForce(fMagnitude, new Vector3f(position.x, position.y, 0), fRadius);
		scene.removeRenderable(renderable);
	}
	
	public boolean isDrawn(){
		return false;
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
