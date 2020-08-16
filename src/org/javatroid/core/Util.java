package org.javatroid.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {

	public static String loadFileAsString(String filePath){
		
		InputStream stream = ClassLoader.getSystemResourceAsStream(filePath);
		
		//File file = new File(filePath);
		BufferedReader reader = null;
		
		StringBuilder string = new StringBuilder();
		
		try{
			
			reader = new BufferedReader(new InputStreamReader(stream));
			String line = "";
			
			while((line = reader.readLine()) != null){
				string.append(line);
				string.append("\n");
			}
			
			reader.close();
			
			
		} catch (IOException e){
			
			e.printStackTrace();
			
		}
		
		return string.toString();
		
	}
	
}
