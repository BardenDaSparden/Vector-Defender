package com.vecdef.gamestate;

import java.util.ArrayList;

import com.vecdef.objects.BlackHole;
import com.vecdef.objects.Bullet;
import com.vecdef.objects.Enemy;
import com.vecdef.objects.MultiplierPiece;
import com.vecdef.objects.Particle;
import com.vecdef.objects.Player;

public class Scene {

	Player player;
	ArrayList<Bullet> bullets;
	ArrayList<Enemy> enemies;
	ArrayList<BlackHole> blackholes;
	ArrayList<Particle> particles;
	ArrayList<MultiplierPiece> multiPieces;
	
	public Scene(){
		player = new Player();
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		blackholes = new ArrayList<BlackHole>();
		multiPieces = new ArrayList<MultiplierPiece>();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public ArrayList<Bullet> getBullets(){
		return bullets;
	}
	
	public ArrayList<Enemy> getEnemies(){
		return enemies;
	}
	
	public ArrayList<BlackHole> getBlackholes(){
		return blackholes;
	}
	
	public ArrayList<Particle> getParticles(){
		return particles;
	}
	
	public ArrayList<MultiplierPiece> getMultiplierPieces(){
		return multiPieces;
	}
	
}
