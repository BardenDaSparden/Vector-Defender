package com.vecdef.rendering;

import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;

import org.barden.util.GPUProfiler;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.FrameBuffer;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.ShaderProgram;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.math.Vector2f;

import com.vecdef.objects.Entity;
import com.vecdef.objects.Scene;
import com.vecdef.util.Masks;

public class SceneRenderer {

	int width;
	int height;
	Scene scene;
	Renderer renderer;
	
	OrthogonalCamera camera;
	
	GridRenderer gridRenderer;
	ArrayList<Entity> tempEntityList;
	EntityRenderer entityRenderer;
	
	
	//Post-processing data for bloom effect
	FrameBuffer back;
	final Vector2f HORIZONTAL = new Vector2f(1, 0);
	final Vector2f VERTICAL = new Vector2f(0, 1);
	final float BLUR_SIZE = 1f;
	FrameBuffer[] blurFBO;
	FrameBuffer bloom;
	FrameBuffer accum;
	FrameBuffer accum2;
	
	ShaderProgram blur;
	ShaderProgram accumulator;
	
	public SceneRenderer(int width, int height, Scene scene, Renderer renderer){
		this.width = width;
		this.height = height;
		this.scene = scene;
		this.renderer = renderer;
		camera = new OrthogonalCamera();
		
		gridRenderer = new GridRenderer(renderer.ShapeRenderer());
		tempEntityList = new ArrayList<Entity>();
		entityRenderer = new EntityRenderer(renderer.ShapeRenderer());
		
		int w = width;
		int h = height;
		
		back = new FrameBuffer(w, h);
		bloom = new FrameBuffer(w, h);
		
		blurFBO = new FrameBuffer[10];
		for(int i = 0, j = 0; i < blurFBO.length; i+=2, j++){
			int sf = ((j + 1) * 2);
			blurFBO[i] = new FrameBuffer(w / sf, h / sf);
			blurFBO[i + 1] = new FrameBuffer(w / sf, h / sf);
			blurFBO[i].getTexture().setTextureFilter(Texture.LINEAR);
			blurFBO[i + 1].getTexture().setTextureFilter(Texture.LINEAR);
		}
		
		accum = new FrameBuffer(w, h);
		accum.bind();
		accum.clear(0, 0, 0, 1);
		accum.release();
		accum2 = new FrameBuffer(w, h);
		accum2.bind();
		accum2.clear(0, 0, 0, 1);
		accum2.release();
		
		blur = new ShaderProgram();
		blur.addVertexShader("shaders/blur.vs");
		blur.addFragmentShader("shaders/blur.fs");
		blur.compile();
		
		accumulator = new ShaderProgram();
		accumulator.addVertexShader("shaders/reverse_accum.vs");
		accumulator.addFragmentShader("shaders/reverse_accum.fs");
		accumulator.compile();
	}
	
	private void drawEntitiesByType(int mask){
		tempEntityList.clear();
		scene.getEntitiesByType(mask, tempEntityList);
		entityRenderer.clear();
		
		for(int i = 0; i < tempEntityList.size(); i++){
			Entity entity = tempEntityList.get(i);
			IRenderable renderable = (IRenderable) entity;
			entityRenderer.add(renderable);
		}
		
		entityRenderer.render();
	}
	
	private void drawEntities(){
		ArrayList<Entity> entities = scene.getAllEntities();
		entityRenderer.clear();
		
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			IRenderable renderable = (IRenderable) entity;
			entityRenderer.add(renderable);
		}
		
		entityRenderer.render();
	}
	
	void Bloom(){
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
			drawFullscreenFBO(blurFBO[9], null);
		bloom.release();
		
		drawFullscreenFBO(bloom, null);
	}
	
	void MotionBlur(){
		accum.bind();
			drawEntitiesByType(Masks.Entities.BLACK_HOLE | Masks.Entities.PARTICLE | Masks.Entities.BULLET | Masks.Entities.OTHER );
		accum.release();
		
		accum2.bind();
			drawFullscreenFBO_ALPHA(accum, accumulator);
		accum2.release();
		
		accum.bind();
			drawFullscreenFBO_ALPHA(accum2, accumulator);
		accum.release();
		
		drawFullscreenFBO(accum, null);
	}
	
	void doPostProcessing(){
		GPUProfiler profiler = GPUProfiler.get();
		profiler.startTask("Bloom");
			Bloom();
		profiler.endTask();
		profiler.startTask("Motion Blur");
			MotionBlur();
		profiler.endTask();
	}
	
	public void draw(){
		GPUProfiler profiler = GPUProfiler.get();
		SpriteBatch batch = renderer.SpriteBatch();
		batch.setColor(1, 1, 1, 1);
		
		profiler.startTask("Draw Scene");
			back.bind();
				back.clear(0, 0, 0, 1);
				profiler.startTask("Draw Grid");
					gridRenderer.render(scene.getGrid());
				profiler.endTask();
				profiler.startTask("Draw Entities");
					drawEntities();
				profiler.endTask();
			back.release();
			drawFullscreenFBO(back, null);
		profiler.endTask();
		
		profiler.startTask("Post Processing");
			doPostProcessing();
		profiler.endTask();
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
		glViewport(0, 0 , width, height);
		camera.setSize(buffer.getWidth(), buffer.getHeight());
		batch.setShader(shader);
		batch.setCamera(camera);
		batch.begin(BlendState.ADDITIVE);
			batch.draw(0, 0, buffer.getWidth(), buffer.getHeight(), 0, buffer.getTexture());
		batch.end();
	}
	
	void drawFullscreenFBO_ALPHA(FrameBuffer buffer, ShaderProgram shader){
		SpriteBatch batch = renderer.SpriteBatch();
		glViewport(0, 0 , width, height);
		camera.setSize(buffer.getWidth(), buffer.getHeight());
		batch.setShader(shader);
		batch.setCamera(camera);
		batch.begin(BlendState.ALPHA);
			batch.draw(0, 0, buffer.getWidth(), buffer.getHeight(), 0, buffer.getTexture());
		batch.end();
	}
	
	public void destroy(){
		
	}
	
}