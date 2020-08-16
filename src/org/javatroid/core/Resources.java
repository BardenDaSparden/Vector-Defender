package org.javatroid.core;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PITCH;
//import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_SOURCE_RELATIVE;
import static org.lwjgl.openal.AL10.AL_TRUE;
//import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
//import static org.lwjgl.openal.AL10.alSource;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.javatroid.audio.Sound;
import org.javatroid.error.UnloadedResourceException;
import org.javatroid.graphics.Texture;
import org.javatroid.text.BitmapFont;
import org.javatroid.text.CharDescriptor;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.WaveData;

public class Resources {

	private static HashMap<String, BitmapFont> fonts;
	private static HashMap<String, Sound> sounds;
	private static HashMap<String, Texture> textures;
	
	static{
		fonts = new HashMap<String, BitmapFont>();
		sounds = new HashMap<String, Sound>();
		textures = new HashMap<String, Texture>();
	}
	
	public static void loadFont(String fontPath, String key){
		if(fonts.containsKey(key))
			return;
		
		InputStream stream = ClassLoader.getSystemResourceAsStream(fontPath);
		Scanner reader = new Scanner(stream);
		
		String imageName = "";
		BitmapFont font = new BitmapFont();
		
		String line = "";
		String readType = "";
		
		while(reader.hasNextLine()){
			
			line = reader.nextLine();
			readType = line.substring(0, line.indexOf(' ', 0));
			
			if(readType.equals("info")){
				
			} else if(readType.equals("common")){
				
				String[] tokens = line.substring(line.indexOf(' ', 0) + 1).split(" ");
				String k = "";
				
				int value = 0;
				
				for(int i = 0; i < tokens.length; i++){
					String token = tokens[i].trim();
					
					int pos = token.indexOf('=');
					
					k = token.substring(0, pos);
					value = Integer.parseInt(token.substring(pos + 1, token.length()));
					
					if(k.equals("lineHeight")){
						font.setLineHeight(value);
					} else if(k.equals("base")){
						font.setBase(value);
					} else if(k.equals("scaleW")){
						//font.width = value;
					} else if(k.equals("scaleH")){
						//font.height = value;
					}
					
				}
			} else if(readType.equals("page")){
				
				String[] tokens = line.substring(line.indexOf(' ', 0) + 1, line.length()).split(" +");
				String k = "";
				String value = "";
				
				for(int i = 0; i < tokens.length; i++){
					String token = tokens[i];
					
					int pos = token.indexOf('=');
					k = token.substring(0, pos);
					value = token.substring(pos + 1, token.length());
					
					if(k.equals("file")){
						imageName = value.substring(1, value.length() - 1);
					}
					
				}
				
			} else if(readType.equals("chars")){
				
			} else if(readType.equals("char")){
				
				String[] tokens = line.substring(line.indexOf(' ', 0) + 1).trim().split(" +");
				
				String k = "";
				int value = 0;
				
				int charID = Integer.parseInt(tokens[0].substring(tokens[0].indexOf('=') + 1, tokens[0].length()));
				CharDescriptor descriptor = font.getChars()[charID];
				
				for(int i = 0; i < tokens.length; i++){
					String token = tokens[i];
					int pos = token.indexOf('=');
					
					k = token.substring(0, pos);
					value = Integer.parseInt(token.substring(pos + 1, token.length()));
					
					if(k.equals("x")){
						descriptor.setX(value);
					} else if(k.equals("y")){
						descriptor.setY(value);
					} else if(k.equals("width")){
						descriptor.setWidth(value);
					} else if(k.equals("height")){
						descriptor.setHeight(value);
					} else if(k.equals("xoffset")){
						descriptor.setOffsetX(value);
					} else if(k.equals("yoffset")){
						descriptor.setOffsetY(value);
					} else if(k.equals("xadvance")){
						descriptor.setAdvancedX(value);
					}
				}
			}
			
		}
		
		reader.close();
		
		String baseImagePath = fontPath.substring(0, fontPath.lastIndexOf('/')) + "/" + imageName;
		loadTexture(baseImagePath, baseImagePath);
		font.setBaseImage(getTexture(baseImagePath));
		
		fonts.put(key, font);
		
	}
	
	public static void loadSound(String soundPath, String key){
		
		InputStream is = ClassLoader.getSystemResourceAsStream(soundPath);
		
		BufferedInputStream stream = null;
		
		stream = new BufferedInputStream(is);
		
		int source = alGenSources();
		int buffer = alGenBuffers();
		
		WaveData soundData = WaveData.create(stream);
		alBufferData(buffer, soundData.format, soundData.data, soundData.samplerate);	
		soundData.dispose();
		
		FloatBuffer position = BufferUtils.createFloatBuffer(3).put(new float[] {0, 1, 0});
		FloatBuffer velocity = BufferUtils.createFloatBuffer(3).put(new float[] {0, 0, 0});
		
		position.flip();
		velocity.flip();
		
		alSourcei(source, AL_BUFFER, buffer);
		alSourcef(source, AL_GAIN, 1.0f);
		alSourcef(source, AL_PITCH, 1.0f);
		//alSourcei(source, AL_POSITION, position);
		//alSourcei(source, AL_VELOCITY, velocity);
		alSourcei(source, AL_SOURCE_RELATIVE, AL_TRUE);
		
		Sound audio = new Sound(source, buffer);
		
		sounds.put(key, audio);
		
	}
	
	public static void loadTexture(String texturePath, String key){
		
		InputStream stream = ClassLoader.getSystemResourceAsStream(texturePath);
		BufferedImage image = null;
		
		int width = 0;
		int height = 0;
		
		try{
			image = ImageIO.read(stream);
			width = image.getWidth();
			height = image.getHeight();
		} catch (IOException ex){
			ex.printStackTrace();
		}
		
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		ByteBuffer pixelData = BufferUtils.createByteBuffer(width * height * 4);
		
		//Read the image from bottom-left, to top-right
		
		for(int j = height - 1; j > -1; j--){
			for(int i = 0; i < width; i++){
				int pixel = pixels[j * width + i];
				
				byte r = (byte) ((pixel >> 16) & 0xFF);
				byte g = (byte) ((pixel >> 8) & 0xFF);
				byte b = (byte) ((pixel) & 0xFF);
				byte a = (byte) ((pixel >> 24) & 0xFF);
				
				pixelData.put(r).put(g).put(b).put(a);
			}
		}
		
		pixelData.flip();
		
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelData);
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor4f(1, 1, 1, 1);
		
		Texture texture = new Texture(textureID, width, height);
		
		textures.put(key, texture);
		
	}
	
public static void loadTexture(InputStream stream, String key){
		
		BufferedImage image = null;
		
		int width = 0;
		int height = 0;
		
		try{
			image = ImageIO.read(stream);
			width = image.getWidth();
			height = image.getHeight();
		} catch (IOException ex){
			ex.printStackTrace();
		}
		
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		ByteBuffer pixelData = BufferUtils.createByteBuffer(width * height * 4);
		
		//Read the image from bottom-left, to top-right
		
		for(int j = height - 1; j > -1; j--){
			for(int i = 0; i < width; i++){
				int pixel = pixels[j * width + i];
				
				byte r = (byte) ((pixel >> 16) & 0xFF);
				byte g = (byte) ((pixel >> 8) & 0xFF);
				byte b = (byte) ((pixel) & 0xFF);
				byte a = (byte) ((pixel >> 24) & 0xFF);
				
				pixelData.put(r).put(g).put(b).put(a);
			}
		}
		
		pixelData.flip();
		
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelData);
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor4f(1, 1, 1, 1);
		
		Texture texture = new Texture(textureID, width, height);
		
		textures.put(key, texture);
		
	}
	
	public static BitmapFont getFont(String key){
		if(!fonts.containsKey(key))
			throwResourceError("Font", key);
		return fonts.get(key);
	}
	
	public static Sound getSound(String key){
		if(!sounds.containsKey(key))
			throwResourceError("Sound", key);
		return sounds.get(key);
	}
	
	public static Texture getTexture(String key){
		if(!textures.containsKey(key))
			throwResourceError("Texture", key);
		return textures.get(key);
	}
	
	private static void throwResourceError(String type, String key){
		throw new UnloadedResourceException("Resource of type " + type + " hasn't been loaded, given the key : " + key + ".");
	}
	
	public static void unloadAll(){
		for(Texture texture : textures.values()){
			glDeleteTextures(texture.getTextureHandle());
		}
		
		for(BitmapFont font : fonts.values()){
			glDeleteTextures(font.getBaseImage().getTextureHandle());
		}
		
		for(Sound sound : sounds.values()){
			alDeleteBuffers(sound.getBufferHandle());
			alDeleteSources(sound.getSourceHandle());
		}
		
		fonts.clear();
		sounds.clear();
		textures.clear();
		
	}
	
}
