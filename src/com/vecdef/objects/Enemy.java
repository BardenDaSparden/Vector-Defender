package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
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
import com.vecdef.ai.ZoomerBehaviour;
import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;

public class Enemy extends Entity{
	
	final int WAKEUP_TIME = 60;
	
	Vector4f baseColor = new Vector4f(1, 1, 1, 1);
	ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
	Timer wakeupTimer = new Timer(WAKEUP_TIME);
	boolean bAwake = false;
	
	int health = 1;
	int killValue = 1;

	public Enemy(){
		this(new Vector2f(0, 0));
	}
	
	public Enemy(Vector2f position){
	    transform.setTranslation(position);
	    this.radius = 16;
	    
	    wakeupTimer.setCallback(new TimerCallback() {
			public void execute(Timer timer) {
				bAwake = true;
			}
		});
	    wakeupTimer.start();
	}

	public void update(Grid grid){
		wakeupTimer.tick();
		if(!bAwake)
			return;
		
		for (Behavior b : behaviors)
	        b.onUpdate(this, grid);
	}

	public void collision(Entity other){
		
		if(other instanceof Bullet){
			wasShot();
			if(bExpired){
				Player player = getScene().getPlayer();
				player.registerBulletKill(this);
			}
		}
		
		for(Behavior b : behaviors)
			b.onCollision(this, other);
	}

	public void wasShot(){
		 health -= 1;
		 if(health <= 0){
			destroy();
		 }
	}
	
	public void destroy(){
		
		 //new DestroyEffect(transform.getTranslation(), 100, 16, getBaseColor(), 6, 70);
		 
		 bExpired = true;
	     
	     int numPieces = FastMath.randomi(1, 4);
	     float av = 4;
	     float speed = 2;
	     
	     for(int i = 0; i < numPieces; i++){
	    	 MultiplierPiece piece = new MultiplierPiece(transform.getTranslation(), FastMath.random() * 360, -av / 2.0f + FastMath.random() * av);
	    	 float a = FastMath.random() * 360; 
	    	 piece.velocity = new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed);
	    	 EntityManager.add(piece);
	     }
	     
	     for(Behavior b : behaviors)
	    	 b.onDestroy(this);
	}
	
	public boolean isExpired(){
		return wakeupTimer.percentComplete() >= 1;
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
	
	public static Enemy createSeeker(Vector2f position){
		Enemy enemy = new Enemy();
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
		
	    enemy.addBehavior(new FollowerBehavior());
	    enemy.killValue = 10;
	    return enemy;
	}

	public static Enemy createWanderer(Vector2f position){
		Enemy enemy = new Enemy();
		enemy.transform.setTranslation(position);
		enemy.baseColor = new Vector4f(1, 1, 0, 1);
	    enemy.addBehavior(new WandererBehavior());
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

	public static Enemy createFollower(Vector2f position){
		Enemy enemy = new Enemy();
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
		
	    enemy.addBehavior(new StalkerBehaviour());
	    enemy.killValue = 15;
	    enemy.radius = 10.0F;
	    return enemy;
	}

	public static Enemy createChaser(Vector2f position){
		Enemy enemy = new Enemy();
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
		
		
	    enemy.addBehavior(new ChaserBehaviour());
	    enemy.killValue = 5;
	    enemy.radius = 10.0F;
	    return enemy;
	}

	public static Enemy createPrototype(Vector2f position){
		Player player = getScene().getPlayer();
		Enemy enemy = new Enemy();
		
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
	    enemy.addBehavior(new PrototypeBehaviour());
	    enemy.killValue = 25;
	    enemy.radius = 15.0F;
	    return enemy;
	}
	
	public static Enemy createZoomer(Vector2f position){
		Enemy enemy = new Enemy();
		enemy.transform.setTranslation(position);
		
		enemy.baseColor = new Vector4f(0.25f, 1, 0.25f, 1);
		enemy.mesh = new Mesh();
		
		final int W = 8;
		final int H = 8;
		final int SPACING = 4;
		
		LinePrimitive l1 = new LinePrimitive();
		l1.addVertex(new Vector2f(-W / 2f, - H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(-W / 2f,  H / 2f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(-W / 2f,  H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(W / 2f,  H / 2f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(W / 2f,  H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(W / 2f,  -H / 2f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(W / 2f,  -H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(-W / 2f,  -H / 2f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(-W / 2f, H / 2f + SPACING), enemy.baseColor);
		l1.addVertex(new Vector2f(0, H + SPACING), enemy.baseColor);
		
		l1.addVertex(new Vector2f(0, H + SPACING), enemy.baseColor);
		l1.addVertex(new Vector2f(W / 2f, H / 2f + SPACING), enemy.baseColor);
		
		l1.addVertex(new Vector2f(W / 2f, H / 2f + SPACING), enemy.baseColor);
		l1.addVertex(new Vector2f(-W / 2f, H / 2f + SPACING), enemy.baseColor);
		
		
		l1.addVertex(new Vector2f(-W / 2f, -H / 2f - SPACING), enemy.baseColor);
		l1.addVertex(new Vector2f(0, -H - SPACING), enemy.baseColor);
		
		l1.addVertex(new Vector2f(0, -H - SPACING), enemy.baseColor);
		l1.addVertex(new Vector2f(W / 2f, -H / 2f - SPACING), enemy.baseColor);
		
		l1.addVertex(new Vector2f(W / 2f, -H / 2f - SPACING), enemy.baseColor);
		l1.addVertex(new Vector2f(-W / 2f, -H / 2f - SPACING), enemy.baseColor);
		
		
		l1.addVertex(new Vector2f(-W / 2f - SPACING, -H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(-W / 2f - SPACING, H / 2f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(-W / 2f - SPACING, H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(-W - SPACING, 0), enemy.baseColor);
		
		l1.addVertex(new Vector2f(-W - SPACING, 0), enemy.baseColor);
		l1.addVertex(new Vector2f(-W / 2f - SPACING, -H / 2f), enemy.baseColor);
		
		
		l1.addVertex(new Vector2f(W / 2f + SPACING, -H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(W / 2f + SPACING, H / 2f), enemy.baseColor);
		
		l1.addVertex(new Vector2f(W / 2f + SPACING, H / 2f), enemy.baseColor);
		l1.addVertex(new Vector2f(W + SPACING, 0), enemy.baseColor);
		
		l1.addVertex(new Vector2f(W + SPACING, 0), enemy.baseColor);
		l1.addVertex(new Vector2f(W / 2f + SPACING, -H / 2f), enemy.baseColor);
		
		MeshLayer layer = new MeshLayer();
		layer.addPrimitive(l1);
		
		enemy.mesh.addLayer(layer);
		
		enemy.getTransform().setOrientation(0);
		enemy.addBehavior(new ZoomerBehaviour());
		enemy.killValue = 20;
		enemy.radius = 12;
		enemy.health = 3;
		
		return enemy;
	}
	
	public static Enemy createBlackHole(Vector2f position){
		Enemy enemy = new Enemy();
		enemy.transform.setTranslation(position);
		
		enemy.baseColor = new Vector4f(1, 1, 1, 1);
		enemy.mesh = new Mesh();
	    enemy.radius = 16;
	    
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
	    enemy.addBehavior(new BlackHoleBehaviour());
	    enemy.health = 15;
	    
	    return enemy;
	}
	
}