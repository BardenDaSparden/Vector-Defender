package com.vecdef.gamestate;

import java.util.ArrayList;

import com.vecdef.objects.Bullet;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.Grid;
import com.vecdef.objects.MultiplierPiece;
import com.vecdef.objects.Particle;
import com.vecdef.objects.Player;

public class Scene {

	Player player;
	Grid grid;
	ArrayList<Bullet> bullets;
	ArrayList<Enemy> enemies;
	ArrayList<Particle> particles;
	ArrayList<MultiplierPiece> multiPieces;
	
	public Scene(){
		player = new Player();
		grid = new Grid(1920, 1080, 40, 40);
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		multiPieces = new ArrayList<MultiplierPiece>();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public ArrayList<Bullet> getBullets(){
		return bullets;
	}
	
	public ArrayList<Enemy> getEnemies(){
		return enemies;
	}
	
	public ArrayList<Particle> getParticles(){
		return particles;
	}
	
	public ArrayList<MultiplierPiece> getMultiplierPieces(){
		return multiPieces;
	}
	
}
