package com.vecdef.gamestate;

import static org.lwjgl.opengl.GL11.glViewport;

import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.FrameBuffer;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.ShaderProgram;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.lwjgl.opengl.Display;

import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;
import com.vecdef.objects.Entity;
import com.vecdef.objects.Grid;
import com.vecdef.objects.Masks;
import com.vecdef.objects.RenderSystem;

import ddf.minim.AudioListener;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;

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
	
	Minim minim;
	AudioPlayer player;
	FFT fft;
	BeatDetect detector;
	
	final int BUFFER_SIZE = 1024;
	final int SAMPLE_RATE = 44100;
	float[] dataStream = new float[BUFFER_SIZE];
	float[] fftStream = new float[BUFFER_SIZE];
	
	float beatScale = 0;
	float minOpacity = 0.6f;
	float opacity = 1.5f;
	
	Mesh mesh;
	
	public SceneRenderer(Scene scene, Renderer renderer, Minim minim){
		this.scene = scene;
		this.renderer = renderer;
		this.minim = minim;	
		
		player = minim.loadFile("astro.wav");
		player.addListener(new AudioListener() {
			
			@Override
			public void samples(float[] sampL, float[] sampR) {
				dataStream = sampL;
			}
			
			@Override
			public void samples(float[] samp) {
				dataStream = samp;
			}
		});
		
		player.loop();
		fft = new FFT(BUFFER_SIZE, SAMPLE_RATE);
		detector = new BeatDetect(BUFFER_SIZE, SAMPLE_RATE);
		detector.detectMode(BeatDetect.SOUND_ENERGY);
		detector.setSensitivity(16);
		
		float startW = 64;
		float startH = 64;
		
		Vector4f color = new Vector4f(0, 1, 0, 0.5f);
		
		LinePrimitive lines = new LinePrimitive();
		lines.addVertex(new Vector2f(-startW / 2, -startH / 2), color);
		lines.addVertex(new Vector2f(startW / 2, -startH / 2), color);
		
		lines.addVertex(new Vector2f(startW / 2, -startH / 2), color);
		lines.addVertex(new Vector2f(startW / 2, startH / 2), color);
		
		lines.addVertex(new Vector2f(startW / 2, startH / 2), color);
		lines.addVertex(new Vector2f(-startW / 2, startH / 2), color);
		
		lines.addVertex(new Vector2f(-startW / 2, startH / 2), color);
		lines.addVertex(new Vector2f(-startW / 2, -startH / 2), color);
		
		MeshLayer layer0 = new MeshLayer();
		layer0.addPrimitive(lines);
		
		mesh = new Mesh();
		mesh.addLayer(layer0);
		
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
		renders.setRelativeScale(beatScale);
		
		detector.detect(dataStream);
		if(detector.isOnset()){
			opacity = 1.4f;
			beatScale = 0.60f;
		}
		
		opacity *= 0.95f;
		if(opacity < minOpacity)
			opacity = minOpacity;
		
		beatScale *= 0.90f;
		
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
		
		
		
//		bloom.bind();
//			bloom.clear(0, 0, 0, 1);
//			drawFullscreenFBO(blurFBO[1], null);
//			drawFullscreenFBO(blurFBO[3], null);
//			drawFullscreenFBO(blurFBO[5], null);
//			drawFullscreenFBO(blurFBO[7], null);
//		bloom.release();
		
		batch.setColor(1, 1, 1, opacity);
		drawFullscreenFBO(bloom, null);
		batch.setColor(1, 1, 1, 1);
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
	
	void createRectangle(){
		RectangleWave rWave = new RectangleWave(scene, mesh);
		scene.add(rWave);
	}
	
	public void destroy(){
		player.close();
	}
	
}

class RectangleWave extends Entity{

	final int LIFETIME = 140;
	Timer lifeTimer = new Timer(LIFETIME);
	
	public RectangleWave(Scene scene, Mesh mesh) {
		super(scene);
		this.mesh = mesh;
		lifeTimer.setCallback(new TimerCallback() {
			
			@Override
			public void execute(Timer timer) {
				expire();
			}
		});
		
		lifeTimer.start();
		
		
		setOpacity(0);
		setAngularVelocity(FastMath.randomi(-3,  3));
		
		float speed = FastMath.randomi(2, 8);
		float dir = (float) (FastMath.random() * Math.PI * 2);
		
		getVelocity().set(FastMath.cos(dir) * speed, FastMath.sin(dir) * speed);
	}

	@Override
	public int getRadius() {
		return 0;
	}

	@Override
	public int getGroupMask() {
		return Masks.NONE;
	}

	@Override
	public int getCollisionMask() {
		return Masks.NONE;
	}

	@Override
	public void update() {
		lifeTimer.tick();
		
		setOpacity(1 - lifeTimer.percentComplete());
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public int getEntityType() {
		return Masks.Entities.OTHER;
	}
	
}
