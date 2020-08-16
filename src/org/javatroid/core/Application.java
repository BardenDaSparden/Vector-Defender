//package org.javatroid.core;
//
//import org.lwjgl.LWJGLException;
//import org.lwjgl.openal.AL;
//
//public class Application{
//
//	BaseGame game;
//	int desiredFPS;
//	boolean bRunning = false;
//    
//    public Application(int framerate, BaseGame game){
//       	this.desiredFPS = framerate;
//       	this.game = game;
//    }
//
//    private void createDisplay(){  
//    	game.setupWindow();
//    	Window.create();
//        
//        try {
//			AL.create();
//		} catch (LWJGLException e) {
//			e.printStackTrace();
//		}
//    }
//
//    private void initialize(){
//        createDisplay();
//        game.create();
//    }
//
//    private void update(){
//    	game.update();
//        if(Window.isCloseRequested()) stop();
//    }
//
//    private void draw(float interpolation){
//    	game.draw(interpolation);
//        Window.update();
//    }
//
//    private void exit(){
//    	game.dispose();
//        AL.destroy();
//        Window.destroy();
//    }
//
//    private void gameLoop(){
//    	
//    	final long NANOS_PER_FRAME = 1000000000 / desiredFPS;
//		
//		long current = System.nanoTime();
//		long previous = System.nanoTime();
//		long elapsed = 0;
//		long lag = 0;
//		double interpolation = 0;
//		
//		while(bRunning){
//			previous = current;
//			current = System.nanoTime();
//			elapsed = current - previous;
//			lag += elapsed;
//			
//			while(lag >= NANOS_PER_FRAME){
//				update();
//				lag -= NANOS_PER_FRAME;
//			}
//			
//			interpolation = (double)lag / (double)NANOS_PER_FRAME;
//			draw((float)interpolation);
//		}
//    }
//    
//    public void stop(){
//        bRunning = false;
//    }
//
//    public void start(){
//    	bRunning = true;
//        initialize();
//        gameLoop();
//        exit();
//    }
//}
