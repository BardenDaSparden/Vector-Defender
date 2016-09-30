package com.vecdef.ai;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemyFactory;
import com.vecdef.objects.EnemySpawnEffect;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Particle;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;
import com.vecdef.util.Masks;

public class BlackHoleBehaviour extends Behaviour{

	static final int BLACK_HOLE_RADIUS = 280;
	
	static final float BULLET_REPULSION = 0.0100f;
	static final float PLAYER_ATTRACT = 0.00050f;
	
	int numKills = 0;
	int maxKills = 16;
	boolean wasHit = false;
	
	ArrayList<Entity> particlesInRange;
	ArrayList<Entity> particlesInContact;
	ArrayList<Entity> piecesInRange;
	ArrayList<Entity> bulletsInRange;
	ArrayList<Entity> enemiesInRange;
	
	ContactEventListener listener;
	
	EnemyFactory factory;
	
	EnemySpawnEffect spawnEffect;
	
	float time = 0;
	
	public BlackHoleBehaviour(Scene scene, EnemyFactory factory, Enemy enemy){
		super(scene, enemy);
		this.factory = factory;
		
		particlesInRange = new ArrayList<Entity>();
		particlesInContact = new ArrayList<Entity>();
		piecesInRange = new ArrayList<Entity>();
		bulletsInRange = new ArrayList<Entity>();
		enemiesInRange = new ArrayList<Entity>();
		
		spawnEffect = new EnemySpawnEffect(scene, enemy);
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
					wasHit = true;
				}
			}
		};
		
		self.addContactListener(listener);
	}
	
	public void update(){
		scene.getGrid().applyExplosiveForce(150, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), BLACK_HOLE_RADIUS - 100);
	    
	    particlesInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.PARTICLE, particlesInRange);
	    
	    particlesInContact.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), self.getRadius() * 2, Masks.Entities.PARTICLE, particlesInContact);
	    
	    piecesInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.MULTIPLIER, piecesInRange);
	    
	    bulletsInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.BULLET, bulletsInRange);
	    
	    enemiesInRange.clear();
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.ENEMY, enemiesInRange);
	    
	    Player player = scene.getPlayer();
	    Player player2 = scene.getPlayer2();
	    Player player3 = scene.getPlayer3();
	    Player player4 = scene.getPlayer4();
	    
	    //Player in range of black hole
	    if(isInRange(self, player)){
	    	affectPlayer(self, player);
	    }
	    
	  //Player in range of black hole
	    if(isInRange(self, player2)){
	    	affectPlayer(self, player2);
	    }
	    
	  //Player in range of black hole
	    if(isInRange(self, player3)){
	    	affectPlayer(self, player3);
	    }
	    
	  //Player in range of black hole
	    if(isInRange(self, player4)){
	    	affectPlayer(self, player4);
	    }
	    
	    int n = Math.min(particlesInRange.size(), 1000);
	    for(int i = 0; i < n; i++){
	    	affectParticle(self, particlesInRange.get(i));
	    }
	    
	    if(wasHit){
	    	scene.getGrid().applyExplosiveForce(100, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), BLACK_HOLE_RADIUS - 50);
	    	if(particlesInContact.size() > 0){
	    		Vector2f dir = new Vector2f();
	    		n = particlesInContact.size();
				for(int i = 0; i < n; i++){
					Entity p = particlesInRange.get(i);
					dir.x = p.getTransform().getTranslation().x - self.getTransform().getTranslation().x;
					dir.y = p.getTransform().getTranslation().y - self.getTransform().getTranslation().y;
					dir.normalizei();
					float s = 12.5f;
					p.getAcceleration().set(new Vector2f(dir.x * s, dir.y * s));
				}
			}
	    	wasHit = false;
	    }
	    
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
	    
	    float f = ((float)numKills / (float)maxKills);
	    float wobble = 0.1f;
	    float s = 1 + (float)Math.sin(time * (f * 2.0f)) * wobble;
	    
	    self.getTransform().getScale().set(s, s);
	    
	    time += 0.15f;
	}
	
	private void affectParticle(Entity blackHole, Entity particle){
        Vector2f pVel = new Vector2f(particle.getVelocity().x, particle.getVelocity().y);

        Vector2f dPos = blackHole.getTransform().getTranslation().sub(particle.getTransform().getTranslation());
        float distance = dPos.length();
        Vector2f n = dPos.scale(1.0F / distance);
        pVel.addi(n.scale(4000.0F).scale(1.0F / (distance * distance + 5000.0F)));
        
        Particle p = (Particle) particle;
        p.setCurrentLife(0);
        
        particle.getVelocity().set(pVel);
	}
	
	void affectBullet(Entity blackHole, Entity bullet){
		Vector2f v = bullet.getVelocity().add(bullet.getTransform().getTranslation().sub(blackHole.getTransform().getTranslation()).scale(BULLET_REPULSION));
		bullet.getVelocity().set(v);
	}
	
	void affectPiece(Entity blackHole, Entity piece){
		Vector2f dPos = blackHole.getTransform().getTranslation().sub(piece.getTransform().getTranslation());
        float distance = dPos.length();
        Vector2f n = dPos.scale(1.0F / distance);
		piece.getVelocity().addi(n.scale(4000.0F).scale(1.0F / (distance * distance + 5000.0F)));
	}
	
	void affectEnemy(Entity blackHole, Entity enemy){
		Vector2f dPos = blackHole.getTransform().getTranslation().sub(enemy.getTransform().getTranslation());
        float distance = dPos.length();
        Vector2f n = dPos.scale(1.0F / distance);
		enemy.getVelocity().addi(n.scale(4000.0F).scale(1.0F / (distance * distance + 5000.0F)));
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
		
		float force = 7 + (float)Math.random() * 4;
		if(numKills >= maxKills){
			for(int i = 0; i < 4; i++){
				float a = FastMath.random() * 360.0F;
		        Vector2f pos = self.getTransform().getTranslation().add(new Vector2f(FastMath.cosd(a) * self.getRadius() + self.getRadius(), FastMath.sind(a) * self.getRadius() + self.getRadius()));
		        Vector2f accel = new Vector2f(FastMath.cosd(a) * force, FastMath.sind(a) * force);
		        Enemy chaser = factory.createChaser(pos);
		        chaser.getAcceleration().set(accel);
			}
		}
	
		spawnEffect.destroy();
		
	}
	
}
