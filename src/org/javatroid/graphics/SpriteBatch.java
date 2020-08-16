package org.javatroid.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.lwjgl.BufferUtils;

public class SpriteBatch {

	private static final int POSITION_SIZE = 2;
	private static final int TEXCOORD_SIZE = 2;
	private static final int COLOR_SIZE = 4;
	
	ShaderProgram defaultShader;
	ShaderProgram shader;
	
	OrthogonalCamera camera;
	OrthogonalCamera blankCamera;
	
	FloatBuffer positionData;
	FloatBuffer texCoordData;
	FloatBuffer colorData;
	
	Vector2f[] vertices;
	Vector2f[] texCoords;
	Vector4f[] colors;
	
	Texture currentTexture = null;
	Vector4f[] currentColor = {new Vector4f(1, 1, 1, 1), new Vector4f(1, 1, 1, 1), new Vector4f(1, 1, 1, 1), new Vector4f(1, 1, 1, 1)};
	
	int index;
	
	int maxDraws;
	int draws;
	boolean isDrawing;
	
	int batchCount = 0;
	
	public SpriteBatch(int width, int height){
		this(width, height, 10000);
	}
	
	public SpriteBatch(int width, int height, int maxDraws){
		
		defaultShader = new ShaderProgram();
		defaultShader.addVertexShader("shaders/defaultShader.vert");
		defaultShader.addFragmentShader("shaders/defaultShader.frag");
		defaultShader.compile();
		
		shader = defaultShader;
		
		camera = new OrthogonalCamera(width, height);
		blankCamera = new OrthogonalCamera(width, height);
		
		positionData = BufferUtils.createFloatBuffer(maxDraws * POSITION_SIZE * 4);
		texCoordData = BufferUtils.createFloatBuffer(maxDraws * TEXCOORD_SIZE * 4);
		colorData = BufferUtils.createFloatBuffer(maxDraws * COLOR_SIZE * 4);
		
		vertices = new Vector2f[maxDraws * 4];
		texCoords = new Vector2f[maxDraws * 4];
		colors = new Vector4f[maxDraws * 4];
		
		for(int i = 0; i < texCoords.length; i++){
			texCoords[i] = new Vector2f(1, 1);
		}
		
		for(int i = 0; i < colors.length; i++){
			colors[i] = new Vector4f(1, 1, 1, 1);
		}
		
		index = 0;
		
		this.maxDraws = maxDraws;
		draws = 0;
		isDrawing = false;
	}
	
	public void startFrame(){
		batchCount = 0;
	}
	
	public void endFrame(){
		
	}
	
	public void begin(){ begin(BlendState.ALPHA); }
	
	public void begin(BlendState blendState){
		glEnable(GL_BLEND);
		glBlendFunc(blendState.sFactor, blendState.dFactor);
		
		if(isDrawing)
			throw new IllegalStateException("SpriteBatch is already started drawing!");
		
		isDrawing = true;
	}
	
	public void draw(float posX, float posY, float width, float height, float rotation, Texture texture){ draw(posX, posY, width, height, rotation, new TextureRegion(texture)); }
	
	public void draw(float posX, float posY, float width, float height, float rotation, TextureRegion region){
		
		Texture texture = region.getTexture();
		
		if(texture != currentTexture){ 
			flush();
			currentTexture = texture;
		}
		
		if(draws >= maxDraws) flush();
		
		Vector2f[] coords = region.getTexCoords();
		
		vertices[index + 0] = new Vector2f(-width / 2, -height / 2).rotate(rotation).add(new Vector2f(posX, posY));
		vertices[index + 1] = new Vector2f(-width / 2, height / 2).rotate(rotation).add(new Vector2f(posX, posY));
		vertices[index + 2] = new Vector2f(width / 2, height / 2).rotate(rotation).add(new Vector2f(posX, posY));
		vertices[index + 3] = new Vector2f(width / 2, -height / 2).rotate(rotation).add(new Vector2f(posX, posY));
		
		texCoords[index + 0].set(coords[0]);
		texCoords[index + 1].set(coords[1]);
		texCoords[index + 2].set(coords[2]);
		texCoords[index + 3].set(coords[3]);
		
		colors[index + 0].set(currentColor[0]);
		colors[index + 1].set(currentColor[1]);
		colors[index + 2].set(currentColor[2]);
		colors[index + 3].set(currentColor[3]);
		
		index += 4;
		draws += 4;
	}
	
	private void flush(){
		
		batchCount++;
		
		if(currentTexture == null)
			return;
		
		for(int i = 0; i < index; i++){
			Vector2f vertex = vertices[i];
			Vector2f texCoord = texCoords[i];
			Vector4f color = colors[i];
			vertex.store(positionData);
			texCoord.store(texCoordData);
			color.store(colorData);
		}
		
		positionData.flip();
		texCoordData.flip();
		colorData.flip();
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, 0, positionData);
		glVertexAttribPointer(1, TEXCOORD_SIZE, GL_FLOAT, false, 0, texCoordData);
		glVertexAttribPointer(2, COLOR_SIZE, GL_FLOAT, false, 0, colorData);
		
		shader.bind();
		shader.setUniformMat("projection", camera.getCombined());
		shader.setUniformTexture("texture0", currentTexture, 0);
		
		glDrawArrays(GL_QUADS, 0, draws);
		
		shader.release();
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		positionData.clear();
		texCoordData.clear();
		colorData.clear();
		
		index = 0;
		draws = 0;
	}
	
	public void end(){
		if(!isDrawing)
			throw new IllegalStateException("SpriteBatch has NOT started drawing yet. Call begin() first!");
		
		flush();
		
		glBlendFunc(BlendState.ALPHA.sFactor, BlendState.ALPHA.dFactor);
		
		isDrawing = false;
	}
	
	public OrthogonalCamera getCamera(){
		return camera;
	}
	
	public int getBatchCount(){
		return batchCount;
	}
	
	public void setCamera(OrthogonalCamera camera){
		if(camera == null)
			this.camera = blankCamera;
		else 
			this.camera = camera;
	}
	
	public void setColor(float r, float g, float b, float a){
		setColor(new Vector4f(r, g, b, a));
	}
	
	public void setColor(Vector4f color){
		setColor(new Vector4f[]{color.clone(), color.clone(), color.clone(), color.clone()});
	}
	
	public void setColor(Vector4f[] color){
		currentColor = color;
	}
	
	public void setShader(ShaderProgram shader){
		if(shader == null)
			this.shader = defaultShader;
		else
			this.shader = shader;
	}
	
}
