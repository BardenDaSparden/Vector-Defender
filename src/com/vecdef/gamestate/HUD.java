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

	private Scene scene;
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
	
	private boolean bDebug;
	
	public HUD(PlayState pState, Scene scene, Renderer renderer, int width, int height){
		this.state = pState;
		this.renderer = renderer;
		this.width = width;
		this.height = height;
		this.scene = scene;
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
		
		bDebug = true;
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
			
			if(bDebug){
				techFontSmall.drawString(-width / 2, 30, "Physics Objects: " + scene.getPhysicsObjectCount(), batch);
				techFontSmall.drawString(-width / 2, 0, "Collision Objects: " + scene.getCollisionObjectCount(), batch);
				techFontSmall.drawString(-width / 2, -30, "Render Objects: " + scene.getRenderObjectCount(), batch);
				techFontSmall.drawString(-width / 2, -60, "Entity Count: " + scene.getRenderObjectCount(), batch);
			}
			
		batch.end();
	}
	
	public void toggleDebugMode(){
		bDebug = !bDebug;
	}
	
}