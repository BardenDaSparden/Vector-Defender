package com.vecdef.objects;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.model.MultiplierModel;
import com.vecdef.util.Masks;

public class MultiplierPiece extends Entity{

	final float ROTATIONAL_DAMPING = 0.99f;
	final float VELOCITY_DAMPING = 0.95f;
	final float RANGE = 150;
	
	Timer expiryTimer = new Timer(240);
	
	ContactEventListener listener;
	
	public MultiplierPiece(Scene scene){
		super(scene);
		transform.setOrientation(FastMath.random() * (float)Math.PI * 2);
		angularVelocity = FastMath.randomf(1, 4);
		model = MultiplierModel.get();
		
		expiryTimer.setCallback(new TimerCallback() {
			public void execute(Timer timer) {
				destroy();
			}
		});
		expiryTimer.start();
		
		listener = new ContactEventListener(){
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				if((other.getGroupMask() & Masks.Collision.PLAYER) == Masks.Collision.PLAYER)
					destroy();
				
			}
		};
	}
	
	public void update() {
		
		Vector2f position = transform.getTranslation();
		Player player = scene.getNearestPlayer(position.x, position.y);
		
		expiryTimer.tick();
		
		opacity = 1 - expiryTimer.percentComplete();
		
		transform.setOrientation(transform.getOrientation() + angularVelocity);
		transform.setTranslation(getTransform().getTranslation().add(velocity));
		
		if(player != null){
			Vector2f dPos = player.getTransform().getTranslation().sub(getTransform().getTranslation());
			if(dPos.lengthSquared() < RANGE * RANGE){
				if(!player.isRespawning()){
					float s = 0.1f * (RANGE - dPos.length());
					transform.setTranslation(getTransform().getTranslation().add(dPos.normalize().scale(s)));
				}
			}
		}
		
		this.angularVelocity *= ROTATIONAL_DAMPING;
		this.velocity = velocity.scale(VELOCITY_DAMPING);
		
		int gridWidth = scene.getGrid().getWidth();
		int gridHeight = scene.getGrid().getHeight();
		
		if ((getTransform().getTranslation().x < -gridWidth / 2) || (getTransform().getTranslation().x > gridWidth / 2)) {
			getTransform().getTranslation().x = FastMath.clamp(-gridWidth / 2 + 1, gridWidth / 2 - 1,getTransform().getTranslation().x);
	    	velocity.x *= -1.0F;
	    }

	    if ((getTransform().getTranslation().y < -gridHeight / 2) || (getTransform().getTranslation().y > gridHeight / 2)) {
	    	getTransform().getTranslation().y = FastMath.clamp(-gridHeight / 2 + 1, gridHeight / 2 - 1, getTransform().getTranslation().y);
	    	velocity.y *= -1.0F;
	    }
		
	}
	
	@Override
	public void reuse(){
		super.reuse();
		addContactListener(listener);
	}
	
	@Override
	public void recycle(){
		super.recycle();
		velocity.set(0, 0);
		angularVelocity = 0;
		acceleration.set(0, 0);
		torque = 0;
		expiryTimer.restart();
		removeContactListener(listener);
	}
	
	@Override
	public void destroy(){
		if(!super.isRecycled())
			scene.getMultiplierPool().recycle(this);
	}
	
	public int getEntityType(){
		return Masks.Entities.MULTIPLIER;
	}

	@Override
	public int getRadius() {
		return 7;
	}

	@Override
	public int getGroupMask() {
		return Masks.Collision.MULTIPLIER;
	}

	@Override
	public int getCollisionMask() {
		return Masks.Collision.BLACK_HOLE | Masks.Collision.PLAYER;
	}
}
