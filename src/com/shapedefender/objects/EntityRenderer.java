package com.shapedefender.objects;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.shapedefender.ShapeRenderer;
import com.shapedefender.model.Mesh;
import com.shapedefender.model.MeshLayer;
import com.shapedefender.model.Primitive;
import com.shapedefender.model.Primitive.DrawType;
import com.shapedefender.model.Transform2D;

public class EntityRenderer {

	private static final int MAX_DRAWABLE_ENTITIES = 1000;
	private ArrayList<IRenderable> entitiesToDraw;
	
	private ArrayList<RenderData> lines = new ArrayList<RenderData>();
	private ArrayList<RenderData> triangles = new ArrayList<RenderData>();
	
	public EntityRenderer(){
		entitiesToDraw = new ArrayList<IRenderable>(MAX_DRAWABLE_ENTITIES);
	}
	
	public void draw(IRenderable renderable){
		entitiesToDraw.add(renderable);
	}
	
	public void render(ShapeRenderer renderer){
		for(int i  = 0; i < entitiesToDraw.size(); i++){
			IRenderable renderable = entitiesToDraw.get(i);
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
		entitiesToDraw.clear();
		
		renderList(DrawType.LINES, BlendState.ADDITIVE, lines, renderer);
		renderList(DrawType.TRIANGLES, BlendState.ALPHA, triangles, renderer);
		
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
	
	public void clear(){
		entitiesToDraw.clear();
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
