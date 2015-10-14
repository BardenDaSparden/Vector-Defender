package com.vecdef.objects;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.core.Window;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;

public class MultiplierPiece extends Entity{

	final float ROTATIONAL_DAMPING = 0.99f;
	final float VELOCITY_DAMPING = 0.95f;
	final float RANGE = 150;
	
	Mesh mesh;
	
	Timer timer = new Timer(500);
	
	public MultiplierPiece(Vector2f position, float startRotation, float av){
		transform.setTranslation(position);
		transform.setOrientation(startRotation);
		angularVelocity = av;
		mesh = new Mesh();
		
		final float WIDTH = 6;
		final float HEIGHT = 6;
		Vector4f color = new Vector4f(0.2f, 1, 0.2f, 1);
		
		LinePrimitive body = new LinePrimitive();
		body.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), color);
		body.addVertex(new Vector2f(-WIDTH / 2f + 1, HEIGHT / 2f - 1), color);
		
		body.addVertex(new Vector2f(-WIDTH / 2f + 1, HEIGHT / 2f - 1), color);
		body.addVertex(new Vector2f(WIDTH / 2f, HEIGHT / 2f), color);
		
		body.addVertex(new Vector2f(WIDTH / 2f, HEIGHT / 2f), color);
		body.addVertex(new Vector2f(WIDTH / 2f - 1, -HEIGHT / 2f + 1), color);
		
		body.addVertex(new Vector2f(WIDTH / 2f - 1, -HEIGHT / 2f + 1), color);
		body.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), color);
		
		MeshLayer bodyLayer = new MeshLayer();
		bodyLayer.addPrimitive(body);
		
		mesh.addLayer(bodyLayer);
		
		timer.setCallback(new TimerCallback() {
			
			public void execute(Timer timer) {
				bExpired = true;
			}
		});
		
		timer.start();
		
	}
	
	public void update(Grid grid) {
		
		Player player = getScene().getPlayer();
		
		timer.tick();
		
		opacity = 1 - timer.percentComplete();
		
		transform.setOrientation(transform.getOrientation() + angularVelocity);
		transform.setTranslation(getTransform().getTranslation().add(velocity));
		
		Vector2f dPos = player.getTransform().getTranslation().sub(getTransform().getTranslation());
		if(dPos.lengthSquared() < RANGE * RANGE){
			if(!player.isDead()){
				float s = 0.1f * (RANGE - dPos.length());
				transform.setTranslation(getTransform().getTranslation().add(dPos.normalize().scale(s)));
			}
		}
		
		this.angularVelocity *= ROTATIONAL_DAMPING;
		this.velocity = velocity.scale(VELOCITY_DAMPING);
		
		if ((getTransform().getTranslation().x < -Window.getWidth() / 2) || (getTransform().getTranslation().x > Window.getWidth() / 2)) {
			getTransform().getTranslation().x = FastMath.clamp(-Window.getWidth() / 2 + 1, Window.getWidth() / 2 - 1,getTransform().getTranslation().x);
	    	velocity.x *= -1.0F;
	    }

	    if ((getTransform().getTranslation().y < -Window.getHeight() / 2) || (getTransform().getTranslation().y > Window.getHeight() / 2)) {
	    	getTransform().getTranslation().y = FastMath.clamp(-Window.getHeight() / 2 + 1, Window.getHeight() / 2 - 1, getTransform().getTranslation().y);
	    	velocity.y *= -1.0F;
	    }
		
	}
	
	public void collision(Entity other){
		if(other instanceof Player){
			bExpired = true;
		}
	}
	
	public void destroy(){
		bExpired = true;
	}
	
	public int getEntityType(){
		return Masks.Entities.MULTIPLIER;
	}
	
	public Mesh getMesh(){
		return mesh;
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
		return Masks.Collision.ENEMY | Masks.Collision.PLAYER;
	}
}
