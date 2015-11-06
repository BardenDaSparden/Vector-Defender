package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.CubicInterpolator;
import org.javatroid.math.FastMath;
import org.javatroid.math.Interpolator;
import org.javatroid.math.SineInterpolator;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.javatroid.math.Vector4f;

import com.toolkit.inputstate.Gamepad;
import com.vecdef.gamestate.Scene;
import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;

public class Player extends Entity{
	
	private static final float MOVEMENT_DEADZONE = 0.25f;
	private static final float ORIENTATION_DEADZONE = 0.2f;
	
	private static final float MAX_SPEED = 10F;
	private static final float VELOCITY_DAMPING = 0.97F;
	private static final float THRUST = 0.75F;
	
	final float BODY_WIDTH = 17;
	final float BODY_HEIGHT = 9;
	
	Mesh mesh;
	Vector4f bodyColor = new Vector4f(1, 1, 1, 1);
	Vector4f wingColor = new Vector4f(1, 0.4f, 1, 1);
	Vector4f nextWingColor = new Vector4f(1, 1, 1, 1);
	
	Vector4f bulletColor = new Vector4f(0.7f, 0.4f, 1, 1);
	Vector2f weaponOffset1 = new Vector2f(25, 11);
	Vector2f weaponOffset2 = new Vector2f(25, -11);
	float aimDirection = 0;
	
	//Vector2f moveToSpawn = new Vector2f();
	Interpolator interpolator = new CubicInterpolator(new Vector2f(0.35f, 0.0f), new Vector2f(1, 0.65f));
	
	ArrayList<Entity> allEnemies;
	
	//Possible refactor for new firing modes
	boolean useOffset1 = true;
	float bulletSpeed = 19;
	Timer weaponTimer = new Timer(3);
	Timer respawnTimer = new Timer(45);
	boolean canUseWeapon = false;
	
	float time = 0.0f;
	PlayerStats stats;
	
	Vector2f orientation = new Vector2f(0, 1);
	Vector2f newOrientation = new Vector2f(0, 1);
	
	boolean shieldAdded = false;
	boolean bShield = false;
	ShieldComponent shield;
	
	Gamepad gamepad;
	
	public Player(Scene scene, Gamepad gamepad){
		super(scene);
		this.gamepad = gamepad;
		
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
		
		addContactListener(new ContactEventListener() {
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				if((other.getGroupMask() & Masks.Collision.ENEMY) == Masks.Collision.ENEMY){
					kill();
				} else if((other.getGroupMask() & Masks.Collision.MULTIPLIER) == Masks.Collision.MULTIPLIER){
					stats.increaseMultiplier();
				}
			}
		});
		
		allEnemies = new ArrayList<Entity>();
		
		stats = new PlayerStats();
		
		shield = new ShieldComponent(scene);
	}
	
	public void update(){
		
		if(!shieldAdded){
			scene.add(shield);
			shieldAdded = true;
		}
		
		weaponTimer.tick();
		respawnTimer.tick();
		
		if(isDead()){
			transform.setTranslation(interpolator.interpolate(transform.getTranslation(), new Vector2f(0, 0), respawnTimer.percentComplete()));
			return;
		}
		
		float lax = (float)gamepad.getLeftStick().getX();
		float lay = (float)gamepad.getLeftStick().getY();
		float rax = (float)gamepad.getRightStick().getX();
		float ray = (float)gamepad.getRightStick().getY();
		
		boolean bRightBumper = gamepad.isButtonPressed(Gamepad.RB_BUTTON);
		boolean bLeftBumper = gamepad.isButtonPressed(Gamepad.LB_BUTTON);
		boolean bTrigger = (gamepad.getLeftTrigger().getX() > 0.15f) || (gamepad.getRightTrigger().getX() > 0.15f);
		
		Vector2f direction = new Vector2f();
		
		boolean setAccel = false;
		boolean bShoot = false;
		
		if(Math.abs(lax) > MOVEMENT_DEADZONE){
			direction.x = lax;
			setAccel = true;
		}
		if(Math.abs(lay) > MOVEMENT_DEADZONE){
			direction.y = lay;
			setAccel = true;
		}
		
		if(Math.abs(rax) > ORIENTATION_DEADZONE){
			newOrientation.x = rax;
			bShoot = true;
		}
		
		if(Math.abs(ray) > ORIENTATION_DEADZONE){
			newOrientation.y = ray;
			bShoot = true;
		}
		
		direction.normalizei();
		if(setAccel)
			acceleration.set(direction.scale(THRUST));
		
		aimDirection = newOrientation.direction();
		transform.setOrientation(velocity.direction());
			
		if ((bRightBumper || bLeftBumper) && stats.getBombCount() > 0){
			useBomb();
		}

	    if (bShoot && canUseWeapon){
	    	fireWeapon();
	    }
	    
	    velocity.set(velocity.scale(VELOCITY_DAMPING));
	    velocity.x = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.x);
	    velocity.y = FastMath.clamp(-MAX_SPEED, MAX_SPEED, velocity.y);
	    
	    int gridWidth = scene.getGrid().getWidth();
	    int gridHeight = scene.getGrid().getHeight();
	    
	    if ((getTransform().getTranslation().x < -gridWidth / 2) || (getTransform().getTranslation().x > gridWidth / 2)) {
	    	getTransform().getTranslation().x = FastMath.clamp(-gridWidth / 2 + 1, gridWidth / 2 - 1, getTransform().getTranslation().x);
	    }

	    if ((getTransform().getTranslation().y < -gridHeight / 2) || (getTransform().getTranslation().y > gridHeight / 2)) {
	    	getTransform().getTranslation().y = FastMath.clamp(-gridHeight / 2 + 1, gridHeight / 2 - 1, getTransform().getTranslation().y);
	    }
	    
	    scene.getGrid().applyExplosiveForce(velocity.length() * 2.5f, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0), 100);
	    shield.getTransform().getTranslation().set(transform.getTranslation());
	    if(bTrigger){
	    	if(stats.hasEnergy(5)){
		    	shield.activate();
		    	stats.useEnergy(5);
	    	} else {
	    		shield.deactivate();
	    	}
	    } else {
	    	shield.deactivate();
	    }
	}
	
	private void useBomb(){
		allEnemies.clear();
		scene.getEntitiesByType(Masks.Entities.ENEMY, allEnemies);
		
		int n = allEnemies.size();
	    for(int i = 0; i < n; i++){
	    	Entity entity = allEnemies.get(i);
	    	entity.expire();
	    }
	    
	    stats.useBomb();
	}
	
	private void fireWeapon(){
		float direction = aimDirection;
		
		Vector2f bulletOffset = (useOffset1) ? weaponOffset1 : weaponOffset2 ;
		Vector2f bulletPosition = bulletOffset.rotate(direction).add(transform.getTranslation());
		Vector2f bulletVelocity = new Vector2f(FastMath.cosd(direction) * bulletSpeed, FastMath.sind(direction) * bulletSpeed);
        
		Bullet bullet = new Bullet(bulletPosition, bulletVelocity, scene);
        scene.add(bullet);
		
	    useOffset1 = !useOffset1;
	    canUseWeapon = false;
	    weaponTimer.restart();
	}

	public void respawn(){
		scene.getGrid().applyExplosiveForce(500, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 250);
	}
	
	public void reset(){
		stats.reset();
	}
	
	public void kill(){
		respawnTimer.restart();
		
		acceleration.set(0, 0);
		velocity.set(0, 0); 
	    
	    allEnemies.clear();
		scene.getEntitiesByType(Masks.Entities.ENEMY, allEnemies);
		scene.getGrid().applyExplosiveForce(100, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 1000.0F);
		
		int n = allEnemies.size();
	    for(int i = 0; i < n; i++){
	    	Entity entity = allEnemies.get(i);
	    	entity.expire();
	    }
	    
	    stats.useLife();
	    stats.resetMultiplier();
	    
	}
	
	public boolean isDead(){
		return respawnTimer.percentComplete() < 1.0f;
	}
	
	public boolean isDrawn(){
		return !isDead();
	}
	
	public void registerBulletKill(Enemy e){
		stats.addScore(e.getKillValue());
		stats.addEnergy(e.getEnergyValue());
	}
	
	public void destroy(){
		
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
	public int getGroupMask() {
		return Masks.Collision.PLAYER;
	}

	@Override
	public int getCollisionMask() {
		return Masks.Collision.ENEMY | Masks.Collision.MULTIPLIER;
	}
	
}

class ShieldComponent extends Entity{
	
	final int MIN_RADIUS = 0;
	final int MAX_RADIUS = 80;
	
	boolean active = false;
	int radius = 0;
	
	Interpolator inter = new CubicInterpolator(new Vector2f(0.3f, 0), new Vector2f(0.7f, 1));
	
	public ShieldComponent(Scene scene) {
		super(scene);
		
		this.mesh = new Mesh();
		Vector4f white = new Vector4f(1, 1, 1, 1);
		Vector4f cyan = new Vector4f(0, 1, 1, 1);
		Vector4f purple = new Vector4f(0.45f, 0, 1, 1);
	    int segments = 36;
	    LinePrimitive circle = new LinePrimitive();
	    
	    for(int i = 0; i < segments; i++){
	    	float a1 = (float)i / (float) segments * 360f;
	    	float a2 = (float)((i + 1) % segments) / (float)segments * 360f;
	    	
	    	Vector2f v0 = new Vector2f(FastMath.cosd(a1) * MAX_RADIUS, FastMath.sind(a1) * MAX_RADIUS);
	    	Vector2f v1 = new Vector2f(FastMath.cosd(a2) * MAX_RADIUS, FastMath.sind(a2) *MAX_RADIUS);
	    	circle.addVertex(v0, white);
	    	circle.addVertex(v1, white);
	    }
	    
	    MeshLayer bodyLayer = new MeshLayer();
	    
	    LinePrimitive circle2 = new LinePrimitive();
	    
	    for(int i = 0; i < segments; i++){
	    	float a1 = (float)i / (float) segments * 360f;
	    	float a2 = (float)((i + 1) % segments) / (float)segments * 360f;
	    	
	    	Vector2f v0 = new Vector2f(FastMath.cosd(a1) * MAX_RADIUS * 0.995f, FastMath.sind(a1) * MAX_RADIUS * 0.995f);
	    	Vector2f v1 = new Vector2f(FastMath.cosd(a2) * MAX_RADIUS  * 0.995f, FastMath.sind(a2) * MAX_RADIUS  * 0.995f);
	    	circle.addVertex(v0, cyan);
	    	circle.addVertex(v1, cyan);
	    }
	    
	    LinePrimitive circle3 = new LinePrimitive();
	    
	    for(int i = 0; i < segments; i++){
	    	float a1 = (float)i / (float) segments * 360f;
	    	float a2 = (float)((i + 1) % segments) / (float)segments * 360f;
	    	
	    	Vector2f v0 = new Vector2f(FastMath.cosd(a1) * MAX_RADIUS * 0.985f, FastMath.sind(a1) * MAX_RADIUS * 0.985f);
	    	Vector2f v1 = new Vector2f(FastMath.cosd(a2) * MAX_RADIUS  * 0.985f, FastMath.sind(a2) * MAX_RADIUS  * 0.985f);
	    	circle.addVertex(v0, purple);
	    	circle.addVertex(v1, purple);
	    }
	    
	    bodyLayer.addPrimitive(circle);
	    bodyLayer.addPrimitive(circle2);
	    bodyLayer.addPrimitive(circle3);
	    mesh.addLayer(bodyLayer);
	}

	@Override
	public int getGroupMask() {
		return Masks.Collision.ABILITY;
	}

	@Override
	public int getCollisionMask() {
		return Masks.Collision.ENEMY;
	}

	public void activate(){
		active = true;
	}
	
	public void deactivate(){
		active = false;
	}
	
	@Override
	public void update() {
		scene.getGrid().applyImplosiveForce(250, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0), radius);
		
		if(active){
			radius = (int) inter.interpolate(radius, MAX_RADIUS, 0.2f);
		} else {
			radius = (int) inter.interpolate(radius, MIN_RADIUS, 0.2f);
		}
		
		radius = (int) FastMath.clamp(MIN_RADIUS, MAX_RADIUS, radius);
		float sFactor = (float)radius / (float)MAX_RADIUS;
		transform.getScale().set(sFactor, sFactor);
		
		opacity = Math.max((float)(2 * radius - 35) / (float)MAX_RADIUS, 0);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	public int getRadius(){
		return radius;
	}
	
	@Override
	public int getEntityType() {
		return Masks.Entities.OTHER;
	}
	
}