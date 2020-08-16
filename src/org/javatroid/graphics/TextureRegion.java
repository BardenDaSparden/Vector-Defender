package org.javatroid.graphics;

import org.javatroid.math.Vector2f;

public class TextureRegion {

	Texture texture;
	Vector2f[] texCoords = new Vector2f[4];
	
	private float width;
	private float height;
	
	public TextureRegion(Texture texture){
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		
		generateTexCoords(0, 0, texture.getWidth(), texture.getHeight());
	}
	
	public TextureRegion(Texture texture, float srcX, float srcY, float srcWidth, float srcHeight){
		this.texture = texture;
		this.width = srcWidth;
		this.height = srcHeight;
		
		//System.out.println(width + " " + height);
		
		generateTexCoords(srcX, srcY, srcWidth, srcHeight);
	}
	
	private void generateTexCoords(float srcX, float srcY, float srcWidth, float srcHeight){
		
		srcX /= texture.getWidth();
		srcY /= texture.getHeight();
		srcWidth /= texture.getWidth();
		srcHeight /= texture.getHeight();
		
		texCoords[0] = new Vector2f(srcX, srcY);
		texCoords[1] = new Vector2f(srcX, srcY + srcHeight);
		texCoords[2] = new Vector2f(srcX + srcWidth, srcY + srcHeight);
		texCoords[3] = new Vector2f(srcX + srcWidth, srcY);
	}
	
	public void flipY(){
		Vector2f t0 = texCoords[0];
		Vector2f t1 = texCoords[1];
		Vector2f t2 = texCoords[2];
		Vector2f t3 = texCoords[3];
		
		float temp = t1.y;
		t1.y = t0.y;
		t0.y = temp;
		
		temp = t3.y;
		t3.y = t2.y;
		t2.y = temp;
		
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public Vector2f[] getTexCoords(){
		return texCoords;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
}
