package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.ai.Behavior;
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

public class Enemy extends Entity{
	
	Vector4f baseColor;
	ArrayList<Behavior> behaviors;
	boolean bCreate;
	int health;
	int killValue;
	
	int groupMask;
	int collisionMask;
	int radius;
	
	public Enemy(Scene scene){
		super(scene);
		
		baseColor = new Vector4f(1, 1, 1, 1);
		behaviors = new ArrayList<Behavior>();
		bCreate = false;
		health = 1;
		killValue = 1;
		
	    radius = 16;
	    groupMask = Masks.Collision.ENEMY;
	    collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
	    
	    addContactListener(new ContactEventListener() {
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				int otherGroup = other.getGroupMask();
				
				if((otherGroup & Masks.Collision.BULLET) == Masks.Collision.BULLET){
					health -= 1;
					 if(health <= 0){
						expire();
						Player player = scene.getPlayer();
						player.registerBulletKill(Enemy.this);
					 }
				} else if((otherGroup & Masks.Collision.BLACK_HOLE) == Masks.Collision.BLACK_HOLE){
					Enemy.this.expire();
				}
			}
		});
	}

	public void update(){
		int n = behaviors.size();
		if(!bCreate){
			for(int i = 0; i < n; i++){
				Behavior behavior = behaviors.get(i);
				behavior.create(this);
			}
			bCreate = true;
		}
		
		for(int i = 0; i < n; i++){
			Behavior behavior = behaviors.get(i);
			behavior.update(this);
		}
	}
	
	
	
	public void destroy(){
	     int numPieces = FastMath.randomi(1, 4);
	     final int numParticles = 30;
	     float speed = 2;
	     
	     for(int i = 0; i < numPieces; i++){
	    	 MultiplierPiece piece = new MultiplierPiece(transform.getTranslation(), scene);
	    	 float a = FastMath.random() * 360; 
	    	 piece.velocity = new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed);
	    	 scene.add(piece);
	     }
	     
	     
	     Vector2f position = getTransform().getTranslation();
	     Vector2f offset = new Vector2f();
	     float radius = 32;
	     float pSpeed = 7f;
	     
	     for(int i = 0; i < numParticles; i++){
	    	 
	    	 float angle = (float) (((float)i / (float)numParticles) * Math.PI * 2);
	    	 offset.x = (float)Math.cos(angle) * radius;
	    	 offset.y = (float)Math.sin(angle) * radius;
	    	 
	    	 Particle particle = new Particle(position.x, position.y, baseColor, scene);
	    	 
	    	 particle.getTransform().setOrientation((float)Math.toDegrees(angle));
	    	 particle.getTransform().getTranslation().addi(offset);
	    	 
	    	 particle.getVelocity().set((float)Math.cos(angle) * pSpeed, (float)Math.sin(angle) * pSpeed);
	    	 particle.addBehavior(new Behavior(scene) {
				
	    		 @Override
    			public void create(Entity self){
    				
    			}
	    		 
				@Override
				public void update(Entity self) {
					Vector2f velocity = self.getVelocity();
					self.getTransform().setOrientation((float)Math.toDegrees(Math.atan2(velocity.y, velocity.x)));
				}
				
				@Override
				public void destroy(Entity self) {
					
				}
			});
	    	 
	    	 scene.add(particle);
	     }
	     
	     int n = behaviors.size();
	     for(int i = 0; i < n; i++){
			Behavior behavior = behaviors.get(i);
			behavior.destroy(this);
	     }
	}
	
	public int getEntityType(){
		return Masks.Entities.ENEMY;
	}
	
	public void addBehavior(Behavior behavior){
		behaviors.add(behavior);
	}
	
	public int getKillValue(){
		return killValue;
	}
	
	public Vector4f getBaseColor(){
		return baseColor;
	}
	
	public static Enemy createSeeker(Vector2f position, Scene scene){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(0, 1, 1, 1);
		
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
		
	    enemy.addBehavior(new FollowerBehavior(scene));
	    enemy.killValue = 10;
	    return enemy;
	}

	public static Enemy createWanderer(Vector2f position, Scene scene){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(1, 1, 0, 1);
	    enemy.addBehavior(new WandererBehavior(scene));
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
		
	    return enemy;
	}

	public static Enemy createFollower(Vector2f position, Scene scene){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(0.5f, 0.1f, 0.75f, 1);
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
		
	    enemy.addBehavior(new StalkerBehaviour(scene));
	    enemy.killValue = 15;
	    enemy.radius = 10;
	    return enemy;
	}

	public static Enemy createChaser(Vector2f position, Scene scene){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(0.85f, 0.54f, 0, 1);
		enemy.mesh = new Mesh();
		
		LinePrimitive l1 = new LinePrimitive();
		l1.addVertex(new Vector2f(-3, -3), enemy.baseColor);
		l1.addVertex(new Vector2f(0, 3.5f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(0, 3.5f), enemy.baseColor);
		l1.addVertex(new Vector2f(3, -3), enemy.baseColor);
		
		l1.addVertex(new Vector2f(3, -3), enemy.baseColor);
		l1.addVertex(new Vector2f(-3, -3), enemy.baseColor);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(l1);
		
		enemy.mesh.addLayer(layer);
		
		
	    enemy.addBehavior(new ChaserBehaviour(scene));
	    enemy.killValue = 5;
	    enemy.radius = 10;
	    return enemy;
	}

	public static Enemy createPrototype(Vector2f position, Scene scene){
		Player player = scene.getPlayer();
		Enemy enemy = new Enemy(scene);
		
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(1, 0.44f, 0, 1);
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
		
	    enemy.transform.setOrientation(player.getTransform().getTranslation().sub(position).direction());
	    enemy.addBehavior(new PrototypeBehaviour(scene));
	    enemy.killValue = 25;
	    enemy.radius = 15;
	    return enemy;
	}
	
	public static Enemy createBlackHole(Vector2f position, Scene scene){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		
		enemy.baseColor = new Vector4f(1, 1, 1, 1);
		enemy.mesh = new Mesh();
	    enemy.radius = 16;
	    enemy.groupMask = Masks.Collision.ENEMY | Masks.Collision.BLACK_HOLE;
	    enemy.collisionMask |= Masks.Collision.MULTIPLIER | Masks.Collision.ENEMY;
	    
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
	    enemy.killValue = 200;
	    enemy.addBehavior(new BlackHoleBehaviour(scene));
	    enemy.health = 8;
	    
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
	    
	    //TODO add proper blackhole -> enemy collision
	    
	    return enemy;
	}

	@Override
	public int getRadius() {
		return radius;
	}

	@Override
	public int getGroupMask() {
		return groupMask;
	}

	@Override
	public int getCollisionMask() {
		return collisionMask;
	}
	
}