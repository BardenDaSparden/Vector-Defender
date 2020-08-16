package org.javatroid.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bitmap {

	private int width, height;
	private int[] pixels;
	
	public Bitmap(String imagePath){
		
		File file = new File(imagePath);
		BufferedImage image = null;
		
		try{
			image = ImageIO.read(file);
		} catch (IOException e){
			e.printStackTrace();
		}
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
	}
	
	public int getPixel(int i, int j){
		return pixels[j * width + i];
	}
	
	public void setPixel(int i, int j, int color){
		pixels[j * width + i] = color;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
}
