package com.vecdef.ai;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.CubicInterpolator;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.model.SplitterModel;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemyFactory;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;
import com.vecdef.util.Masks;

public class SplitterBehaviour extends Behaviour {

	static int ROTATION_COUNTER = 0;
	
	EnemyFactory factory;
	
	final Vector2f UP_VEC = new Vector2f(0, 1);
	
	final float MOVEMENT_FORCE = 7.0f;
	final Vector2f VELOCITY_DAMPING = new Vector2f(0.940f, 0.940f);
	
	final int STATE_MOVE = 1;
	final int STATE_SWITCH = 2;
	
	CubicInterpolator interpolator = new CubicInterpolator(new Vector2f(0, 0.5f), new Vector2f(0.5f, 1.0f));
	
	Vector2f direction = new Vector2f(UP_VEC);
	Vector2f nextDirection = new Vector2f(UP_VEC);
	int state;
	
	int moveTime = 35;
	int switchTime = 10;
	
	Timer moveTimer = new Timer(moveTime);
	Timer switchTimer = new Timer(switchTime);
	
	public SplitterBehaviour(Scene scene, EnemyFactory factory, Enemy enemy) {
		super(scene, enemy);
		this.factory = factory;
		moveTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				switchTimer.restart();
				switchDir();
			}
		});
		
		switchTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				direction.set(nextDirection);
				moveTimer.restart();
				move();
			}
		});
		switchTimer.start();
		state = STATE_SWITCH;
		ROTATION_COUNTER++;
		float angle = ROTATION_COUNTER * -90;
		nextDirection.set(nextDirection.rotate(angle));
		self.getTransform().setOrientation(nextDirection.direction());
	}

	void move(){
		state = STATE_MOVE;
		float theta = direction.direction();
		float ax = FastMath.cosd(theta) * (MOVEMENT_FORCE * (14.0f / (float)self.getRadius()));
		float ay = FastMath.sind(theta) * (MOVEMENT_FORCE * (14.0f / (float)self.getRadius()));
		Vector2f acceleration = self.getAcceleration();
		acceleration.set(ax, ay);
	}
	
	void switchDir(){
		state = STATE_SWITCH;
		
		Vector2f position = self.getTransform().getTranslation();
		Player player = scene.getNearestPlayer(position.x, position.y);
		
		if((self.getEntityType() & Masks.Entities.SPAWNABLE) == Masks.Entities.SPAWNABLE){
			float rand = FastMath.random();
			if(rand > 0.5f){
				nextDirection.set(direction.rotate(-90));
			} else {
				nextDirection.set(direction.rotate(90));
			}
		} else {
			if(player.isAlive()){
				Vector2f playerPosition = player.getTransform().getTranslation();
				float dx = playerPosition.x - position.x;
				float dy = playerPosition.y - position.y;
				if(Math.abs(dx) > Math.abs(dy)){
					nextDirection.set(1 * Math.signum(dx), 0);
				} else {
					nextDirection.set(0, 1 * Math.signum(dy));
				}
			}
		}
		
		
	}
	
	@Override
	public void create() {
		
	}

	@Override
	public void update() {
		moveTimer.tick();
		switchTimer.tick();
		
		Grid grid = scene.getGrid();
		int gridWidth = grid.getWidth();
		int gridHeight = grid.getHeight();
		
		if(state == STATE_SWITCH){
			Vector2f dir = new Vector2f();
			dir.x = interpolator.interpolate(direction.x, nextDirection.x, switchTimer.percentComplete());
			dir.y = interpolator.interpolate(direction.y, nextDirection.y, switchTimer.percentComplete());
			self.getTransform().setOrientation(dir.direction());
		}
		
		if ((self.getTransform().getTranslation().x < -gridWidth / 2) || (self.getTransform().getTranslation().x > gridWidth / 2)) {
	    	self.getTransform().getTranslation().x = FastMath.clamp(-gridWidth / 2 + 1, gridWidth / 2 - 1, self.getTransform().getTranslation().x);
	    	self.getVelocity().x *= -1.0F;
	    }

	    if ((self.getTransform().getTranslation().y < -gridHeight / 2) || (self.getTransform().getTranslation().y > gridHeight / 2)) {
	    	self.getTransform().getTranslation().y = FastMath.clamp(-gridHeight / 2 + 1, gridHeight / 2 - 1, self.getTransform().getTranslation().y);
	    	self.getVelocity().y *= -1.0F;
	    }
		
		Vector2f velocity = self.getVelocity();
		velocity.x *= VELOCITY_DAMPING.x;
		velocity.y *= VELOCITY_DAMPING.y;
	}

	@Override
	public void destroy() {
		
		if((self.getEntityType() & Masks.Entities.SPAWNABLE) == Masks.Entities.SPAWNABLE)
			return;
		
		Vector2f position = self.getTransform().getTranslation();
		float offset = SplitterModel.SIZE / 2;
		Enemy enemyBL = factory.createSplitter(new Vector2f(position.x - offset, position.y - offset));
		Enemy enemyBR = factory.createSplitter(new Vector2f(position.x + offset, position.y - offset));
		Enemy enemyTR = factory.createSplitter(new Vector2f(position.x + offset, position.y + offset));
		Enemy enemyTL = factory.createSplitter(new Vector2f(position.x - offset, position.y + offset));
		
		enemyBL.getTransform().getScale().set(0.5f, 0.5f);
		enemyBL.setRadius(enemyBL.getRadius() / 2);
		
		enemyBR.getTransform().getScale().set(0.5f, 0.5f);
		enemyBR.setRadius(enemyBL.getRadius() / 2);
		
		enemyTR.getTransform().getScale().set(0.5f, 0.5f);
		enemyTR.setRadius(enemyBL.getRadius() / 2);
		
		enemyTL.getTransform().getScale().set(0.5f, 0.5f);
		enemyTL.setRadius(enemyBL.getRadius() / 2);
		
		enemyBL.setEntityType(Masks.Entities.ENEMY | Masks.Entities.SPAWNABLE);
		enemyBR.setEntityType(Masks.Entities.ENEMY | Masks.Entities.SPAWNABLE);
		enemyTR.setEntityType(Masks.Entities.ENEMY | Masks.Entities.SPAWNABLE);
		enemyTL.setEntityType(Masks.Entities.ENEMY | Masks.Entities.SPAWNABLE);
		
	}
	
}
