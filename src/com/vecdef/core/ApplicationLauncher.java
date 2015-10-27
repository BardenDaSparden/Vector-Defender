package com.vecdef.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import static org.lwjgl.opengl.GL11.*;

public class ApplicationLauncher {

	Application application;
	ApplicationSettings settings;
	
	public ApplicationLauncher(Application application, ApplicationSettings settings){
		this.application = application;
		this.settings = settings;
	}
	
	public void launch(){
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "" + settings.borderless);
		
		DisplayMode compatibleDisplay = null;
		if(settings.fullscreen){
			DisplayMode[] allDisplays = null;
			try {
				allDisplays = Display.getAvailableDisplayModes();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			int n =  allDisplays.length;
			for(int i = 0; i < n; i++){
				DisplayMode dm = allDisplays[i];
				if(	dm.getWidth() == settings.width &&
					dm.getHeight() == settings.height &&
					dm.isFullscreenCapable() == settings.fullscreen){
					compatibleDisplay = dm;
					break;
				}
			}
		} else {
			compatibleDisplay = new DisplayMode(settings.width, settings.height);
		}
		
		PixelFormat format = new PixelFormat(32, 8, 0, 8, settings.multisamples);
		
		ContextAttribs attributes = new ContextAttribs(2, 0);
		attributes.withForwardCompatible(true);
		
		try {
			Display.setTitle(settings.title);
			Display.setDisplayMode(compatibleDisplay);
			Display.setResizable(settings.resizable);
			Display.setFullscreen(settings.fullscreen);
			Display.setVSyncEnabled(settings.vsync);
			Display.create(format, attributes);
			//glViewport(0, 0, Display.getWidth(), Display.getHeight());
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		long previousTime = System.nanoTime();
		long currentTime = previousTime;
		long unprocessedUpdateTime = 0;
		long unprocessedRenderTime = 0;
		final long UPDATE_STEP = (long)(1000000000D / (double)settings.updaterate);
		final long RENDER_STEP = (long)(1000000000D / (double)settings.renderrate);
		final int MAX_UPDATES_PER_FRAME = 3;
		final int MAX_RENDERS_PER_FRAME = 1;
		
		application.initialize();
		
		while(!Display.isCloseRequested()){
			previousTime = currentTime;
			currentTime = System.nanoTime();
			unprocessedUpdateTime += (currentTime - previousTime);
			unprocessedRenderTime += (currentTime - previousTime);
			
			int updates = 0;
			while(unprocessedUpdateTime >= UPDATE_STEP && updates < MAX_UPDATES_PER_FRAME){
				unprocessedUpdateTime -= UPDATE_STEP;
				application.update();
				updates++;
			}
			
			int renders = 0;
			while(unprocessedRenderTime >= RENDER_STEP && renders < MAX_RENDERS_PER_FRAME){
				unprocessedRenderTime -= RENDER_STEP;
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
				application.render();
				Display.update();
				renders++;
			}
		}
		
		application.destroy();
		AL.destroy();
		Display.destroy();
	}
	
}
