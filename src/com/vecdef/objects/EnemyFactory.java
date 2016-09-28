package com.vecdef.objects;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

import com.vecdef.ai.BlackHoleBehaviour;
import com.vecdef.ai.ChaserBehaviour;
import com.vecdef.ai.FollowerBehavior;
import com.vecdef.ai.PrototypeBehaviour;
import com.vecdef.ai.StalkerBehaviour;
import com.vecdef.ai.WandererBehavior;
import com.vecdef.collision.ContactEvent;
import com.vecdef.collision.ContactEventListener;
import com.vecdef.collision.ICollidable;
import com.vecdef.model.BlackHoleModel;
import com.vecdef.model.ChaserModel;
import com.vecdef.model.FollowerModel;
import com.vecdef.model.PrototypeModel;
import com.vecdef.model.SeekerModel;
import com.vecdef.model.WandererModel;
import com.vecdef.util.Masks;

public class EnemyFactory {

	Scene scene;
	
	public EnemyFactory(Scene scene){
		this.scene = scene;
	}
	
	public Enemy createWanderer(Vector2f position){
		Enemy enemy = new Enemy(scene);
		
		enemy.transform.setTranslation(position);
		enemy.baseColor.set(0.35f, 0.25f, 1, 1);
	    enemy.killValue = 5;
	    enemy.energyValue = 500;
	    enemy.radius = 13;
	    enemy.model = WandererModel.get();
		enemy.addBehavior(new WandererBehavior(scene, enemy));
		
		scene.add(enemy);
		return enemy;
	}
	
	public Enemy createSeeker(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor.set(0, 1, 1, 1);
	    enemy.killValue = 10;
	    enemy.energyValue = 5;
	    enemy.radius = 12;
	    enemy.health = 1;
		enemy.model = SeekerModel.get();
		enemy.addBehavior(new FollowerBehavior(scene, enemy));
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createFollower(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor.set(1, 0, 1, 1);
	    enemy.killValue = 25;
	    enemy.energyValue = 5;
	    enemy.health = 1;
	    enemy.radius = 12;
		enemy.model = FollowerModel.get();
		enemy.addBehavior(new StalkerBehaviour(scene, enemy));
		
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createChaser(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor.set(0.5f, 1, 1, 1);
	    enemy.killValue = 10;
	    enemy.energyValue = 5;
	    enemy.radius = 7;
		enemy.angularVelocity = FastMath.randomi(-2, 2);
		enemy.model = ChaserModel.get();
		
		enemy.addBehavior(new ChaserBehaviour(scene, enemy));
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createPrototype(Vector2f position){
		Player player = scene.getPlayer();
		
		Enemy enemy = new Enemy(scene);
		enemy.baseColor.set(1, 0.75f, 0, 1);
		enemy.transform.setTranslation(position);
		enemy.transform.setOrientation(player.getTransform().getTranslation().sub(position).direction());
	    enemy.killValue = 25;
	    enemy.energyValue = 10;
	    enemy.radius = 12;
		enemy.model = PrototypeModel.get();
		enemy.addBehavior(new PrototypeBehaviour(scene, enemy));
		
	    scene.add(enemy);
	    return enemy;
	}
	
	public Enemy createBlackHole(Vector2f position){
		Enemy enemy = new Enemy(scene);
		enemy.transform.setTranslation(position);
		enemy.baseColor.set(1, 1, 1, 1);
		enemy.killValue = 100;
		enemy.energyValue = 25;
	    enemy.health = 5;
	    enemy.radius = 26;
	    enemy.groupMask = Masks.Collision.ENEMY | Masks.Collision.BLACK_HOLE;
	    enemy.collisionMask |= Masks.Collision.MULTIPLIER | Masks.Collision.ENEMY;
	    enemy.model = BlackHoleModel.get();
	    enemy.addBehavior(new BlackHoleBehaviour(scene, this, enemy));
	    enemy.addContactListener(new ContactEventListener() {
			
			@Override
			public void process(ContactEvent event) {
				ICollidable other = event.other;
				int group = other.getGroupMask();
				if((group & Masks.Collision.ENEMY) == Masks.Collision.ENEMY){
					Entity eOther = (Entity)other;
					eOther.expire();
				}
			}
		});
	    
	    scene.add(enemy);
	    return enemy;
	}
	
}
