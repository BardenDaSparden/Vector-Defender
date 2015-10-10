package com.shapedefender.behaviours;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.shapedefender.objects.Bullet;
import com.shapedefender.objects.Enemy;
import com.shapedefender.objects.Entity;
import com.shapedefender.objects.EntityManager;
import com.shapedefender.objects.Grid;
import com.shapedefender.objects.MultiplierPiece;
import com.shapedefender.objects.Particle;
import com.shapedefender.objects.Player;

public class BlackHoleBehaviour implements Behavior{

	private static final int BLACK_HOLE_RADIUS = 200;
	private static final int FORCE_RADIUS = 500;
	private static final int MAX_AFFECTED_PARTICLES = 750;
	
	private ArrayList<Particle> particlesInRange = new ArrayList<Particle>();
	
	int time = 0;
	Vector2f toScale = new Vector2f(0, 0);
	int wobbleSpeed = 3;
	
	int numKills = 0;
	int maxKills = 10;
	
	@SuppressWarnings("unchecked")
	public void onUpdate(Entity object, Grid grid, float dt){
		grid.applyImplosiveForce(25.0F, new Vector3f(object.transform.getTranslation().x, object.transform.getTranslation().y, 0.0F), FORCE_RADIUS);

	   // ArrayList<Entity> nearbyObjects = EntityManager.getNearbyEntities(object.transform.getTranslation(), 300.0F);
	    
	    ArrayList<Particle> 		nearbyParticles 		= (ArrayList<Particle>) EntityManager.getEntities(Particle.class);
	    ArrayList<MultiplierPiece> 	nearbyPieces 			= (ArrayList<MultiplierPiece>) EntityManager.getEntities(MultiplierPiece.class);
	    ArrayList<Bullet>			nearbyBullets   		= (ArrayList<Bullet>) EntityManager.getEntities(Bullet.class);
	    ArrayList<Enemy>			nearbyEnemies			= (ArrayList<Enemy>)  EntityManager.getEntities(Enemy.class);
	    
	    Player player = Entity.getScene().getPlayer();
	    
	    //Player in range of black hole
	    if(isInRange(object, player)){
	    	affectPlayer(object, player);
	    }
	    
	    
	    if(particlesInRange.size() < MAX_AFFECTED_PARTICLES + 1){
	    	for(Particle p : nearbyParticles){
		    	if(isInRange(object, p)){
		    		if(!particlesInRange.contains(p)){
		    			particlesInRange.add(p);
		    		}
		    	}
		    }
		}
	    
	    for(Particle p : particlesInRange)
	    	affectParticle(object, p);
	    
	    for(MultiplierPiece piece : nearbyPieces){
	    	if(isInRange(object, piece))
	    		affectPiece(object, piece);
	    }
	    
	    for(Bullet bullet : nearbyBullets){
	    	if(isInRange(object, bullet))
	    		affectBullet(object, bullet);
	    }
	    
	    for(Enemy enemy : nearbyEnemies){
	    	if(enemy == object)
	    		continue;
	    	if(isInRange(object, enemy))
	    		affectEnemy(object, enemy);
	    }
	    
	    Vector2f scale = object.getTransform().getScale();
	    toScale.x = scale.x + (float)Math.signum(FastMath.sind(time * wobbleSpeed)) * 0.001f;
	    toScale.y = toScale.x;
	    
	    scale.set(approach(scale.x, toScale.x, 0.95f), approach(scale.y, toScale.y, 0.95f));
	    
	    time++;
	}
	
	private void affectParticle(Entity blackHole, Particle particle){
        Vector2f pVel = new Vector2f(particle.getVelocity().x, particle.getVelocity().y);

        Vector2f dPos = blackHole.transform.getTranslation().sub(particle.getTransform().getTranslation());
        float distance = dPos.length();
        Vector2f n = dPos.scale(1.0F / distance);
        pVel = pVel.add(n.scale(10000.0F).scale(1.0F / (distance * distance + 10000.0F)));

        if (distance < BLACK_HOLE_RADIUS){
        	particle.setCurrentLife(particle.getCurrentLife() - 1);
        	pVel = pVel.add(new Vector2f(n.y, -n.x).scale(10 + FastMath.random() * 40).scale(1.0F / (distance)));
        }
        
        particle.setVelocity(pVel.clone());
	}
	
	private void affectBullet(Entity blackHole, Bullet bullet){
		Vector2f v = bullet.getVelocity().add(bullet.transform.getTranslation().sub(blackHole.transform.getTranslation()).scale(0.005f));
		bullet.setVelocity(v);
		//e.setVelocity(e.getVelocity().add(e.transform.getTranslation().sub(object.transform.getTranslation()).scale(0.005F)));
	}
	
	private void affectPiece(Entity blackHole, MultiplierPiece piece){
		Vector2f v = piece.getVelocity().add(blackHole.transform.getTranslation().sub(piece.transform.getTranslation()).scale(0.002f));
		piece.setVelocity(v);
		//e.setVelocity(e.getVelocity().add(object.transform.getTranslation().sub(e.transform.getTranslation()).scale(0.002F)));
	}
	
	private void affectEnemy(Entity blackHole, Entity other){
		Vector2f v = blackHole.transform.getTranslation().sub(other.transform.getTranslation()).scale(0.001f);
		other.getVelocity().addi(v);
		//other.setVelocity(e.getVelocity().add(object.transform.getTranslation().sub(e.transform.getTranslation()).scale(0.00055F)));
	}
	
	private void affectPlayer(Entity blackHole, Player player){
		Vector2f v = blackHole.transform.getTranslation().sub(player.transform.getTranslation()).scale(0.0005F);
		player.getVelocity().addi(v);
	}
	
	private boolean isInRange(Entity blackHole, Entity other){
		Vector2f dPos  = other.getTransform().getTranslation().sub(blackHole.getTransform().getTranslation());
		float lenSqr = dPos.lengthSquared();
		return (lenSqr <= BLACK_HOLE_RADIUS * BLACK_HOLE_RADIUS);
	}

	private float approach(float start, float end, float w){
		return start + (end - start) * w;
	}
	
	public void onCollision(Entity object, Entity other) {
		if(other instanceof Enemy){
			other.onDestroy();
			
			Vector2f scale = object.getTransform().getScale();
			
			toScale.x = scale.x + 0.05f;
			toScale.y = scale.y + 0.05f;
			
			onKill(object);
		} else if(other instanceof MultiplierPiece) {
			other.onDestroy();
		} else if(other instanceof Bullet){
			if(particlesInRange.size() > 250){
				for(int i = 0; i < particlesInRange.size(); i+=2){
					Particle p = particlesInRange.get(i);
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
			onDestroy(object);
			for (int i = 0; i < 8; i++){
				float a = FastMath.random() * 360.0F;
		        Vector2f pos = object.transform.getTranslation().add(new Vector2f(FastMath.cosd(a) * object.radius, FastMath.sind(a) * object.radius));
		        Enemy e = Enemy.createChaser(pos);
		        EntityManager.add(e);
			}
		}
	}
	
	@Override
	public void onDestroy(Entity object) {
		for(Particle p : particlesInRange){
			p.getAcceleration().addi(new Vector2f(FastMath.randomf(-15, 15), FastMath.randomf(-15, 15)));
		}
	}
	
}
