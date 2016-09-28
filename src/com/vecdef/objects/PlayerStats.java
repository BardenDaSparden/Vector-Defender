package com.vecdef.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class PlayerStats{
	
	final String HIGHSCORE_INPUT_PATH = "data/highscore";
	final String HIGHSCORE_OUTPUT_PATH = "resources/data/highscore";
	
	public static final int MAX_LIVES = 8;
	public static final int MAX_ENERGY = 4000;
	
	private long score = 0;
	private int multiplier = 1;
	private int lives = 3;
	private int energy = 0;
	private long highscore;
	
	public void reset(){
		score = 0;
		multiplier = 1;
		lives = 3;
		energy = 0;
		highscore = loadHighscore();
	}

	public long loadHighscore(){
		long hs = -1;
		InputStream stream = ClassLoader.getSystemResourceAsStream(HIGHSCORE_INPUT_PATH);
		if(stream != null){
			Scanner scanner = new Scanner(stream);
			hs = Long.parseLong(scanner.nextLine().trim());
			scanner.close();
		}
		return hs;
	}
	
	public void writeHighscore(){
		try {
			File file = new File(HIGHSCORE_OUTPUT_PATH);
			if(!file.exists()){
				file.createNewFile();
			}
			OutputStream stream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
			writer.write(highscore+"");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("\nWARNING: Unable to write to " + HIGHSCORE_OUTPUT_PATH);
			e.printStackTrace();
		}
	}
	
	public long getHighscore(){
		return highscore;
	}
	
	public void addScore(int s){
		score += (s * multiplier);
		if(score > highscore)
			highscore = score;
	}
	
	public void addEnergy(int e){
		energy += e;
		if(energy > MAX_ENERGY)
			energy = MAX_ENERGY;
	}
	
	public boolean hasEnergy(int e){
		return (energy - e) > 0;
	}
	
	public boolean useEnergy(int e){
		boolean b = hasEnergy(e);
		if(b)
			energy -= e;
		return b;
	}
	
	public int getEnergy(){
		return energy;
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

	public long getScore(){
		return score;
	}

	public int getMultiplier(){
		return multiplier;
	}

	public int getLiveCount(){
		return lives;
	}

}