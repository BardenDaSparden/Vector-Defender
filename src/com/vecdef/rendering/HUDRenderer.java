package com.vecdef.rendering;

import java.text.DecimalFormat;

import org.javatroid.core.Resources;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.math.Vector4f;
import org.javatroid.text.BitmapFont;

import com.vecdef.objects.HUD;
import com.vecdef.objects.Player;
import com.vecdef.objects.PlayerStats;
import com.vecdef.objects.Scene;

public class HUDRenderer {
	
	int width, height;
	SpriteBatch batch;
	OrthogonalCamera camera;
	
	DecimalFormat scoreFormat = new DecimalFormat("###,###,###");
	
	private BitmapFont techFontSmall;
	private BitmapFont techFontMedium;
	private BitmapFont squareFont;
	private Texture statBackground;
	
	public static final Vector4f P1_COLOR = new Vector4f(1, 1, 1, 1);
	public static final Vector4f P2_COLOR = new Vector4f(1, 0.75f, 0.45f, 1);
	public static final Vector4f P3_COLOR = new Vector4f(0.45f, 1, 0.45f, 1);
	public static final Vector4f P4_COLOR = new Vector4f(1, 0.45f, 1, 1);
	
	public HUDRenderer(int width, int height, Renderer renderer){
		this.width = width;
		this.height = height;
		batch = renderer.SpriteBatch();
		camera = new OrthogonalCamera(width, height);
		techFontSmall = Resources.getFont("tech18");
		techFontMedium = Resources.getFont("tech30");
		squareFont = Resources.getFont("square18");
		statBackground = Resources.getTexture("PlayerStatBackground");
	}
	
	void drawMessage(float x, float y){
		
	}
	
	void drawPlayerHUD(Player player, int playerID, float x, float y, Vector4f color){
		float incX = width / 4.0f;
		
		x += incX * playerID;
		String str = "Player " + (playerID + 1);
		
		PlayerStats stats = player.getStats();
		int energy = stats.getEnergy();
		int lives = stats.getLiveCount();
		
		batch.setColor(color.x, color.y, color.z, 0.35f);
		batch.draw(x, y, statBackground.getWidth(), statBackground.getHeight(), 0, statBackground);
		batch.setColor(color.x, color.y, color.z, 0.75f);
		techFontSmall.drawString(x - statBackground.getWidth() / 2.0f + 18, y + statBackground.getHeight() / 2.0f + 27, str, batch);
		squareFont.drawString(x - statBackground.getWidth() / 2.0f + 15, y + statBackground.getHeight() / 4.0f + 8, "Energy: " + energy, batch);
		squareFont.drawString(x - statBackground.getWidth() / 2.0f + 15, y + statBackground.getHeight() / 4.0f - 30 + 8, "Lives: " + lives, batch);
	}
	
	void drawPlayerScore(Player player, int playerID, float x, float y, Vector4f color){
		
		int incY = 40;
		y -= incY * playerID;
		
		PlayerStats stats = player.getStats();
		long score = stats.getScore();
		int multiplier = stats.getMultiplier();
		
		float strW = techFontMedium.getWidth(scoreFormat.format(score));
		
		float opacity = player.isAlive() ? 1.0f : 0.45f;
		
		batch.setColor(color.x, color.y, color.z, opacity);
		techFontMedium.drawString(x, y, scoreFormat.format(score), batch);
		techFontMedium.drawString(x + strW + 15, y, "x " + multiplier, batch);
	}
	
	public void draw(HUD hud){
		Scene scene = hud.getScene();
		
		Player p1 = scene.getPlayer();
		Player p2 = scene.getPlayer2();
		Player p3 = scene.getPlayer3();
		Player p4 = scene.getPlayer4();
		
		float startX = - width / 2.0f + statBackground.getWidth() / 2.0f + 40;
		float startY = - height / 2.0f + statBackground.getHeight() / 2.0f + 40;
		
		batch.setCamera(camera);
		batch.begin(BlendState.ADDITIVE);
			
			drawPlayerHUD(p1, 0, startX, startY, P1_COLOR);	//Draw Player 1 HUD
			
			if(p2.hasJoined())	//P2
				drawPlayerHUD(p2, 1, startX, startY, P2_COLOR);
			
			if(p3.hasJoined())	//P3
				drawPlayerHUD(p2, 2, startX, startY, P3_COLOR);
			
			if(p4.hasJoined())	//P4
				drawPlayerHUD(p2, 3, startX, startY, P4_COLOR);
			
		batch.end();
		
		startX = - width / 2.0f + 25;
		startY = height / 2.0f - 50 - 25;
		int totalScore = 0;	
		batch.begin(BlendState.ADDITIVE);
//			totalScore += p1.getStats().getScore();
//			drawPlayerScore(p1, 0, startX, startY, P1_COLOR);
//			
//			if(p2.hasJoined()){
//				totalScore += p2.getStats().getScore();
//				drawPlayerScore(p2, 1, startX, startY, P2_COLOR);
//			}
//			
//			if(p3.hasJoined()){
//				totalScore += p3.getStats().getScore();
//				drawPlayerScore(p3, 2, startX, startY, P3_COLOR);
//			}
//			
//			if(p4.hasJoined()){
//				totalScore += p4.getStats().getScore();
//				drawPlayerScore(p4, 3, startX, startY, P4_COLOR);
//			}
//			
//			batch.setColor(1, 1, 1, 1);
//			techFontMedium.drawString(-width / 2 + 25, height / 2 - 25, "SCORE: " + scoreFormat.format(totalScore), batch);
//			
			batch.setColor(1, 1, 1, 1);
			techFontMedium.drawStringCentered(0, height / 2.0f - 40, "Survival", batch);
			techFontSmall.drawStringCentered(0, height / 2.0f - 75, "Wave: 0", batch);
		
			if(hud.isCountdownDrawn()){
				batch.setColor(1, 1, 1, 1.0f - hud.countdownOpacity());
				techFontMedium.drawStringCentered(0, 0, hud.getCountdownValue(), batch);
			}
			
		batch.end();
	}
	
}
