package com.vecdef.ai;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.objects.Bullet;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.EnemyFactory;
import com.vecdef.objects.Entity;
import com.vecdef.objects.MultiplierPiece;
import com.vecdef.objects.Particle;
import com.vecdef.objects.Player;
import com.vecdef.objects.Scene;
import com.vecdef.util.Masks;

public class BlackHoleBehaviour extends Behaviour{

	final int BLACK_HOLE_RADIUS = 250;
	final float BULLET_REPULSION = 0.0100f;
	final float PLAYER_ATTRACT = 0.00050f;
	
	int numKills = 0;
	int maxKills = 10;
	
	ArrayList<Entity> particlesInRange;
	ArrayList<Entity> particlesInContact;
	ArrayList<Entity> piecesInRange;
	ArrayList<Entity> bulletsInRange;
	ArrayList<Entity> enemiesInRange;
	
	ContactEventListener listener;
	
	EnemyFactory factory;
	
	float time = 0;
	
	public BlackHoleBehaviour(Scene scene, EnemyFactory factory, Enemy enemy){
		super(scene, enemy);
		this.factory = factory;
		particlesInRange = new ArrayList<Entity>();
		particlesInContact = new ArrayList<Entity>();
		piecesInRange = new ArrayList<Entity>();
		bulletsInRange = new ArrayList<Entity>();
		enemiesInRange = new ArrayList<Entity>();
	}
	
	@Override
	public void create(){
		listener = new ContactEventListener() {
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				int group = other.getGroupMask();
				
				if((group & Masks.Collision.ENEMY) == Masks.Collision.ENEMY)
					onContactWithEnemy((Enemy)other);
				
				if((group & Masks.Collision.BULLET) != 0)
					onContactWithBullet((Bullet)other);
				
				if((group & Masks.Collision.MULTIPLIER) == Masks.Collision.MULTIPLIER)
					onContactWithMultiplier((MultiplierPiece)other);
			}
		};
		
		self.addContactListener(listener);
	}
	
	public void update(){
	    particlesInRange.clear();
	    particlesInContact.clear();
	    piecesInRange.clear();
	    bulletsInRange.clear();
	    enemiesInRange.clear();
	    
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.PARTICLE, particlesInRange);
	    scene.getEntitiesByType(self.getTransform().getTranslation(), self.getRadius() * 2, Masks.Entities.PARTICLE, particlesInContact);
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.MULTIPLIER, piecesInRange);
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.BULLET, bulletsInRange);
	    scene.getEntitiesByType(self.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.ENEMY, enemiesInRange);
	    
	    Player player = scene.getPlayer();
	    Player player2 = scene.getPlayer2();
	    Player player3 = scene.getPlayer3();
	    Player player4 = scene.getPlayer4();
	    
	    //Affect players
	    
	    if(isInRange(self, player) && player.isAlive())
	    	affectPlayer(self, player);
	    
	    if(isInRange(self, player2) && player2.isAlive())
	    	affectPlayer(self, player2);
	    
	    if(isInRange(self, player3) && player3.isAlive())
	    	affectPlayer(self, player3);
	    
	    if(isInRange(self, player4) && player4.isAlive())
	    	affectPlayer(self, player4);
	    
	    //Attract particles
	    for(int i = 0; i < particlesInRange.size(); i++)
	    	affectParticle(self, particlesInRange.get(i));
	    
	    //Attract multiplier pieces
	    for(int i = 0; i < piecesInRange.size(); i++)
	    	affectPiece(self, piecesInRange.get(i));
	    
	    //Repulse bullets
	    for(int i = 0; i < bulletsInRange.size(); i++)
	    	affectBullet(self, bulletsInRange.get(i));
	    
	    //Attract enemies
	    for(int i = 0; i < enemiesInRange.size(); i++){
	    	Entity enemy = enemiesInRange.get(i);
	    	if((enemy.getGroupMask() & Masks.Collision.BLACK_HOLE) == Masks.Collision.BLACK_HOLE || enemy.equals(self))
	    		continue;
	    	affectEnemy(self, enemy);
	    }
	    
	    float f = ((float)numKills / (float)maxKills);
	    float wobble = 0.125f;
	    float s = 1 + (float)Math.sin(time * f) * wobble;
	    
	    time += 0.45f;
	    
	    scene.getGrid().applyImplosiveForce(30, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), BLACK_HOLE_RADIUS - 100);
	    
	    self.getTransform().getScale().set(s, s);
	    self.getAcceleration().set(0, 0);
	    self.getVelocity().set(0, 0);
	}
	
	void onContactWithBullet(Bullet bullet){
		bullet.destroy();
		float dir = FastMath.random() * 360.0f;
		Vector2f force = new Vector2f();
		dir = FastMath.random() * 360.0f;
		force.set(FastMath.cosd(dir), FastMath.sind(dir));
		force.scalei(6);
		for(int i = 0; i < particlesInContact.size(); i++){
			Entity entity = particlesInContact.get(i);
			dir = FastMath.random() * 360.0f;
			force.set(FastMath.cosd(dir), FastMath.sind(dir));
			force.scalei(9);
			entity.getAcceleration().set(force.x, force.y);
		}
	}
	
	void onContactWithEnemy(Enemy enemy){
		if(numKills < maxKills){
			numKills++;
			enemy.destroy();
		}
			
		if(numKills >= maxKills){
			spawnChasers();
			enemy.destroy();
			self.destroy();
			numKills = 0;
		}
	}
	
	void onContactWithMultiplier(MultiplierPiece multiplier){
		multiplier.destroy();
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
	
	void spawnChasers(){
		Vector2f spawnPosition = new Vector2f();
		spawnPosition.set(self.getTransform().getTranslation());
		for(int i = 0; i < 3; i++){
			Vector2f offsetPos = new Vector2f(spawnPosition);
			float a = FastMath.random() * 360.0f;
			offsetPos.x += FastMath.cosd(a) * (self.getRadius() * 2);
			offsetPos.y += FastMath.sind(a) * (self.getRadius() * 2);
			factory.createChaser(offsetPos);
		}
	}
	
	@Override
	public void destroy() {
		float dir = FastMath.random() * 360.0f;
		Vector2f force = new Vector2f();
		dir = FastMath.random() * 360.0f;
		force.set(FastMath.cosd(dir), FastMath.sind(dir));
		force.scalei(6);
		for(int i = 0; i < particlesInContact.size(); i++){
			Entity entity = particlesInContact.get(i);
			dir = FastMath.random() * 360.0f;
			force.set(FastMath.cosd(dir), FastMath.sind(dir));
			entity.getAcceleration().set(force.x * 3, force.y * 3);
		}
		self.removeContactListener(listener);
	}
	
}
