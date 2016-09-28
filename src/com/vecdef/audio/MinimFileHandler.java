package com.vecdef.audio;

import java.io.InputStream;

public class MinimFileHandler {
	
	public String sketchPath(String filename){
		return filename;
	}
	
	public InputStream createInput(String filename){
		InputStream stream = ClassLoader.getSystemResourceAsStream("music/" + filename);
		if(stream == null){
			System.err.println("WARNING (MinimFileHandler): Unable to locate file " + filename);
		}
		return stream;
	}
}