package org.barden.util;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

//import org.barden.math.Vector2f;
//import org.barden.math.Vector3f;
//import org.barden.math.Vector4f;
import org.lwjgl.BufferUtils;

public class GLUtil {

	private static ArrayList<Integer> loadedVBOs = new ArrayList<Integer>();
	private static ArrayList<Integer> loadedVAOs = new ArrayList<Integer>();
	
	public static FloatBuffer createFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
//	public static FloatBuffer createFloatBuffer(Vector2f[] data){
//		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 2);
//		for(Vector2f v : data)
//			v.store(buffer);
//		buffer.flip();
//		return buffer;
//	}
//	
//	public static FloatBuffer createFloatBuffer(Vector3f[] data){
//		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 3);
//		for(Vector3f v : data){
//			v.store(buffer);
//		}
//		buffer.flip();
//		return buffer;
//	}
//	
//	public static FloatBuffer createFloatBuffer(Vector4f[] data){
//		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 4);
//		for(Vector4f v : data)
//			v.store(buffer);
//		buffer.flip();
//		return buffer;
//	}
	
	public static ByteBuffer createByteBuffer(byte[] data){
		ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static ShortBuffer createShortBuffer(short[] data){
		ShortBuffer buffer = BufferUtils.createShortBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer createIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static int createVBO(FloatBuffer data, int drawType){
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, drawType);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		loadedVBOs.add(vboID);
		return vboID;
	}
	
	public static int createVBO(IntBuffer data, int drawType){
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, drawType);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		loadedVBOs.add(vboID);
		return vboID;
	}
	
	public static int createVBO(ByteBuffer data, int drawType){
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, drawType);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		loadedVBOs.add(vboID);
		return vboID;
	}
	
	public static int createVBO(ShortBuffer data, int drawType){
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, drawType);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		loadedVBOs.add(vboID);
		return vboID;
	}
	
	public static int createIBO(IntBuffer data, int drawType){
		int iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, drawType);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		loadedVBOs.add(iboID);
		return iboID;
	}
	
	public static int createIBO(ShortBuffer data, int drawType){
		int iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, drawType);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		loadedVBOs.add(iboID);
		return iboID;
	}
	
	public static int createIBO(ByteBuffer data, int drawType){
		int iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, drawType);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		loadedVBOs.add(iboID);
		return iboID;
	}
	
	public static int createVAO(){
		int vaoID = glGenVertexArrays();
		
		loadedVAOs.add(vaoID);
		return vaoID;
	}
	
	public static void deleteVBO(int vbo){
		loadedVBOs.remove((Object)vbo);
		glDeleteBuffers(vbo);
	}
	
	public static void deleteVAO(int vao){
		loadedVAOs.remove((Object)vao);
		glDeleteVertexArrays(vao);
	}
	
	public static void deleteAll(){
		for(int vbo : loadedVBOs)
			glDeleteBuffers(vbo);
		for(int vao : loadedVAOs){
			glDeleteVertexArrays(vao);
		}
	}
	
}
