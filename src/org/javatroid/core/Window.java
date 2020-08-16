//package org.javatroid.core;
//
//import java.nio.ByteBuffer;
//
//import org.javatroid.graphics.Texture;
//import org.lwjgl.BufferUtils;
//import org.lwjgl.LWJGLException;
//import org.lwjgl.opengl.Display;
//import org.lwjgl.opengl.DisplayMode;
//import org.lwjgl.opengl.PixelFormat;
//
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
//
//public class Window {
//
//	public enum MSAA{
//		NONE(0), X1(1), X4(4), X8(8);
//		
//		int samples;
//		private MSAA(int samples){
//			this.samples = samples;
//		}
//	}
//	
//	private static String title = "Default Title";
//	private static int width = 800;
//	private static int height = 600;
//	private static boolean fullscreen = false;
//	private static boolean resizable = false;
//	private static boolean vsync = false;
//	private static MSAA msaa = MSAA.NONE;
//	
//	private static boolean useDesktopDisplay = false;
//	
//	private static Texture backbuffer;
//	private static ByteBuffer backbufferData;
//	
//	public static void create(){
//		try{
//			
//			Display.setTitle(title);
//			Display.setFullscreen(true);
//			Display.setResizable(resizable);
//			Display.setVSyncEnabled(vsync);
//			
//			DisplayMode displayMode = null;
//			if(useDesktopDisplay){
//				displayMode = Display.getDesktopDisplayMode();
//			} else {
//				displayMode = new DisplayMode(width, height);
//			}
//			
//			Display.setDisplayMode(displayMode);
//			
//			
//			if(msaa.samples != 0)
//				Display.create(new PixelFormat(32, 8, 0, 0, msaa.samples));
//			else
//				Display.create();
//			
//		} catch (LWJGLException e){
//			e.printStackTrace();
//		}
//		
//		int textureHandle = glGenTextures();
//		glBindTexture(GL_TEXTURE_2D, textureHandle);
//		
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//		
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//		
//		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
//		glBindTexture(GL_TEXTURE_2D, 0);
//		
//		backbuffer = new Texture(textureHandle, width, height);
//		backbufferData = BufferUtils.createByteBuffer(width * height * 4);
//	}
//	
//	public static Texture getBackbuffer(){
//		backbuffer.bind();
//		glFlush();
//		glReadBuffer(GL_BACK);
//		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, backbufferData);
//		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, backbufferData);
//		backbuffer.release();
//		return backbuffer;
//	}
//	
//	public static void update(){
//		Display.update();
//	}
//	
//	public static void destroy(){
//		Display.destroy();
//	}
//
//	public static String getTitle() {
//		return title;
//	}
//
//	public static void setTitle(String title) {
//		Window.title = title;
//	}
//
//	public static int getWidth() {
//		return width;
//	}
//
//	public static void setWidth(int width) {
//		Window.width = width;
//	}
//
//	public static int getHeight() {
//		return height;
//	}
//
//	public static void setHeight(int height) {
//		Window.height = height;
//	}
//
//	public static boolean isFullscreen() {
//		return fullscreen;
//	}
//
//	public static void setFullscreen(boolean fullscreen) {
//		Window.fullscreen = fullscreen;
//	}
//
//	public static boolean isResizable() {
//		return resizable;
//	}
//
//	public static void setResizable(boolean resizable) {
//		Window.resizable = resizable;
//	}
//
//	public static boolean isVsync() {
//		return vsync;
//	}
//
//	public static void setVsync(boolean vsync) {
//		Window.vsync = vsync;
//	}
//
//	public static MSAA getMSAA() {
//		return msaa;
//	}
//
//	public static void setMSAA(MSAA msaa) {
//		Window.msaa = msaa;
//	}
//	
//	public static boolean isCloseRequested(){
//		return Display.isCloseRequested();
//	}
//	
//	public static void useDesktopDisplayMode(boolean useDesktopDisplay){
//		Window.useDesktopDisplay = useDesktopDisplay;
//		Window.width = Display.getDesktopDisplayMode().getWidth();
//		Window.height = Display.getDesktopDisplayMode().getHeight();
//	}
//	
//	public static void viewport(int x, int y, int width, int height){
//		glViewport(x, y, width, height);
//	}
//	
//}
