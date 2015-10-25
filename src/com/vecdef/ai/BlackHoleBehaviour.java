package com.vecdef.ai;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.ContactEvent;
import com.vecdef.objects.ContactEventListener;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemyFactory;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Entity;
import com.vecdef.objects.ICollidable;
import com.vecdef.objects.Masks;
import com.vecdef.objects.Particle;
import com.vecdef.objects.Player;

public class BlackHoleBehaviour extends Behavior{

	static final int BLACK_HOLE_RADIUS = 300;
	
	static final float BULLET_REPULSION = 0.0015f;
	static final float PLAYER_ATTRACT = 0.00025f;
	
	int numKills = 0;
	int maxKills = 10;
	
	ArrayList<Entity> particlesInRange;
	ArrayList<Entity> piecesInRange;
	ArrayList<Entity> bulletsInRange;
	ArrayList<Entity> enemiesInRange;
	
	ContactEventListener listener;
	
	EnemyFactory factory;
	
	EnemySpawnEffect spawnEffect;
	
	public BlackHoleBehaviour(Scene scene, EnemyFactory factory, Enemy enemy){
		super(scene, enemy);
		this.factory = factory;
		
		particlesInRange = new ArrayList<Entity>();
		piecesInRange = new ArrayList<Entity>();
		bulletsInRange = new ArrayList<Entity>();
		enemiesInRange = new ArrayList<Entity>();
		
		spawnEffect = new EnemySpawnEffect(scene, enemy, 450, 450);
		scene.add(spawnEffect);
	}
	
	@Override
	public void create(){
		listener = new ContactEventListener() {
			
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				int group = other.getGroupMask();
				
				if((group & Masks.Collision.ENEMY) == Masks.Collision.ENEMY){
					Entity enemy = (Entity)other;
					onKill(self, enemy);
				} else if((group & Masks.Collision.BULLET) == Masks.Collision.BULLET){
					if(particlesInRange.size() > 250){
						for(int i = 0; i < particlesInRange.size(); i+=2){
							Entity p = particlesInRange.get(i);
							Vector2f acceleration = p.getAcceleration();
							acceleration.x += FastMath.randomf(-4, 4);
							acceleration.y += FastMath.randomf(-4, 4);
						}
					}
				}
			}
		};
		
		self.addContactListener(listener);
	}
	
	public void update(){
		scene.getGrid().applyImplosiveForce(25.0F, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0.0F), BLACK_HOLE_RADIUS);
	    
	    particlesInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.PARTICLE, particlesInRange);
	    
	    piecesInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.MULTIPLIER, piecesInRange);
	    
	    bulletsInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.BULLET, bulletsInRange);
	    
	    enemiesInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.ENEMY, enemiesInRange);
	    
	    Player player = scene.getPlayer();
	    
	    //Player in range of black hole
	    if(isInRange(self, player)){
	    	affectPlayer(self, player);
	    }
	    
	    int n = particlesInRange.size();
	    for(int i = 0; i < n; i++)
	    	affectParticle(self, particlesInRange.get(i));
	    
	    n = piecesInRange.size();
	    for(int i = 0; i < n; i++)
	    	affectPiece(self, piecesInRange.get(i));
	    
	    n = bulletsInRange.size();
	    for(int i = 0; i < n; i++)
	    	affectBullet(self, bulletsInRange.get(i));
	    
	    n = enemiesInRange.size();
	    for(int i = 0; i < n; i++){
	    	Entity enemy = enemiesInRange.get(i);
	    	
	    	int group = enemy.getGroupMask();
	    	if((group & Masks.Collision.BLACK_HOLE) == Masks.Collision.BLACK_HOLE){
	    		continue;
	    	}
	    	
	    	if(enemy.equals(self))
	    		continue;
	    	
	    	affectEnemy(self, enemy);
	    }
	}
	
	private void affectParticle(Entity blackHole, Entity particle){
        Vector2f pVel = new Vector2f(particle.getVelocity().x, particle.getVelocity().y);

        Vector2f dPos = blackHole.getTransform().getTranslation().sub(particle.getTransform().getTranslation());
        float distance = dPos.length();
        Vector2f n = dPos.scale(1.0F / distance);
        pVel.addi(n.scale(5000.0F).scale(1.0F / (distance * distance + 5000.0F)));
        
        Particle p = (Particle) particle;
        p.setCurrentLife(0);
        
        particle.getVelocity().set(pVel);
	}
	
	void affectBullet(Entity blackHole, Entity bullet){
		Vector2f v = bullet.getVelocity().add(bullet.getTransform().getTranslation().sub(blackHole.getTransform().getTranslation()).scale(BULLET_REPULSION));
		bullet.getVelocity().set(v);
	}
	
	void affectPiece(Entity blackHole, Entity piece){
		Vector2f direction = blackHole.getTransform().getTranslation().sub(piece.getTransform().getTranslation()).normalize();
		piece.getAcceleration().addi(direction);
	}
	
	void affectEnemy(Entity blackHole, Entity enemy){
		Vector2f direction = blackHole.getTransform().getTranslation().sub(enemy.getTransform().getTranslation()).normalize();
		enemy.getAcceleration().addi(direction.scale(4));
	}
	
	private void affectPlayer(Entity blackHole, Entity player){
		Vector2f v = blackHole.getTransform().getTranslation().sub(player.getTransform().getTranslation()).scale(PLAYER_ATTRACT);
		player.getVelocity().addi(v);
	}
	
	boolean isInRange(Entity blackHole, Entity other){
		Vector2f dPos  = other.getTransform().getTranslation().sub(blackHole.getTransform().getTranslation());
		float lenSqr = dPos.lengthSquared();
		return (lenSqr <= BLACK_HOLE_RADIUS * BLACK_HOLE_RADIUS);
	}

	public void onKill(Entity self, Entity other){
		if (numKills < maxKills){
			numKills++;
		}
			
		if (numKills >= maxKills){
			self.expire();
		}
	}
	
	@Override
	public void destroy() {
		for(Entity p : particlesInRange){
			p.getAcceleration().addi(new Vector2f(FastMath.randomf(-12, 12), FastMath.randomf(-12, 12)));
		}
		
		if(numKills >= maxKills){
			for(int i = 0; i < 6; i++){
				float a = FastMath.random() * 360.0F;
		        Vector2f pos = self.getTransform().getTranslation().add(new Vector2f(FastMath.cosd(a) * self.getRadius() + self.getRadius(), FastMath.sind(a) * self.getRadius() + self.getRadius()));
		        Vector2f accel = new Vector2f(FastMath.cosd(a), FastMath.sind(a));
		        Enemy chaser = factory.createChaser(pos);
		        chaser.getAcceleration().set(accel);
			}
		}
	
		spawnEffect.destroy();
		
	}
	
}
