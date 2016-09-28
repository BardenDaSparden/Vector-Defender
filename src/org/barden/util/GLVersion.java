package org.barden.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import java.util.ArrayList;

public class GLVersion {
	
	public static boolean isExtensionSupported(String ext){
		return get().m_isExtensionSupported(ext);
	}
	
	public static GLVersion get(){
		if(instance == null)
			instance = new GLVersion();
		return instance;
	}
	
	private static GLVersion instance = null;
	private ArrayList<String> extensions;
	
	private GLVersion(){
		extensions = new ArrayList<String>();
		int n = glGetInteger(GL_NUM_EXTENSIONS);
		for(int i = 0; i < n; i++){
			String extension = glGetStringi(GL_EXTENSIONS, i);
			extensions.add(extension);
		}
	}
	
	public boolean m_isExtensionSupported(String extension){
		return extensions.contains(extension);
	}
	
}