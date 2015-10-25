package com.vecdef.objects;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.ai.BlackHoleBehaviour;
import com.vecdef.ai.ChaserBehaviour;
import com.vecdef.ai.FollowerBehavior;
import com.vecdef.ai.PrototypeBehaviour;
import com.vecdef.ai.StalkerBehaviour;
import com.vecdef.ai.WandererBehavior;
import com.vecdef.gamestate.Scene;
import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;

public class EnemyFactory {

	Scene scene;
	
	public EnemyFactory(Scene scene){
		this.scene = scene;
	}
	
	public Enemy createWanderer(Vector2f position){
		Enemy enemy = new Enemy(scene);
		
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(1, 1, 0, 1);
	    enemy.killValue = 5;
		enemy.mesh = new Mesh();
		final float WIDTH = 26;
		final float HEIGHT = 26;
		
		LinePrimitive p1 = new LinePrimitive();
		
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		p1.addVertex(new Vector2f(0, HEIGHT / 2f), enemy.baseColor);
		
		p1.addVertex(new Vector2f(0, HEIGHT / 2f), enemy.baseColor);
		p1.addVertex(new Vector2f(WIDTH / 4f, HEIGHT / 4f), enemy.baseColor);
		
		p1.addVertex(new Vector2f(WIDTH / 4f, HEIGHT / 4f), enemy.baseColor);
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		p1.addVertex(new Vector2f(WIDTH / 2f, 0), enemy.baseColor);
		
		p1.addVertex(new Vector2f(WIDTH / 2f, 0), enemy.baseColor);
		p1.addVertex(new Vector2f(WIDTH / 4f, -HEIGHT / 4f), enemy.baseColor);
		
		p1.addVertex(new Vector2f(WIDTH / 4f, -HEIGHT / 4f), enemy.baseColor);
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		p1.addVertex(new Vector2f(0, -HEIGHT / 2f), enemy.baseColor);
		
		p1.addVertex(new Vector2f(0, -HEIGHT / 2f), enemy.baseColor);
		p1.addVertex(new Vector2f(-WIDTH / 4f, -HEIGHT / 4f), enemy.baseColor);
		
		p1.addVertex(new Vector2f(-WIDTH / 4f, -HEIGHT / 4f), enemy.baseColor);
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		p1.addVertex(new Vector2f(-WIDTH / 2f, 0), enemy.baseColor);
		
		p1.addVertex(new Vector2f(-WIDTH / 2f, 0), enemy.baseColor);
		p1.addVertex(new Vector2f(-WIDTH / 4f, HEIGHT / 4f), enemy.baseColor);
		
		p1.addVertex(new Vector2f(-WIDTH / 4f, HEIGHT / 4f), enemy.baseColor);
		p1.addVertex(new Vector2f(0, 0), enemy.baseColor);
		
		MeshLayer bodyLayer = new MeshLayer();
		bodyLayer.addPrimitive(p1);
		enemy.mesh.addLayer(bodyLayer);
		enemy.addBehavior(new WandererBehavior(scene, enemy));
		
		scene.add(enemy);
		return enemy;
	}
	
	public Enemy createSeeker(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(0, 1, 0, 1);
	    enemy.killValue = 10;
		enemy.mesh = new Mesh();
		
		final float WIDTH = 20;
		final float HEIGHT = 20;
		LinePrimitive topBody = new LinePrimitive();
		topBody.addVertex(new Vector2f(-WIDTH / 2f, 0), enemy.baseColor);
		topBody.addVertex(new Vector2f(0, HEIGHT / 2f), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(0, HEIGHT / 2f), enemy.baseColor);
		topBody.addVertex(new Vector2f(WIDTH / 2f, 0), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(WIDTH / 2f, 0), enemy.baseColor);
		topBody.addVertex(new Vector2f(0, -HEIGHT / 2f), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(0, -HEIGHT / 2f), enemy.baseColor);
		topBody.addVertex(new Vector2f(-WIDTH / 2f, 0), enemy.baseColor);
		
		MeshLayer bodyLayer = new MeshLayer();
		bodyLayer.addPrimitive(topBody);
		enemy.mesh.addLayer(bodyLayer);
		enemy.addBehavior(new FollowerBehavior(scene, enemy));
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createFollower(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(1, 0.75f, 0, 1);
	    enemy.killValue = 15;
	    enemy.radius = 10;
		enemy.mesh = new Mesh();
		
		final float WIDTH = 18;
		final float HEIGHT = 18;
		
		final float CAP_SIZE = 5;
		
		LinePrimitive body = new LinePrimitive();
		body.addVertex(new Vector2f(0, HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(WIDTH / 2f, 0), enemy.baseColor);
		
		body.addVertex(new Vector2f(WIDTH / 2f, 0), enemy.baseColor);
		body.addVertex(new Vector2f(0, -HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(0, -HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(-WIDTH / 2f, 0), enemy.baseColor);
		
		body.addVertex(new Vector2f(-WIDTH / 2f, 0), enemy.baseColor);
		body.addVertex(new Vector2f(0, HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(-WIDTH / 2f + CAP_SIZE, HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(-WIDTH / 2f + CAP_SIZE, HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(-WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), enemy.baseColor);
		
		body.addVertex(new Vector2f(-WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), enemy.baseColor);
		body.addVertex(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(WIDTH / 2f, HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(WIDTH / 2f - CAP_SIZE, HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(WIDTH / 2f - CAP_SIZE, HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), enemy.baseColor);
		
		body.addVertex(new Vector2f(WIDTH / 2f, HEIGHT / 2f - CAP_SIZE), enemy.baseColor);
		body.addVertex(new Vector2f(WIDTH / 2f, HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(-WIDTH / 2f + CAP_SIZE, -HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(-WIDTH / 2f + CAP_SIZE, -HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), enemy.baseColor);
		
		body.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), enemy.baseColor);
		body.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), enemy.baseColor);
		
		
		body.addVertex(new Vector2f(WIDTH / 2f, -HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(WIDTH / 2f - CAP_SIZE, -HEIGHT / 2f), enemy.baseColor);
		
		body.addVertex(new Vector2f(WIDTH / 2f - CAP_SIZE, -HEIGHT / 2f), enemy.baseColor);
		body.addVertex(new Vector2f(WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), enemy.baseColor);
		
		body.addVertex(new Vector2f(WIDTH / 2f, -HEIGHT / 2f + CAP_SIZE), enemy.baseColor);
		body.addVertex(new Vector2f(WIDTH / 2f, -HEIGHT / 2f), enemy.baseColor);
		
		MeshLayer bodyLayer = new MeshLayer();
		bodyLayer.addPrimitive(body);
		enemy.mesh.addLayer(bodyLayer);
		enemy.addBehavior(new StalkerBehaviour(scene, enemy));
		
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createChaser(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(0, 1, 1, 1);
	    enemy.killValue = 5;
	    enemy.radius = 10;
		enemy.mesh = new Mesh();
		enemy.angularVelocity = FastMath.randomi(-2, 2);
		
		LinePrimitive l1 = new LinePrimitive();
		l1.addVertex(new Vector2f(-6, -6), enemy.baseColor);
		l1.addVertex(new Vector2f(0, 6f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(0, 6f), enemy.baseColor);
		l1.addVertex(new Vector2f(6, -6), enemy.baseColor);
		
		l1.addVertex(new Vector2f(6, -6), enemy.baseColor);
		l1.addVertex(new Vector2f(-6, -6), enemy.baseColor);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(l1);
		
		enemy.mesh.addLayer(layer);
		
		enemy.addBehavior(new ChaserBehaviour(scene, enemy));
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createPrototype(Vector2f position){
		Player player = scene.getPlayer();
		
		Enemy enemy = new Enemy(scene);
		enemy.baseColor = new Vector4f(1, 0, 1, 1);
		enemy.transform.setTranslation(position);
		enemy.transform.setOrientation(player.getTransform().getTranslation().sub(position).direction());
	    enemy.killValue = 25;
	    enemy.radius = 15;
		enemy.mesh = new Mesh();
		
		final float WIDTH = 20;
		final float HEIGHT = 14;
		LinePrimitive topBody = new LinePrimitive();
		topBody.addVertex(new Vector2f(-WIDTH / 2f + 5, 2), enemy.baseColor);
		topBody.addVertex(new Vector2f(WIDTH / 2f, 2), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(WIDTH / 2f, 2), enemy.baseColor);
		topBody.addVertex(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(-WIDTH / 2f, HEIGHT / 2f), enemy.baseColor);
		topBody.addVertex(new Vector2f(-WIDTH / 2f + 5, 2), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(-WIDTH / 2f + 5, -2), enemy.baseColor);
		topBody.addVertex(new Vector2f(WIDTH / 2f, -2), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(WIDTH / 2f, -2), enemy.baseColor);
		topBody.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), enemy.baseColor);
		
		topBody.addVertex(new Vector2f(-WIDTH / 2f, -HEIGHT / 2f), enemy.baseColor);
		topBody.addVertex(new Vector2f(-WIDTH / 2f + 5, -2), enemy.baseColor);
		
		MeshLayer bodyLayer = new MeshLayer();
		bodyLayer.addPrimitive(topBody);
		
		enemy.mesh.addLayer(bodyLayer);
		enemy.addBehavior(new PrototypeBehaviour(scene, enemy));
		
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createBlackHole(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(1, 1, 1, 1);
		enemy.killValue = 200;
	    enemy.health = 8;
	    enemy.radius = 16;
	    enemy.groupMask = Masks.Collision.ENEMY | Masks.Collision.BLACK_HOLE;
	    enemy.collisionMask |= Masks.Collision.MULTIPLIER | Masks.Collision.ENEMY;
	    enemy.mesh = new Mesh();
	    
		float radius = enemy.radius;
	    int segments = 32;
	    LinePrimitive circle = new LinePrimitive();
	    
	    for(int i = 0; i < segments; i++){
	    	float a1 = (float)i / (float) segments * 360f;
	    	float a2 = (float)((i + 1) % segments) / (float)segments * 360f;
	    	
	    	Vector2f v0 = new Vector2f(FastMath.cosd(a1) * radius, FastMath.sind(a1) * radius);
	    	Vector2f v1 = new Vector2f(FastMath.cosd(a2) * radius, FastMath.sind(a2) * radius);
	    	circle.addVertex(v0, enemy.baseColor);
	    	circle.addVertex(v1, enemy.baseColor);
	    }
	    
	    MeshLayer bodyLayer = new MeshLayer();
	    bodyLayer.addPrimitive(circle);
	    enemy.mesh.addLayer(bodyLayer);
	    enemy.addBehavior(new BlackHoleBehaviour(scene, this, enemy));
	    enemy.addContactListener(new ContactEventListener() {
			
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				int group = other.getGroupMask();
				if((group & Masks.Collision.ENEMY) == Masks.Collision.ENEMY){
					Entity eOther = (Entity)other;
					eOther.expire();
				}
			}
		});
	    
	    scene.add(enemy);
	    return enemy;
	}
	
}
