package org.javatroid.graphics;

public class TextureAtlas {

	private Texture baseTexture;
	private int cols = 0;
	private int rows = 0;
	
	TextureRegion regions[];
	
	public TextureAtlas(Texture baseTexture, int rows, int cols){
		this.rows = rows;
		this.cols = cols;
		this.baseTexture = baseTexture;
		this.regions = new TextureRegion[rows * cols];
		
		float cellWidth = (float) baseTexture.getWidth() / (float) cols;
		float cellHeight = (float) baseTexture.getHeight() / (float) rows;
		
		for(int j = rows - 1; j > -1; j--){
			for(int i = 0; i < cols; i++){
				
				float srcX = (float)i * cellWidth;
				float srcY = (float)j * cellHeight;
				
				int index = ((rows - 1) - j) * cols + i;
				
				regions[index] = new TextureRegion(baseTexture, srcX, srcY, cellWidth, cellHeight);
				
			}
		}
		
	}
	
	public TextureRegion getRegion(int i , int j){
		return regions[j * cols + i];
	}
	
	public Texture getBaseTexture(){
		return baseTexture;
	}
	
	public int getMaxIndex(){
		return rows * cols;
	}
	
}
