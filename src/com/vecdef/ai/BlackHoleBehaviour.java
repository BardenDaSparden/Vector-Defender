package com.vecdef.ai;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.objects.Bullet;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Masks;
import com.vecdef.objects.MultiplierPiece;
import com.vecdef.objects.Particle;
import com.vecdef.objects.Player;

public class BlackHoleBehaviour extends Behavior{

	static final int BLACK_HOLE_RADIUS = 300;
	
	static final float BULLET_REPULSION = 0.0015f;
	static final float MULTIPLIER_ATTRACT = 0.003f;
	static final float ENEMY_ATTRACT = 0.0005f;
	static final float PLAYER_ATTRACT = 0.00025f;
	
	int time = 0;
	Vector2f toScale = new Vector2f(0, 0);
	int wobbleSpeed = 3;
	
	int numKills = 0;
	int maxKills = 10;
	
	ArrayList<Entity> particlesInRange;
	ArrayList<Entity> piecesInRange;
	ArrayList<Entity> bulletsInRange;
	ArrayList<Entity> enemiesInRange;
	
	public BlackHoleBehaviour(Scene scene){
		super(scene);
		
		particlesInRange = new ArrayList<Entity>();
		piecesInRange = new ArrayList<Entity>();
		bulletsInRange = new ArrayList<Entity>();
		enemiesInRange = new ArrayList<Entity>();
		
	}
	
	@Override
	public void create(Entity self){
		
	}
	
	public void update(Entity object){
		scene.getGrid().applyImplosiveForce(25.0F, new Vector3f(object.getTransform().getTranslation().x, object.getTransform().getTranslation().y, 0.0F), BLACK_HOLE_RADIUS);
	    
	    particlesInRange.clear();
	    scene.getEntitiesByType(object.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.PARTICLE, particlesInRange);
	    
	    piecesInRange.clear();
	    scene.getEntitiesByType(object.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.MULTIPLIER, piecesInRange);
	    
	    bulletsInRange.clear();
	    scene.getEntitiesByType(object.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.BULLET, bulletsInRange);
	    
	    enemiesInRange.clear();
	    scene.getEntitiesByType(object.getTransform().getTranslation(), BLACK_HOLE_RADIUS, Masks.Entities.ENEMY, enemiesInRange);
	    
	    Player player = scene.getPlayer();
	    
	    //Player in range of black hole
	    if(isInRange(object, player)){
	    	affectPlayer(object, player);
	    }
	    
	    int n = particlesInRange.size();
	    for(int i = 0; i < n; i++)
	    	affectParticle(object, particlesInRange.get(i));
	    
	    n = piecesInRange.size();
	    for(int i = 0; i < n; i++)
	    	affectPiece(object, piecesInRange.get(i));
	    
	    n = bulletsInRange.size();
	    for(int i = 0; i < n; i++)
	    	affectBullet(object, bulletsInRange.get(i));
	    
	    n = enemiesInRange.size();
	    for(int i = 0; i < n; i++){
	    	Entity enemy = enemiesInRange.get(i);
	    	
	    	int group = enemy.getGroupMask();
	    	if((group & Masks.Collision.BLACK_HOLE) == Masks.Collision.BLACK_HOLE){
	    		continue;
	    	}
	    	
	    	if(enemy.equals(object))
	    		continue;
	    	
	    	affectEnemy(object, enemy);
	    }
	    
	    Vector2f scale = object.getTransform().getScale();
	    toScale.x = scale.x + (float)Math.signum(FastMath.sind(time * wobbleSpeed)) * 0.001f;
	    toScale.y = toScale.x;
	    
	    scale.set(approach(scale.x, toScale.x, 0.95f), approach(scale.y, toScale.y, 0.95f));
	    
	    time++;
	}
	
	private void affectParticle(Entity blackHole, Entity particle){
        Vector2f pVel = new Vector2f(particle.getVelocity().x, particle.getVelocity().y);

        Vector2f dPos = blackHole.getTransform().getTranslation().sub(particle.getTransform().getTranslation());
        float distance = dPos.length();
        Vector2f n = dPos.scale(1.0F / distance);
        pVel.addi(n.scale(10000.0F).scale(1.0F / (distance * distance + 10000.0F)));
        
        Particle p = (Particle) particle;
        p.setCurrentLife(0);
        
        particle.getVelocity().set(pVel);
	}
	
	void affectBullet(Entity blackHole, Entity bullet){
		Vector2f v = bullet.getVelocity().add(bullet.getTransform().getTranslation().sub(blackHole.getTransform().getTranslation()).scale(BULLET_REPULSION));
		bullet.getVelocity().set(v);
	}
	
	void affectPiece(Entity blackHole, Entity piece){
		Vector2f v = piece.getVelocity().add(blackHole.getTransform().getTranslation().sub(piece.getTransform().getTranslation()).scale(MULTIPLIER_ATTRACT));
		piece.getVelocity().set(v);
	}
	
	void affectEnemy(Entity enemy, Entity other){
		Vector2f v = enemy.getTransform().getTranslation().sub(other.getTransform().getTranslation()).scale(ENEMY_ATTRACT);
		other.getVelocity().addi(v);
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

	private float approach(float start, float end, float w){
		return start + (end - start) * w;
	}
	
	public void onCollision(Entity object, Entity other) {
		if(other instanceof Enemy){
			other.destroy();
			
			Vector2f scale = object.getTransform().getScale();
			
			toScale.x = scale.x + 0.05f;
			toScale.y = scale.y + 0.05f;
			
			onKill(object);
		} else if(other instanceof MultiplierPiece) {
			other.destroy();
		} else if(other instanceof Bullet){
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

	public void onKill(Entity object){
		if (numKills < maxKills){
			numKills++;
			wobbleSpeed++;
		}
			
		if (numKills == maxKills){
			destroy(object);
			for (int i = 0; i < 8; i++){
				float a = FastMath.random() * 360.0F;
		        Vector2f pos = object.getTransform().getTranslation().add(new Vector2f(FastMath.cosd(a) * object.getRadius(), FastMath.sind(a) * object.getRadius()));
		        Enemy e = Enemy.createChaser(pos, scene);
		        scene.add(e);
			}
		}
	}
	
	@Override
	public void destroy(Entity object) {
		for(Entity p : particlesInRange){
			p.getAcceleration().addi(new Vector2f(FastMath.randomf(-12, 12), FastMath.randomf(-12, 12)));
		}
	}
	
}
