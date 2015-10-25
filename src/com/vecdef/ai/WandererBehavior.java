package com.vecdef.ai;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;

import com.vecdef.gamestate.Scene;
import com.vecdef.model.Mesh;
import com.vecdef.model.Transform2D;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.IRenderable;

public class WandererBehavior extends Behavior{
	
	float speed = 2f;	
	int gridWidth;
	int gridHeight;
	
	//time in frames
	final int ACTIVATION_TIME = 20;
	Timer activationTimer;
	boolean active;
	
	IRenderable renderHull;
	
	public WandererBehavior(Scene scene){
		super(scene);
		Grid grid = scene.getGrid();
		gridWidth = grid.getWidth();
		gridHeight = grid.getHeight();
		active = false;
	}
	
	@Override
	public void create(Entity self){
		activationTimer = new Timer(ACTIVATION_TIME);
		activationTimer.setCallback(new TimerCallback() {
			
			@Override
			public void execute(Timer timer) {
				scene.removeRenderable(renderHull);
				self.setAngularVelocity(-3);
				float a = FastMath.random() * 360;
				self.getVelocity().set(new Vector2f(FastMath.cosd(a) * speed, FastMath.sind(a) * speed));
				
				Vector2f position = self.getTransform().getTranslation();
				scene.getGrid().applyExplosiveForce(250, new Vector3f(position.x, position.y, 0), 100);
			}
		});
		
		IRenderable renderComp = (IRenderable) self;
		
		renderHull = new IRenderable() {
			
			float opacity = renderComp.getOpacity();
			boolean bDrawn = renderComp.isDrawn();
			Transform2D transform = new Transform2D(renderComp.getTransform().getTranslation(), renderComp.getTransform().getOrientation());
			Mesh mesh = renderComp.getMesh();
			
			@Override
			public void setOpacity(float opacity) {
				this.opacity = opacity;
			}
			
			@Override
			public void setDraw(boolean bDraw) {
				bDrawn = bDraw;
			}
			
			@Override
			public boolean isDrawn() {
				return bDrawn;
			}
			
			@Override
			public Transform2D getTransform() {
				return transform;
			}
			
			@Override
			public float getOpacity() {
				return opacity;
			}
			
			@Override
			public Mesh getMesh() {
				return mesh;
			}
		};
		scene.addRenderable(renderHull);
		activationTimer.start();
	}
	
	public void update(Entity self){
		if(!activationTimer.isComplete()){
			activationTimer.tick();
			float opacity = (float) Math.sin( (1 - activationTimer.percentComplete()) * Math.PI / 2f );
			renderHull.setOpacity(opacity);
			Transform2D transform = renderHull.getTransform();
			transform.getScale().addi(new Vector2f(0.15f, 0.15f));
			return;
		}
		
		scene.getGrid().applyExplosiveForce(10, new Vector3f(self.getTransform().getTranslation().x, self.getTransform().getTranslation().y, 0), 100);
	    
	    if ((self.getTransform().getTranslation().x < -gridWidth / 2) || (self.getTransform().getTranslation().x > gridWidth / 2)) {
	    	self.getTransform().getTranslation().x = FastMath.clamp(-gridWidth / 2 + 1, gridWidth / 2 - 1, self.getTransform().getTranslation().x);
	    	self.getVelocity().x *= -1.0F;
	    }

	    if ((self.getTransform().getTranslation().y < -gridHeight / 2) || (self.getTransform().getTranslation().y > gridHeight / 2)) {
	    	self.getTransform().getTranslation().y = FastMath.clamp(-gridHeight / 2 + 1, gridHeight / 2 - 1, self.getTransform().getTranslation().y);
	    	self.getVelocity().y *= -1.0F;
	    }
	    
	}

	@Override
	public void destroy(Entity self) {
		scene.removeRenderable(renderHull);
	}
}