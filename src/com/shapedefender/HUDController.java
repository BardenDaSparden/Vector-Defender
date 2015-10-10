package com.shapedefender;

import org.javatroid.core.Resources;
import org.javatroid.core.Window;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.text.BitmapFont;
import com.shapedefender.objects.Player;

public class HUDController {

	private Player player;
	private Texture liveTexture, bombTexture;
	private BitmapFont defaultFont, scoreFont;
	
	public HUDController(Player p){
		player = p;
		defaultFont = Resources.getFont("imagine16");
		scoreFont = Resources.getFont("imagine18");
		liveTexture = Resources.getTexture("player");
		bombTexture = Resources.getTexture("bomb");
	}
	
	public void draw(SpriteBatch spriteBatch){
		
		spriteBatch.begin();
		spriteBatch.setColor(1, 1, 1, 1);
		
		int lives = player.getStats().getLiveCount();
		int bombs = player.getStats().getBombCount();
		float startX = -Window.getWidth() / 2.0f + 30;
		float startY = Window.getHeight() / 2 - 46;
		
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
	    scoreFont.drawStringCentered(0, Window.getHeight() / 2 - 46, player.getStats().getScore() + "", spriteBatch);
	    defaultFont.drawStringCentered(0.5f, Window.getHeight() / 2 - 90, "Multiplier : " + player.getStats().getMultiplier(), spriteBatch);
	    
	    spriteBatch.setColor(1.0F, 1.0F, 1.0F, 0.4F);
	    defaultFont.drawString(-Window.getWidth() / 2 + 20, -Window.getHeight() / 2 + 70, "PRE-ALPHA 0.12", spriteBatch);
	    defaultFont.drawString(-Window.getWidth() / 2 + 20, -Window.getHeight() / 2 + 40, "BY Branden Monroe", spriteBatch);
		
	    spriteBatch.end();
	}
	
}