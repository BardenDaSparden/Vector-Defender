package org.javatroid.text;

import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.graphics.TextureRegion;

public class BitmapFont {

	private float base;
	private float lineHeight;
	private CharDescriptor[] chars;
	private Texture baseImage = null;
	
	public BitmapFont(){
		this.base = 0;
		this.lineHeight = 0;
		this.chars = new CharDescriptor[256];
		
		for(int i = 0; i < chars.length; i++){
			chars[i] = new CharDescriptor();
		}
		
	}
	
	public void drawString(float x, float y, String s, SpriteBatch renderer){
		float startX = x;
		
		float currentX = x;
		float currentY = y;
		
		for(char c : s.toCharArray()){
			
			float posX = chars[c].getX();
			float posY = chars[c].getY();
			float width = chars[c].getWidth();
			float height = chars[c].getHeight();
			float xOffset = chars[c].getOffsetX();
			float yOffset = chars[c].getOffsetY();
			
			TextureRegion region = null;
			
			if(c == '\n'){
				currentX = startX;
				currentY -= lineHeight;
			} else if(c == '\t'){
				currentX += chars[0x20].getWidth() * 8;
			}else {
				region = new TextureRegion(baseImage, posX, baseImage.getHeight() - (posY + height), width, height);
				renderer.draw(currentX + (xOffset + width / 2), currentY - (height / 2 + yOffset), width, height, 0, region);
			}
			
			
			currentX += chars[c].getAdvancedX();
			
		}
	}
	
	public void drawStringCentered(float x, float y, String s, SpriteBatch renderer){
		drawString(x - getWidth(s) / 2, y + getHeight(s) / 2, s, renderer);
	}
	
	public float getWidth(String s){
		
		float maxWidth = 0;
		
		for(String str : s.split("\n")){
			
			int width = 0;
			
			for(char c : str.toCharArray()){
				width += chars[c].getAdvancedX();
			}
			
			if(width > maxWidth)
				maxWidth = width;
			
		}
		
		return maxWidth;
	}
	
	public float getHeight(String s){
		float height = lineHeight;
		for(char c : s.toCharArray()){
			if(c == '\n')
				height += lineHeight;
		}
		
		return height;
	}
	
	public float getBase(){
		return base;
	}
	
	public void setBase(float newBase){
		this.base = newBase;
	}

	public float getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}

	public Texture getBaseImage() {
		return baseImage;
	}

	public void setBaseImage(Texture baseImage) {
		this.baseImage = baseImage;
	}

	public CharDescriptor[] getChars() {
		return chars;
	}
	
}
