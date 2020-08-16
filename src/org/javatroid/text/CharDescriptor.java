package org.javatroid.text;

public class CharDescriptor {

	private float x, y;
	private float width, height;
	private float offsetX, offsetY;
	private float advancedX;
	
	public CharDescriptor(){
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.offsetX = 0;
		this.offsetY = 0;
		this.advancedX = 0;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public float getAdvancedX() {
		return advancedX;
	}

	public void setAdvancedX(float advancedX) {
		this.advancedX = advancedX;
	}

	public String toString(){
		String s = "";
		s += x + " ";
		s += y + " ";
		s += width + " ";
		s += height + " ";
		s += offsetX + " ";
		s += offsetY + " ";
		s += advancedX + " ";
		return s;
	}
	
}
