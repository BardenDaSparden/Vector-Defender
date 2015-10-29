package com.vecdef.gamestate;

import static org.lwjgl.opengl.GL11.glViewport;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.FrameBuffer;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.ShaderProgram;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.math.Vector2f;
import org.lwjgl.opengl.Display;

import com.vecdef.objects.Grid;
import com.vecdef.objects.RenderSystem;

import ddf.minim.Minim;

public class SceneRenderer {

	Scene scene;
	Renderer renderer;
	
	OrthogonalCamera camera;
	FrameBuffer back;
	
	final Vector2f HORIZONTAL = new Vector2f(1, 0);
	final Vector2f VERTICAL = new Vector2f(0, 1);
	final float BLUR_SIZE = 1f;
	FrameBuffer[] blurFBO;
	FrameBuffer bloom;
	ShaderProgram blur;
	
	public SceneRenderer(Scene scene, Renderer renderer, Minim minim){
		this.scene = scene;
		this.renderer = renderer;
		
		camera = new OrthogonalCamera();
		
		int w = Display.getWidth();
		int h = Display.getHeight();
		
		back = new FrameBuffer(w, h);
		bloom = new FrameBuffer(w, h);
		
		blurFBO = new FrameBuffer[8];
		for(int i = 0, j = 0; i < blurFBO.length; i+=2, j++){
			int sf = ((j + 1) * 2);
			blurFBO[i] = new FrameBuffer(w / sf, h / sf);
			blurFBO[i + 1] = new FrameBuffer(w / sf, h / sf);
			blurFBO[i].getTexture().setTextureFilter(Texture.LINEAR);
			blurFBO[i + 1].getTexture().setTextureFilter(Texture.LINEAR);
		}
		
		blur = new ShaderProgram();
		blur.addVertexShader("shaders/blur.vs");
		blur.addFragmentShader("shaders/blur.fs");
		blur.compile();
	}
	
	public void draw(){
		ShapeRenderer sRenderer = renderer.ShapeRenderer();
		SpriteBatch batch = renderer.SpriteBatch();
		batch.setColor(1, 1, 1, 1);
		
		Grid grid = scene.getGrid();
		RenderSystem renders = scene.renders;
		
		back.bind();
			back.clear(0, 0, 0, 1);
			grid.draw(sRenderer);
			renders.draw(renderer);
		back.release();
		
		drawFullscreenFBO(back, null);
		
		blurFBO[0].bind();
			blurFBO[0].clear(0, 0, 0, 1);
			setBlurSettings(blurFBO[0].getHeight(), VERTICAL);
			drawFBO(back, blur);
		blurFBO[0].release();
		
		blurFBO[1].bind();
			blurFBO[1].clear(0, 0, 0, 1);
			setBlurSettings(blurFBO[1].getWidth(), HORIZONTAL);
			drawFBO(blurFBO[0], blur);
		blurFBO[1].release();
		
		for(int i = 2; i < blurFBO.length; i+=2){
			blurFBO[i].bind();
				blurFBO[i].clear(0, 0, 0, 1);
				setBlurSettings(blurFBO[i].getHeight(), VERTICAL);
				drawFBO(blurFBO[i - 1], blur);
			blurFBO[i].release();
			
			blurFBO[i + 1].bind();
				blurFBO[i + 1].clear(0, 0, 0, 1);
				setBlurSettings(blurFBO[i + 1].getWidth(), HORIZONTAL);
				drawFBO(blurFBO[i], blur);
			blurFBO[i + 1].release();
		}
		
		
		
		bloom.bind();
			bloom.clear(0, 0, 0, 1);
			drawFullscreenFBO(blurFBO[1], null);
			drawFullscreenFBO(blurFBO[3], null);
			drawFullscreenFBO(blurFBO[5], null);
			drawFullscreenFBO(blurFBO[7], null);
		bloom.release();
		
		drawFullscreenFBO(bloom, null);
	}
	
	void setBlurSettings(float resolution, Vector2f blurDir){
		blur.bind();{
			blur.setUniformf("texelSize", BLUR_SIZE / resolution);
			blur.setUniformf("blurDir", blurDir);
		}
		blur.release();
	}
	
	void drawFBO(FrameBuffer buffer, ShaderProgram shader){
		SpriteBatch batch = renderer.SpriteBatch();
		camera.setSize(buffer.getWidth(), buffer.getHeight());
		batch.setShader(shader);
		batch.setCamera(camera);
		batch.begin(BlendState.ADDITIVE);
			batch.draw(0, 0, buffer.getWidth(), buffer.getHeight(), 0, buffer.getTexture());
		batch.end();
	}
	
	void drawFullscreenFBO(FrameBuffer buffer, ShaderProgram shader){
		SpriteBatch batch = renderer.SpriteBatch();
		glViewport(0, 0 , Display.getWidth(), Display.getHeight());
		camera.setSize(buffer.getWidth(), buffer.getHeight());
		batch.setShader(shader);
		batch.setCamera(camera);
		batch.begin(BlendState.ADDITIVE);
			batch.draw(0, 0, buffer.getWidth(), buffer.getHeight(), 0, buffer.getTexture());
		batch.end();
	}
	
	public void destroy(){
		
	}
	
}