package com.vecdef.rendering;

import java.util.ArrayList;

import org.javatroid.graphics.BlendState;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.model.Model;
import com.vecdef.model.Transform;
import com.vecdef.rendering.ShapeRenderer.DrawType;

public class EntityRenderer {

	ShapeRenderer renderer;
	ArrayList<IRenderable> renderables;
	
	final int BUFFER_SIZE = 40000;
	ArrayList<Vector2f> positionBuffer;
	ArrayList<Vector4f> colorBuffer;
	int bufferIdx = 0;
	
	public EntityRenderer(ShapeRenderer renderer){
		this.renderer = renderer;
		renderables = new ArrayList<IRenderable>();
		positionBuffer = new ArrayList<Vector2f>(BUFFER_SIZE);
		colorBuffer = new ArrayList<Vector4f>(BUFFER_SIZE);
		for(int i = 0; i < BUFFER_SIZE; i++){
			positionBuffer.add(new Vector2f(0, 0));
			colorBuffer.add(new Vector4f(0, 0, 0, 1));
		}
	}
	
	public void render(){
		
		bufferIdx = 0;
		
		renderer.begin(DrawType.LINES, BlendState.ALPHA);
		{
			for(int i = 0; i < renderables.size(); i++){
				IRenderable renderable = renderables.get(i);
				
				if(!renderable.isVisible())
					continue;
				
				//Retrieve rendering data
				Model model = renderable.getModel();
				Transform transform = renderable.getTransform();
				float opacity = renderable.getOpacity();
				ArrayList<Vector2f> vertices = model.getVertices();
				ArrayList<Vector4f> colors = model.getColors();
				
				//Copy vertices of each instance to buffer, transform position and color data
				for(int j = 0; j < model.numVertices(); j++){
					Vector2f position = positionBuffer.get(bufferIdx + j);
					Vector4f color = colorBuffer.get(bufferIdx + j);
					
					position.set(vertices.get(j));
					color.set(colors.get(j));
					
					position = position.mul(transform.getScale()).rotate(transform.getOrientation()).add(transform.getTranslation());
					color.w *= opacity;
					
					renderer.draw(position, color);
				}
				
				bufferIdx += model.numVertices();
				
			}
		}
		renderer.end();
	}
	
	public void add(IRenderable renderable){
		renderables.add(renderable);
	}
	
	public void remove(IRenderable renderable){
		renderables.remove(renderable);
	}
	
	public void clear(){
		renderables.clear();
	}
	
}
