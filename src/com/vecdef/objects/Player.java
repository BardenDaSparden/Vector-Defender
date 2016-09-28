package com.vecdef.objects;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.barden.input.Joystick;
import org.javatroid.audio.AudioPlayer;
import org.javatroid.audio.Sound;
import org.javatroid.core.Resources;
import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.CubicInterpolator;
import org.javatroid.math.FastMath;
import org.javatroid.math.Interpolator;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.model.PlayerModel;
import com.vecdef.model.ShieldModel;
import com.vecdef.util.Masks;

public class Player extends Entity{
	
	static final DecimalFormat FLOAT_FORMATTER = new DecimalFormat("##.##");
	
	static final float MOVEMENT_THRESHOLD = 0.20f;		//Threshold value for LEFT Analog Stick
	static final float REORIENTATION_THRESHOLD = 0.2f;	//Threshold value for RIGHT Analog Stick
	static final float MAX_MOVEMENT_SPEED = 7.3F;		//Max player movement speed
	static final float VELOCITY_DAMPING = 0.945F;		//Controls the "slipperiness" of ship movement. Within range of [0, 0.99999999], closer to 1 the more slippery
	static final float MOVEMENT_ACCELERATION = 0.45F;	//How much acceleration is applied each frame in the direction of the LEFT Analog Stick
	
	Vector2f weaponOffset1 = new Vector2f(25, 11);
	Vector2f weaponOffset2 = new Vector2f(25, -11);
	float aimDirection = 0;
	
	Joystick gamepad;
	
	Interpolator interpolator = new CubicInterpolator(new Vector2f(0.35f, 0.0f), new Vector2f(1, 0.65f));
	
	ArrayList<Entity> allEnemies;
	
	//Possible refactor for new firing modes
	boolean useOffset1 = true;
	float bulletSpeed = 17;
	Timer weaponTimer = new Timer(5);
	Timer respawnTimer = new Timer(35);
	boolean canUseWeapon = false;
	
	boolean canBeKilled = true;
	
	float time = 0.0f;
	PlayerStats stats;
	
	Vector2f orientation = new Vector2f(0, 1);
	Vector2f oldOrientation = new Vector2f(0, 1);
	Vector2f newOrientation = new Vector2f(0, 1);
	
	boolean shieldAdded = false;
	ShieldComponent shield;
	
	Sound fireSound;
	
	public Player(Scene scene, Joystick gamepad){
		super(scene);
		
		this.gamepad = gamepad;
		
		model = PlayerModel.get();
		transform.setTranslation(new Vector2f(0, 0));
		
		weaponTimer.setCallback(new TimerCallback() {
			public void execute(Timer timer) {
				canUseWeapon = true;
			}
		});
		weaponTimer.start();
		
		respawnTimer.setCallback(new TimerCallback() {
			public void execute(Timer timer) {
				respawn();
				canBeKilled = true;
			}
		});
		respawnTimer.start();
		
		addContactListener(new ContactEventListener() {
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				if((other.getGroupMask() & Masks.Collision.ENEMY) == Masks.Collision.ENEMY){
					if(canBeKilled)
						kill();
				} else if((other.getGroupMask() & Masks.Collision.MULTIPLIER) == Masks.Collision.MULTIPLIER){
					stats.increaseMultiplier();
				}
			}
		});
		
		allEnemies = new ArrayList<Entity>();
		stats = new PlayerStats();
		shield = new ShieldComponent(scene);
		velocity.set(0, 0.00001f);
		
		fireSound = Resources.getSound("fire1");
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
		
		float lax = (float)gamepad.getLeftX();
		float lay = (float)gamepad.getLeftY();
		float rax = (float)gamepad.getRightX();
		float ray = (float)gamepad.getRightY();
		boolean bTrigger = (gamepad.getLeftTrigger() > - 0.75f) || (gamepad.getRightTrigger() > -0.75f);//(gamepad.isLeftTriggerDown(0.15f)) || (gamepad.isRightTriggerDown(0.15f));
		
		Vector2f movementAcceleration = new Vector2f();
		
		boolean canAccelerate = false;
		boolean canShoot = false;
		
		//Using left analog stick, calculate movement direction
		//System.out.println("Left : " + lax + ", " + lay);
		if(Math.abs(lax) > MOVEMENT_THRESHOLD){
			movementAcceleration.x = lax * MOVEMENT_ACCELERATION;
			movementAcceleration.y = lay * MOVEMENT_ACCELERATION;
			canAccelerate = true;
		}
		
		if(Math.abs(lay) > MOVEMENT_THRESHOLD){
			movementAcceleration.x = lax * MOVEMENT_ACCELERATION;
			movementAcceleration.y = lay * MOVEMENT_ACCELERATION;
			canAccelerate = true;
		}
		
		//Using right analog stick, calculate ship orientation / weapon aim direction
		//System.out.println("Right : " + rax + ", " + ray);
		newOrientation.x = rax;
		newOrientation.y = ray;
		if(Math.abs(rax) >= REORIENTATION_THRESHOLD || Math.abs(ray) >+ REORIENTATION_THRESHOLD){
			oldOrientation.x = newOrientation.x;
			oldOrientation.y = newOrientation.y;
			canShoot = true;
		}
		
		aimDirection = oldOrientation.direction();
		
		//Update physics related variables
	    if(canAccelerate)
			acceleration.set(movementAcceleration);
		
		//Player ship abilities. Currently "fire weapon", and "use shield"
	    if (canShoot && canUseWeapon){
	    	fireWeapon();
	    }
	    
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
	    
		transform.setOrientation(velocity.direction());
		shield.getTransform().getTranslation().set(transform.getTranslation()); //Link shield position, to player ship
		
	    velocity.set(velocity.scale(VELOCITY_DAMPING));
	    velocity.x = FastMath.clamp(-MAX_MOVEMENT_SPEED, MAX_MOVEMENT_SPEED, velocity.x);
	    velocity.y = FastMath.clamp(-MAX_MOVEMENT_SPEED, MAX_MOVEMENT_SPEED, velocity.y);
	    
	    //Clamp position to grid bounds. HACKY!!!
	    int gridWidth = scene.getGrid().getWidth();
	    int gridHeight = scene.getGrid().getHeight();
	    if ((getTransform().getTranslation().x < -gridWidth / 2) || (getTransform().getTranslation().x > gridWidth / 2)) 
	    	getTransform().getTranslation().x = FastMath.clamp(-gridWidth / 2 + 1, gridWidth / 2 - 1, getTransform().getTranslation().x);
	    if ((getTransform().getTranslation().y < -gridHeight / 2) || (getTransform().getTranslation().y > gridHeight / 2)) 
	    	getTransform().getTranslation().y = FastMath.clamp(-gridHeight / 2 + 1, gridHeight / 2 - 1, getTransform().getTranslation().y);
	    
	    //Apply movement force to grid
	    scene.getGrid().applyImplosiveForce(velocity.length() * 3.0f, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0), 100);
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
	    AudioPlayer.instance().play(fireSound);
	}

	public void respawn(){
		scene.getGrid().applyExplosiveForce(300, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 200);
	}
	
	public void reset(){
		stats.reset();
	}
	
	public void kill(){
		canBeKilled = false;
		respawnTimer.restart();
		
		acceleration.set(0, 0);
		velocity.set(0, 0.00001f); 
	    
	    allEnemies.clear();
		scene.getEntitiesByType(Masks.Entities.ENEMY, allEnemies);
		scene.getGrid().applyExplosiveForce(500, new Vector3f(transform.getTranslation().x, transform.getTranslation().y, 0.0F), 200.0F);
		
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
	
	public boolean isVisible(){
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
	final int MAX_RADIUS = 48;
	
	boolean active = false;
	int radius = 0;
	
	Interpolator inter = new CubicInterpolator(new Vector2f(0.1f, 0.9f), new Vector2f(0.8f, 1));
	
	public ShieldComponent(Scene scene) {
		super(scene);
		this.model = ShieldModel.get();
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
		
		if(active){
			radius = (int) inter.interpolate(radius, MAX_RADIUS, 0.07f);
			
			float amt = 40;
			Vector3f position = new Vector3f(0, 0, 0);
			Vector3f force = new Vector3f(0, 0, 0);
			for(int i = 0; i < 32; i++){
				float theta = (float)Math.PI * 2 * ((float)i / 32.0f);
				float fx = FastMath.cos(theta);
				float fy = FastMath.sin(theta);
				force.set(fx * amt, fy * amt, 0);
				position.set(transform.getTranslation().x + fx * radius,transform.getTranslation().y + fy * radius, 0);
				scene.getGrid().applyDirectedForce(force, position, amt);
			}
			
		} else {
			radius = (int) inter.interpolate(radius, MIN_RADIUS, 0.07f);
		}
		
		radius = (int) FastMath.clamp(MIN_RADIUS, MAX_RADIUS, radius);
		float sFactor = (float)radius / (float)MAX_RADIUS;
		transform.getScale().set(sFactor, sFactor);
		
		opacity = Math.max((float)(radius) / (float)MAX_RADIUS, 0);
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