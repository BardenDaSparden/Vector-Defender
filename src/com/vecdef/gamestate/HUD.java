package com.vecdef.gamestate;

import java.text.DecimalFormat;

import org.javatroid.core.Resources;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.text.BitmapFont;

import com.vecdef.objects.Player;
import com.vecdef.objects.PlayerStats;

public class HUD {

	private Renderer renderer;
	private OrthogonalCamera camera;
	private PlayState state;
	private Player player;
	private int width;
	private int height;
	
	DecimalFormat scoreFormat;
	DecimalFormat minSecFormat;
	DecimalFormat partFormat;
	
	private BitmapFont techFontSmall;
	private BitmapFont techFontMedium;
	private BitmapFont techFontLarge;
	
	private Texture energyBarOutline;
	private Texture livesBarOutline;
	private Texture life;
	private Texture white;
	
	public HUD(PlayState pState, Scene scene, Renderer renderer, int width, int height){
		this.state = pState;
		this.renderer = renderer;
		this.width = width;
		this.height = height;
		camera = new OrthogonalCamera(width, height);
		player = scene.getPlayer();
		
		scoreFormat = new DecimalFormat("###,###,###");
		minSecFormat = new DecimalFormat("00");
		partFormat = new DecimalFormat("000");
		
		techFontSmall = Resources.getFont("tech18");
		techFontMedium = Resources.getFont("tech30");
		techFontLarge = Resources.getFont("tech36");
		
		energyBarOutline = Resources.getTexture("energyBar");
		livesBarOutline = Resources.getTexture("livesBar");
		life = Resources.getTexture("life");
		white = Resources.getTexture("blank");
	}
	
	public void draw(){
		renderer.setCamera(camera);
		SpriteBatch batch = renderer.SpriteBatch();
		
		PlayerStats stats = player.getStats();
		
		long highscore = stats.getHighscore();
		long score= stats.getScore();
		int multiplier = stats.getMultiplier();
		int lives = stats.getLiveCount();
		int energy = stats.getEnergy();
		float ePercent = (float)energy / (float)PlayerStats.MAX_ENERGY;
		
		long frames = state.getFrameCount();
		
		long minutes = (frames / 1800);
		frames %= 1800;
		
		long seconds = (frames / 60);
		frames %= 60;
		
		long parts = frames * 16;
		
		batch.begin(BlendState.ALPHA);
			batch.setColor(1, 1, 1, 0.4f);
			techFontSmall.drawStringCentered(0, height / 2 - 25, "HIGHSCORE", batch);
			techFontMedium.drawStringCentered(0, height / 2 - 50, scoreFormat.format(highscore), batch);
			batch.setColor(1, 1, 1, 1);
			techFontSmall.drawString(-width / 2 + 25, height / 2 - 25, "SCORE", batch);
			techFontMedium.drawString(-width / 2 + 25, height / 2 - 50, scoreFormat.format(score), batch);
			float w = techFontMedium.getWidth(scoreFormat.format(score));
			techFontSmall.drawString(-width / 2 + 25 + w + 20, height / 2 - 59, "x " + multiplier, batch);
			
			w = techFontSmall.getWidth("TIME");
			techFontSmall.drawString(width / 2 - 25 - w, height / 2 - 25, "TIME", batch);
			w = techFontMedium.getWidth("00:00:000") - 7;
			techFontMedium.drawString(width / 2 - 25 - w, height / 2 - 50, minSecFormat.format(minutes) + ":" + minSecFormat.format(seconds) + ":" + partFormat.format(parts), batch);
			
			w = techFontSmall.getWidth("ENERGY: ");
			techFontSmall.drawString(width / 2 - 25 - energyBarOutline.getWidth() - w - 15, -height / 2 + 85, "ENERGY: ", batch);
			
			w = techFontSmall.getWidth("LIVES: ");
			techFontSmall.drawString(width / 2 - 25 - livesBarOutline.getWidth() - w - 15, -height / 2 + 45, "LIVES: ", batch);
			
			batch.setColor(0.35f, 1, 1, 1);
			w = ePercent * energyBarOutline.getWidth();
			batch.draw(width / 2 - 25 - energyBarOutline.getWidth() / 2, -height / 2 + 75, energyBarOutline.getWidth(), energyBarOutline.getHeight(), 0, energyBarOutline);
			batch.draw(width / 2 - 25 - energyBarOutline.getWidth() + w / 2, -height / 2 + 75, w, energyBarOutline.getHeight(), 0, white);
			
			batch.setColor(1, 0, 0, 1);
			float lifeWidth = 24;
			float lifeHeight = 11;
			float spacing = 2;
			for(int i = 0; i < lives; i++){
				float startX = width / 2 - lifeWidth - livesBarOutline.getWidth() + lifeWidth / 2 + spacing;
				batch.draw(startX + (i * spacing) + (i * lifeWidth), -height / 2 + 34, 23.5f, lifeHeight, 0, white);
			}
			
			batch.setColor(1, 1, 1, 0.3f);
			techFontSmall.drawString(-width / 2 + 25, - height / 2 + 34, "PREVIEW BUILD 11-2-15", batch);
		batch.end();
//		batch.begin(BlendState.ADDITIVE);
//			batch.draw(0, 0, Display.getWidth(), Display.getHeight(), 0, Resources.getTexture("HUD"));
//		batch.end();
//		SpriteBatch spriteBatch = renderer.SpriteBatch();
//		
//		glViewport(0, 0, Display.getWidth(), Display.getHeight());
//		spriteBatch.setShader(null);
//		spriteBatch.begin();
//		spriteBatch.setColor(1, 1, 1, 1);
//		
//		int lives = player.getStats().getLiveCount();
//		int bombs = player.getStats().getBombCount();
//		long score = player.getStats().getScore();
//		long highscore = player.getStats().getHighscore();
//		int multiplier = player.getStats().getMultiplier();
//		
//		float startX = -Display.getWidth() / 2.0f + 30;
//		float startY = Display.getHeight() / 2 - 46;
//		
//		defaultFont.drawString(startX, startY, "Lives : ", spriteBatch);
//		
//		startX += 120;
//		startY -= 12;
//		
//		if(lives< 5){
//			for(int i = 0; i < lives; i++){
//				float offsetX = i * 30;
//				spriteBatch.draw(startX + offsetX, startY, liveTexture.getWidth() * 0.5f, liveTexture.getHeight() * 0.5f, 90, liveTexture);
//			}
//		} else {
//			startX -= 8;
//			startY += 12;
//			defaultFont.drawString(startX + 10, startY, lives+"", spriteBatch);
//		}
//		
//		startX -= 120;
//		startY -= 30;
//		
//		defaultFont.drawString(startX, startY, "Bombs : ", spriteBatch);
//		
//		startX += 120;
//		startY -= 15;
//		
//		if(bombs < 5){
//			for(int i = 0; i < bombs; i++){
//				float offsetX = i * 30;
//				spriteBatch.draw(startX + offsetX, startY, bombTexture.getWidth(), bombTexture.getHeight(), 0, bombTexture);
//			}
//		} else {
//			//startX -= 8;
//			startY += 12;
//			defaultFont.drawString(startX + 10, startY, bombs+"", spriteBatch);
//		}
//		
//		spriteBatch.setColor(1.0F, 1.0F, 1.0F, 1.0F);
//		
//		String str = "Highscore  " + " " + highscore;
//	    scoreFont.drawStringCentered(0, Display.getHeight() / 2 - 30, str, spriteBatch);
//		
//		float scoreWidth = scoreFont.getWidth(score+"");
//		spriteBatch.setColor(0.50f, 0.50f, 0.50f, 1);
//	    scoreFont.drawStringCentered(0, Display.getHeight() / 2 - 96, score+"", spriteBatch);
//	    defaultFont.drawString(scoreWidth / 2 + 10, Display.getHeight() / 2 - 92, "x " + multiplier, spriteBatch);
//	    spriteBatch.setColor(1, 1, 1, 1);
//	    
//	    spriteBatch.end();
	}
	
}