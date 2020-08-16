package com.vecdef.objects;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.ai.BlackHoleBehaviour;
import com.vecdef.ai.ChaserBehaviour;
import com.vecdef.ai.FollowerBehavior;
import com.vecdef.ai.PrototypeBehaviour;
import com.vecdef.ai.SplitterBehaviour;
import com.vecdef.ai.StalkerBehaviour;
import com.vecdef.ai.WandererBehavior;
import com.vecdef.ai.ZoomerBehavior;
import com.vecdef.model.BlackHoleModel;
import com.vecdef.model.ChaserModel;
import com.vecdef.model.FollowerModel;
import com.vecdef.model.PrototypeModel;
import com.vecdef.model.SeekerModel;
import com.vecdef.model.SplitterModel;
import com.vecdef.model.WandererModel;
import com.vecdef.model.ZoomerModel;
import com.vecdef.util.Masks;

public class EnemyFactory {

	public static final int WANDERER = 1;
	public static final int SEEKER = 2;
	public static final int FOLLOWER = 3;
	public static final int CHASER = 4;
	public static final int PROTOTYPE = 5;
	public static final int SPLITTER = 6;
	public static final int ZOOMER = 7;
	public static final int BLACK_HOLE = 8;
	
	Scene scene;
	EnemyPool pool;
	SpawnEffectPool ePool;
	
	public EnemyFactory(Scene scene, EnemyPool pool){
		this.scene = scene;
		this.pool = pool;
		this.ePool = scene.getSpawnEffectPool();
	}
	
	public Enemy create(int type, Vector2f position){
		Enemy enemy = null;
		if(type == WANDERER)
			enemy = createWanderer(position);
		else if(type == SEEKER)
			enemy = createSeeker(position);
		else if(type == FOLLOWER)
			enemy = createFollower(position);
		else if(type == CHASER)
			enemy = createChaser(position);
		else if(type == PROTOTYPE)
			enemy = createPrototype(position);
		else if(type == SPLITTER)
			enemy = createSplitter(position);
		else if(type == ZOOMER)
			enemy = createZoomer(position);
		else if(type == BLACK_HOLE)
			enemy = createBlackHole(position);
		
		return enemy;
	}
	
	public Enemy createWanderer(Vector2f position){
		Enemy enemy = (Enemy)pool.getNext();
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.baseColor.set(0.35f, 0.25f, 1, 1);
	    enemy.killValue = 5;
	    enemy.energyValue = 10;
	    enemy.radius = 13;
	    enemy.model = WandererModel.get();
	    enemy.groupMask = Masks.Collision.ENEMY;
	    enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
		enemy.addBehavior(new WandererBehavior(scene, enemy));
		
		SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
		
		return enemy;
	}
	
	public Enemy createSeeker(Vector2f position){
		Enemy enemy = (Enemy)pool.getNext();
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.baseColor.set(0, 1, 1, 1);
	    enemy.killValue = 10;
	    enemy.energyValue = 15;
	    enemy.radius = 12;
	    enemy.health = 1;
		enemy.model = SeekerModel.get();
		enemy.groupMask = Masks.Collision.ENEMY;
	    enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
		enemy.addBehavior(new FollowerBehavior(scene, enemy));
		
		SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
		
	    return enemy;
	}
	
	public Enemy createFollower(Vector2f position){
		Enemy enemy = (Enemy)pool.getNext();
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.baseColor.set(1, 0, 1, 1);
	    enemy.killValue = 25;
	    enemy.energyValue = 25;
	    enemy.health = 1;
	    enemy.radius = 12;
		enemy.model = FollowerModel.get();
		enemy.groupMask = Masks.Collision.ENEMY;
	    enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
		enemy.addBehavior(new StalkerBehaviour(scene, enemy));
		
		SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
		
	    return enemy;
	}
	
	public Enemy createChaser(Vector2f position){
		Enemy enemy = (Enemy)pool.getNext();
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.baseColor.set(0.5f, 1, 1, 1);
	    enemy.killValue = 10;
	    enemy.energyValue = 50;
	    enemy.radius = 23;
		enemy.angularVelocity = FastMath.randomi(-2, 2);
		enemy.model = ChaserModel.get();
		enemy.groupMask = Masks.Collision.ENEMY;
	    enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
		enemy.addBehavior(new ChaserBehaviour(scene, enemy));
		
		SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
		
	    return enemy;
	}
	
	public Enemy createPrototype(Vector2f position){
		Player player = scene.getPlayer();
		
		Enemy enemy = (Enemy)pool.getNext();
		enemy.baseColor.set(1, 0.75f, 0, 1);
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.transform.setOrientation(player.getTransform().getTranslation().sub(position).direction());
	    enemy.killValue = 25;
	    enemy.energyValue = 25;
	    enemy.radius = 15;
		enemy.model = PrototypeModel.get();
		enemy.groupMask = Masks.Collision.ENEMY;
	    enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
		enemy.addBehavior(new PrototypeBehaviour(scene, enemy));
		
		SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
		
	    return enemy;
	}
	
	public Enemy createSplitter(Vector2f position){
		Enemy enemy = (Enemy) pool.getNext();
		enemy.baseColor.set(1.0f, 0.15f, 0, 1.0f);
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.killValue = 25;
		enemy.radius = 14;
		enemy.model = SplitterModel.get();
		enemy.groupMask = Masks.Collision.ENEMY;
		enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
		enemy.addBehavior(new SplitterBehaviour(scene, this, enemy));
		
		SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
		
		return enemy;
	}
	
	public Enemy createZoomer(Vector2f position){
		Enemy enemy = (Enemy) pool.getNext();
		enemy.baseColor.set(0, 1.0f, 0, 1.0f);
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.killValue = 25;
		enemy.radius = 14;
		enemy.model = ZoomerModel.get();
		enemy.groupMask = Masks.Collision.ENEMY;
		enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET;
		enemy.addBehavior(new ZoomerBehavior(scene, enemy));
		
		SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
		
		return enemy;
	}
	
	public Enemy createBlackHole(Vector2f position){
		Enemy enemy = (Enemy)pool.getNext();
		enemy.transform.getTranslation().set(position.x, position.y);
		enemy.baseColor.set(1, 1, 1, 1);
		enemy.killValue = 100;
		enemy.energyValue = 150;
	    enemy.health = 7;
	    enemy.radius = 26;
	    enemy.groupMask = Masks.Collision.ENEMY | Masks.Collision.BLACK_HOLE;
	    enemy.collisionMask = Masks.Collision.PLAYER | Masks.Collision.BULLET | Masks.Collision.ENEMY;
	    enemy.model = BlackHoleModel.get();
	    enemy.addBehavior(new BlackHoleBehaviour(scene, this, enemy));
	    
	    SpawnEffect effect = (SpawnEffect) ePool.getNext();
		effect.setBase(enemy);
	    
	    return enemy;
	}
	
}
