package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.core.Input;
import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.CubicInterpolator;
import org.javatroid.math.FastMath;
import org.javatroid.math.Interpolator;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.javatroid.math.Vector4f;

import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;

public class Player extends Entity{
	
	private static final float MAX_SPEED = 8.0F;
	private static final float VELOCITY_DAMPING = 0.975F;
	private static final float THRUST = 0.75F;
	
	private static final int MOVE_LEFT = Input.KEY_A;
	private static final int MOVE_RIGHT = Input.KEY_D;
	private static final int MOVE_UP = Input.KEY_W;
	private static final int MOVE_DOWN = Input.KEY_S;
	
	private static final int USE_WEAPON = 0;
	private static final int USE_BOMB = 1;
	
	private static final int FIRING_MODE_0 = 0;
	private static final int FIRING_MODE_1 = 1;
	private static final int FIRING_MODE_2 = 2;
	
	static final float BODY_WIDTH = 17;
	static final float BODY_HEIGHT = 9;
	
	Mesh mesh;
	Vector4f bodyColor = new Vector4f(1, 1, 1, 1);
	Vector4f wingColor = new Vector4f(1, 0.4f, 1, 1);
	Vector4f nextWingColor = new Vector4f(1, 1, 1, 1);
	
	Vector4f bulletColor = new Vector4f(0.7f, 0.4f, 1, 1);
	Vector2f weaponOffset1 = new Vector2f(25, 11);
	Vector2f weaponOffset2 = new Vector2f(25, -11);
	
	Vector2f moveToSpawn = new Vector2f();
	Interpolator interpolator = new CubicInterpolator(new Vector2f(0.35f, 0.0f), new Vector2f(1, 0.65f));
	
	//Possible refactor for new firing modes
	boolean useOffset1 = true;
	float bulletSpeed = 15;
	Timer weaponTimer = new Timer(5);
	Timer respawnTimer = new Timer(60);
	int firingMode = FIRING_MODE_0;
	boolean canUseWeapon = false;
	
	float time = 0.0f;
	Grid grid;
	PlayerStats stats;
	
	public Player(){
		transform.setTranslation(new Vector2f(0, 0));
		
		mesh = new Mesh();
		
		LinePrimitive body = new LinePrimitive();
		body.addVertex(new Vector2f(-BODY_WIDTH / 2f, 0), bodyColor);
		body.addVertex(new Vector2f(0, BODY_HEIGHT / 2f), bodyColor);
		
		body.addVertex(new Vector2f(0, BODY_HEIGHT / 2f), bodyColor);
		body.addVertex(new Vector2f(BODY_WIDTH / 2F, 0), bodyColor);
		
		body.addVertex(new Vector2f(BODY_WIDTH / 2F, 0), bodyColor);
		body.addVertex(new Vector2f(0, -BODY_HEIGHT / 2f), bodyColor);
		
		body.addVertex(new Vector2f(0, -BODY_HEIGHT / 2f), bodyColor);
		body.addVertex(new Vector2f(-BODY_WIDTH / 2f, 0), bodyColor);
		
		LinePrimitive l1 = new LinePrimitive();
		l1.addVertex(new Vector2f(-BODY_WIDTH / 2f + 1, -4), wingColor);
		l1.addVertex(new Vector2f(2, -BODY_HEIGHT / 2f - 4), wingColor);
		
		l1.addVertex(new Vector2f(-BODY_WIDTH / 2f + 2, -8), wingColor);
		l1.addVertex(new Vector2f(3, -BODY_HEIGHT / 2f - 8), wingColor);
		
		l1.addVertex(new Vector2f(-BODY_WIDTH / 2f + 3, -12), wingColor);
		l1.addVertex(new Vector2f(4, -BODY_HEIGHT / 2f - 12), wingColor);
		
		l1.addVertex(new Vector2f(-BODY_WIDTH / 2f + 1, 4), wingColor);
		l1.addVertex(new Vector2f(2, BODY_HEIGHT / 2f + 4), wingColor);
		
		l1.addVertex(new Vector2f(-BODY_WIDTH / 2f + 2, 8), wingColor);
		l1.addVertex(new Vector2f(3, BODY_HEIGHT / 2f + 8), wingColor);
		
		l1.addVertex(new Vector2f(-BODY_WIDTH / 2f + 3, 12), wingColor);
		l1.addVertex(new Vector2f(4, BODY_HEIGHT / 2f + 12), wingColor);
		
		
		MeshLayer layer0 = new MeshLayer();
		layer0.addPrimitive(body);
		layer0.addPrimitive(l1);
		
		mesh.addLayer(layer0);
		
		weaponTimer.setCallback(new TimerCallback() {
			public void execute(Timer timer) {
				canUseWeapon = true;
			}
		});
		weaponTimer.start();
		
		respawnTimer.setCallback(new TimerCallback() {
			public void execute(Timer timer) {
				respawn();
			}
		});
		respawnTimer.start();
		
		stats = new PlayerStats();
	}
	
	public void update(Grid grid){
		
		if(this.grid == null)
			this.grid = grid;
		
		weaponTimer.tick();
		respawnTimer.tick();
		
		if(isDead()){
			transform.setTranslation(interpolator.interpolate(transform.getTranslation(), new Vector2f(0, 0), respawnTimer.percentComplete()));
			return;
		}
		
		if(Input.isKeyDown(MOVE_UP))
			acceleration.y = THRUST;
		
		if(Input.isKeyDown(MOVE_DOWN))
			acceleration.y = -THRUST;
		
		if(Input.isKeyDown(MOVE_LEFT))
			acceleration.x = -THRUST;
		
		if(Input.isKeyDown(MOVE_RIGHT)){
			acceleration.x = THRUST;
		}
		
		if ((Input.isMouseButtonPressed(USE_BOMB)) && stats.getBombCount() > 0){
			useBomb();
		}

	    if ((Input.isMouseButtonDown(USE_WEAPON)) && canUseWeapon){
	    	fireWeapon();
	    }
	    
	    velocity.set(velocity.scale(VELOCITY_DAMPING));
	    velocity.x = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.x);
	    velocity.y = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.y);
	}
	
	@SuppressWarnings("unchecked")
	private void useBomb(){
		Vector3f pos = new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F);
		grid.applyExplosiveForce(1200.0F, pos, 1000.0F);
	    
	    ArrayList<Enemy> enemies = (ArrayList<Enemy>) EntityManager.getEntities(Enemy.class);
	    for(int i = 0; i < enemies.size(); i++){
	    	Enemy e = enemies.get(i);
	    	e.destroy();
	    }
	    
	    stats.useBomb();
	}
	
	public void collision(Entity other){
		if(other instanceof Enemy){
			destroy();
		} else if(other instanceof MultiplierPiece){
			stats.increaseMultiplier();
		}
	}
	
	private void fireWeapon(){
		
		if(firingMode == FIRING_MODE_0){
			float direction = transform.getOrientation();
			
			Vector2f bulletOffset = (useOffset1) ? weaponOffset1 : weaponOffset2 ;
			Vector2f bulletPosition = bulletOffset.rotate(direction).add(transform.getTranslation());
			Vector2f bulletVelocity = new Vector2f(FastMath.cosd(direction) * bulletSpeed, FastMath.sind(direction) * bulletSpeed);
	        
			Bullet bullet = new Bullet(bulletPosition, bulletVelocity);
	        EntityManager.add(bullet);
			
		    useOffset1 = !useOffset1;
		    canUseWeapon = false;
		    weaponTimer.restart();
		} else if(firingMode == FIRING_MODE_1){
			float direction = transform.getOrientation();
			Vector2f offset = new Vector2f(20, -12);
			Vector2f offset2 = new Vector2f(15, -24);
			Vector2f offset3 = new Vector2f(20, 12);
			Vector2f offset4 = new Vector2f(15, 24);
			
			Vector2f o1 = offset.rotate(direction).add(transform.getTranslation());
			Vector2f o2 = offset2.rotate(direction).add(transform.getTranslation());
			Vector2f o3 = offset3.rotate(direction).add(transform.getTranslation());
			Vector2f o4 = offset4.rotate(direction).add(transform.getTranslation());
			
			Vector2f bulletVelocity = new Vector2f(FastMath.cosd(direction) * bulletSpeed, FastMath.sind(direction) * bulletSpeed);
			
			Bullet bullet = new Bullet(o1, bulletVelocity);
	        EntityManager.add(bullet);
			
	        Bullet bullet2 = new Bullet(o2, bulletVelocity);
	        EntityManager.add(bullet2);
	        
	        Bullet bullet3 = new Bullet(o3, bulletVelocity);
	        EntityManager.add(bullet3);
	        
	        Bullet bullet4 = new Bullet(o4, bulletVelocity);
	        EntityManager.add(bullet4);
	        canUseWeapon = false;
		    weaponTimer.restart();
			
		} else if(firingMode == FIRING_MODE_2){
			
		}
	}

	public void respawn(){
		grid.applyDirectedForce(new Vector3f(0.0F, 0.0F, 10000.0F), new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 400.0F);
		moveToSpawn.x = 0;
		moveToSpawn.y = 0;
	}
	
	public void reset(){
		stats.reset();
	}
	
	public boolean isDead(){
		return respawnTimer.percentComplete() < 1.0f;
	}
	
	public void registerBulletKill(Enemy e){
		stats.addScore(e.getKillValue());
	}
	
	@SuppressWarnings("unchecked")
	public void destroy(){
		respawnTimer.restart();
		
	    velocity.x = 0.0F;
	    velocity.y = 0.0F;
	    moveToSpawn.x = -transform.getTranslation().x;
	    moveToSpawn.y = -transform.getTranslation().y;    
	    
	    ArrayList<Enemy> enemies = (ArrayList<Enemy>) EntityManager.getEntities(Enemy.class);
	    for(int i = 0; i < enemies.size(); i++){
	    	Enemy e = enemies.get(i);
	    	e.destroy();
	    }
	    
	    stats.useLife();
	    stats.resetMultiplier();
	}
	
	public int getEntityType(){
		return Masks.Entities.PLAYER;
	}
	
	public void look(Vector2f mousePosition){
		transform.setOrientation(mousePosition.sub(transform.getTranslation()).direction());
	}
	
	public Mesh getMesh(){
		return mesh;
	}
	
	public PlayerStats getStats(){
		return stats;
	}

	@Override
	public int getRadius() {
		return 15;
	}

	@Override
	public int getGroupMask() {
		return Masks.Collision.PLAYER;
	}

	@Override
	public int getCollisionMask() {
		return Masks.Collision.ENEMY | Masks.Collision.MULTIPLIER;
	}
	
}
