package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.FrameBuffer;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.ShaderProgram;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.lwjgl.opengl.Display;

import com.vecdef.gamestate.Renderer;
import com.vecdef.gamestate.ShapeRenderer;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;
import com.vecdef.model.Primitive;
import com.vecdef.model.Primitive.DrawType;
import com.vecdef.model.Transform2D;

public class RenderSystem {

	private ArrayList<IRenderable> renderables;
	private ArrayList<RenderData> lines;
	private ArrayList<RenderData> triangles;
	
	final Vector2f BLUR_DIR_V = new Vector2f(0, 1);
	final Vector2f BLUR_DIR_H = new Vector2f(1, 0);
	
	OrthogonalCamera camera;
	int screenWidth;
	int screenHeight;
	FrameBuffer diffuseBuffer;
	
	FrameBuffer blurBuffer;
	
	ShaderProgram blur;
	
	public RenderSystem(){
		renderables = new ArrayList<IRenderable>();
		lines = new ArrayList<RenderData>();
		triangles = new ArrayList<RenderData>();
		
		screenWidth = Display.getWidth();
		screenHeight = Display.getHeight();
		camera = new OrthogonalCamera(screenWidth, screenHeight);
		
		diffuseBuffer = new FrameBuffer(screenWidth, screenHeight);
		
		blurBuffer = new FrameBuffer(screenWidth, screenHeight);
		
		blur = new ShaderProgram();
		blur.addVertexShader("shaders/blur.vs");
		blur.addFragmentShader("shaders/blur.fs");
		blur.compile();
		
	}
	
	public void add(IRenderable renderable){
		renderables.add(renderable);
	}
	
	public void remove(IRenderable renderable){
		renderables.remove(renderable);
	}
	
	public void draw(Renderer renderer){
		
		
		//renderer.setCamera(camera);
		ShapeRenderer sRenderer = renderer.ShapeRenderer();
		
		for(int i  = 0; i < renderables.size(); i++){
			IRenderable renderable = renderables.get(i);
			
			if(!renderable.isDrawn()){
				continue;
			}
			
			Mesh mesh = renderable.getMesh();
			Transform2D transform = renderable.getTransform();
			float opacity = renderable.getOpacity();
			
			ArrayList<MeshLayer> layers = mesh.getLayers();
			for(int j = 0; j < layers.size(); j++){
				MeshLayer layer = layers.get(j);
				ArrayList<Primitive> primitives = layer.getPrimitives();
				for(int k = 0; k < primitives.size(); k++){
					Primitive p = primitives.get(k);
					if(p.getDrawType() == DrawType.LINES){
						lines.add(new RenderData(p, transform, opacity));
					} else if(p.getDrawType() == DrawType.TRIANGLES){
						triangles.add(new RenderData(p, transform, opacity));
					}
				}
			}
		}
		
		//diffuseBuffer.bind();
		//diffuseBuffer.clear(0, 0, 0, 1);
			renderList(DrawType.LINES, BlendState.ADDITIVE, lines, sRenderer);
			renderList(DrawType.TRIANGLES, BlendState.ALPHA, triangles, sRenderer);
		//diffuseBuffer.release();
		
//		blur.bind();
//			blur.setUniformf("texelSize", (float)1.0f / 900f);
//			blur.setUniformf("blurDir", BLUR_DIR_V);
//		blur.release();
//		
//		batch.setCamera(camera);
//		batch.setShader(null);
//		blurBuffer.bind();
//		blurBuffer.clear(0, 0, 0, 1);
//			batch.begin();
//				batch.draw(0, 0, screenWidth, screenHeight, 0, diffuseBuffer.getTexture());
//			batch.end();
//		blurBuffer.release();
//		
//		batch.setShader(null);
//		batch.begin();
//			batch.draw(0, 0, screenWidth, screenHeight, 0, blurBuffer.getTexture());
//		batch.end();
//		
		lines.clear();
		triangles.clear();
	}
	
	private void renderList(DrawType drawType, BlendState blendState, ArrayList<RenderData> dataList, ShapeRenderer renderer){
		renderer.begin(drawType, blendState);
		for(int i = 0; i < dataList.size(); i++){
			RenderData data = dataList.get(i);
			
			Transform2D transform = data.transform;
			float opacity = data.opacity;
			
			ArrayList<Vector2f> positions = data.primitive.getVertices();
			ArrayList<Vector4f> colors	 = data.primitive.getColors();
			
			for(int j = 0; j < positions.size(); j++){
				Vector2f position = new Vector2f(positions.get(j));
				position = position.mul(transform.getScale()).rotate(transform.getOrientation()).add(transform.getTranslation());
				
				Vector4f color = new Vector4f(colors.get(j));
				color.w *= opacity;
				renderer.draw(position, color);
			}
		}
		renderer.end();
	}
	
	public int numObjects(){
		return renderables.size();
	}
	
}

class RenderData{
	
	public Primitive primitive;
	public Transform2D transform;
	public float opacity;
	
	public RenderData(Primitive primitive, Transform2D transform, float opacity){
		this.primitive = primitive;
		this.transform = transform;
		this.opacity = opacity;
	}
}
