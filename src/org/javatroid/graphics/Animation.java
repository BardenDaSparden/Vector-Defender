package org.javatroid.graphics;


public class Animation {

	Texture baseTexture;
	int cols;
	int rows;
	
	int currentTime = 0;
	int timePerFrame;
	
    int currentFrame = 0;
	TextureRegion[] frames;
	
	public Animation(Texture baseTexture, int columns, int rows, int timePerFrame){
		this.baseTexture = baseTexture;
		this.cols = columns;
		this.rows = rows;
		this.timePerFrame = timePerFrame;
		this.frames = new TextureRegion[rows * cols];
		
		generateFrames();
		
	}
	
	private void generateFrames(){
		float frameWidth = baseTexture.getWidth() / cols;
		float frameHeight = baseTexture.getHeight() / rows;
		
		for(int j = rows - 1; j > - 1; j--){
			for(int i = 0; i < cols; i++){
				float sampleX = i * frameWidth;
				float sampleY = j * frameHeight;
				TextureRegion frame = new TextureRegion(baseTexture, sampleX, sampleY, frameWidth, frameHeight);
				
				int index = ((rows - (j + 1)) * cols) + i;
				//System.out.println(index);
				
				frames[index] = frame;
			}
		}
	}
	
	public void update(){
		if(currentTime >= timePerFrame){
			if(currentFrame == frames.length - 1){
				
			}
			
			nextFrame();
			currentTime = 0;
			
		}
		
		currentTime++;
	}
	
	public void nextFrame(){
		currentFrame = (currentFrame + 1) % frames.length;
	}
	
	public void previousFrame(){
		currentFrame = (currentFrame - 1) % frames.length;
	}
	
	public void setFrame(int index){
		currentFrame = (index) % frames.length;
	}
	
	public void reset(){
		currentTime = 0;
		currentFrame = 0;
	}
	
	public float getFrameTime(){
		return timePerFrame;
	}
	
	public void setFrameTime(int tickPerFrame){
		this.timePerFrame = tickPerFrame;
	}
	
	public TextureRegion getCurrentFrame(){
		return frames[currentFrame];
	}
	
}
