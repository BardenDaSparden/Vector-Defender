package com.shapedefender.objects;

public class PlayerStats{
	
	private int score = 0;
	private int multiplier = 1;
	private int lives = 3;
	private int bombs = 4;

	public PlayerStats(){

	}

	public void reset(){
		score = 0;
		multiplier = 1;
		lives = 3;
		bombs = 3;
	}

	public void addScore(int s){
		score += (s * multiplier);
	}

	public void increaseMultiplier(){
		multiplier++;
	}
	
	public void resetMultiplier(){
		multiplier = 0;
	}

	public void useLife(){
		lives--;
	}

	public void useBomb(){
		bombs--;
	}

	public int getScore(){
		return score;
	}

	public int getMultiplier(){
		return multiplier;
	}

	public int getLiveCount(){
		return lives;
	}

	public int getBombCount(){
		return bombs;
	}

}