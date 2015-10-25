package com.vecdef.core;

import java.io.InputStream;

public class MinimFileHandler {
	
	public String sketchPath(String fileName){
		return fileName;
	}
	
	public InputStream createInput(String fileName){
		InputStream fis = this.getClass().getResourceAsStream("/resources/" + fileName);
		return fis;
	}
}