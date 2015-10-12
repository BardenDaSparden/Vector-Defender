package com.vecdef.gamestate;

import org.javatroid.core.Resources;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.text.BitmapFont;
import org.lwjgl.opengl.Display;

import com.vecdef.objects.Player;

public class HUDController {

	private OrthogonalCamera camera;
	private Player player;
	private Texture liveTexture, bombTexture;
	private BitmapFont defaultFont, scoreFont;
	
	public HUDController(Scene scene, int width, int height){
		camera = new OrthogonalCamera(width, height);
		player = scene.getPlayer();
		defaultFont = Resources.getFont("imagine16");
		scoreFont = Resources.getFont("imagine18");
		liveTexture = Resources.getTexture("player");
		bombTexture = Resources.getTexture("bomb");
	}
	
	public void draw(Renderer renderer){
		
		renderer.setCamera(camera);
		SpriteBatch spriteBatch = renderer.SpriteBatch();
		
		spriteBatch.begin();
		spriteBatch.setColor(1, 1, 1, 1);
		
		int lives = player.getStats().getLiveCount();
		int bombs = player.getStats().getBombCount();
		float startX = -Display.getWidth() / 2.0f + 30;
		float startY = Display.getHeight() / 2 - 46;
		
		defaultFont.drawString(startX, startY, "Lives : ", spriteBatch);
		
		startX += 120;
		startY -= 12;
		
		if(lives< 5){
			for(int i = 0; i < lives; i++){
				float offsetX = i * 30;
				spriteBatch.draw(startX + offsetX, startY, liveTexture.getWidth() * 0.5f, liveTexture.getHeight() * 0.5f, 90, liveTexture);
			}
		} else {
			startX -= 8;
			startY += 12;
			defaultFont.drawString(startX + 10, startY, lives+"", spriteBatch);
		}
		
		startX -= 120;
		startY -= 30;
		
		defaultFont.drawString(startX, startY, "Bombs : ", spriteBatch);
		
		startX += 120;
		startY -= 15;
		
		if(bombs < 5){
			for(int i = 0; i < bombs; i++){
				float offsetX = i * 30;
				spriteBatch.draw(startX + offsetX, startY, bombTexture.getWidth(), bombTexture.getHeight(), 0, bombTexture);
			}
		} else {
			//startX -= 8;
			startY += 12;
			defaultFont.drawString(startX + 10, startY, bombs+"", spriteBatch);
		}
		
		spriteBatch.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	    scoreFont.drawStringCentered(0, Display.getHeight() / 2 - 46, player.getStats().getScore() + "", spriteBatch);
	    defaultFont.drawStringCentered(0.5f, Display.getHeight() / 2 - 90, "Multiplier : " + player.getStats().getMultiplier(), spriteBatch);
	    
	    spriteBatch.setColor(1.0F, 1.0F, 1.0F, 0.4F);
	    defaultFont.drawString(-Display.getWidth() / 2 + 20, -Display.getHeight() / 2 + 70, "PRE-ALPHA 0.12", spriteBatch);
	    defaultFont.drawString(-Display.getWidth() / 2 + 20, -Display.getHeight() / 2 + 40, "BY Branden Monroe", spriteBatch);
		
	    spriteBatch.end();
	}
	
}