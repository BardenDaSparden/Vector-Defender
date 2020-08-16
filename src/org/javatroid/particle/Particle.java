package org.javatroid.particle;

import org.javatroid.core.Resources;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.graphics.TextureRegion;

public class Particle {
	
	private static Texture texture = null;
	
	public float x, y;
	public float vx = 0, vy = 0;
	
	public float orientation = 0;
	public float av = 0;
	
	public float scaleX = 1, scaleY = 1;
	
	public float r = 1, g = 1, b = 1, a = 1;
	
	public int maxLife = 60;
	public int currentLife = 0;
	
	public TextureRegion region;
	
	public Particle(){
		this(0, 0);
	}
	
	public Particle(float x, float y){
		if(texture == null){
			Resources.loadTexture("textures/white.png", "white");
			texture = Resources.getTexture("white");
		}
		
		this.x = x;
		this.y = y;
		this.region = new TextureRegion(texture);
	}
	
	public void update(){
		x += vx;
		y += vy;
		
		orientation += av;
		
		currentLife++;
		if(currentLife < 0)
			currentLife = 0;
	}
	
	public void draw(SpriteBatch renderer){
		renderer.setColor(r, g, b, a);
		renderer.draw(x, y, region.getWidth() * scaleX, region.getHeight() * scaleY, orientation, region);
	}
	
	public Particle clone(){
		Particle p = new Particle(this.x, this.y);
		
		p.vx = this.vx;
		p.vy = this.vy;
		
		p.orientation = this.orientation;
		p.av = this.av;
		
		p.scaleX = this.scaleX;
		p.scaleY = this.scaleY;
		
		p.r = this.r;
		p.g = this.g;
		p.b = this.b;
		p.a = this.a;
		
		p.maxLife = this.maxLife;
		
		p.region = this.region;
		
		return p;
	}
	
	
	
	public boolean isDead(){
		return (currentLife >= maxLife);
	}
}
