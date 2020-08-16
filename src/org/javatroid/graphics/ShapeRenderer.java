package org.javatroid.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;
import org.lwjgl.BufferUtils;

public class ShapeRenderer {

	private static final int POSITION_SIZE = 2;
	private static final int TEXCOORD_SIZE = 2;
	private static final int COLOR_SIZE = 4;
	
	private OrthogonalCamera camera;
	private OrthogonalCamera blankCamera;
	
	private FloatBuffer projection;
	private FloatBuffer positions;
	private FloatBuffer texCoords;
	private FloatBuffer colors;
	
	private DrawMode drawMode;
	private int maxDraws;
	private int draws;
	private boolean isDrawing;
	
	public enum DrawMode{
		POINT(GL_POINTS), LINE(GL_LINES), LINE_LOOP(GL_LINE_LOOP), TRIANGLE(GL_TRIANGLES), TRIANGLE_FAN(GL_TRIANGLE_FAN), TRIANGLE_STRIP(GL_TRIANGLE_STRIP), QUAD(GL_QUADS), POLYGON(GL_POLYGON);
		
		int glMode;
		private DrawMode(int glMode){
			this.glMode = glMode;
		}	
	}
	
	public ShapeRenderer(int width, int height){
		this(width, height, 1000);
	}
	
	public ShapeRenderer(int width, int height, int maxDraws){
		projection = BufferUtils.createFloatBuffer(16);
		positions = BufferUtils.createFloatBuffer(maxDraws * POSITION_SIZE);
		texCoords = BufferUtils.createFloatBuffer(maxDraws * TEXCOORD_SIZE);
		colors = BufferUtils.createFloatBuffer(maxDraws * COLOR_SIZE);
		draws = 0;
		isDrawing = false;
		
		camera = new OrthogonalCamera(width, height);
		blankCamera = new OrthogonalCamera(width, height);
		
		this.maxDraws = maxDraws;
		this.draws = 0;
		
		glEnable(GL_BLEND);
	}
	
	public void setCamera(OrthogonalCamera camera){
		if(camera == null)
			this.camera = blankCamera;
		else
			this.camera = camera;
		
		projection.clear();
		this.camera.getCombined().store(projection);
		projection.flip();
		
		glMatrixMode(GL_PROJECTION);
		glLoadMatrixf(projection);
		glMatrixMode(GL_MODELVIEW);
		
	}
	
	public OrthogonalCamera getCamera(){
		return camera;
	}
	
	public void begin(DrawMode mode){
		begin(mode, BlendState.ALPHA);
	}
	
	public void begin(DrawMode mode, BlendState blendState){
		
		glBlendFunc(blendState.sFactor, blendState.dFactor);
		
		drawMode = mode;
		isDrawing = true;
	}
	
	public void draw(Vector2f vertex){
		draw(vertex, new Vector2f(0, 0), new Vector4f(1, 1, 1, 1));
	}
	
	public void draw(Vector2f vertex, Vector4f color){
		draw(vertex, new Vector2f(0, 0), color);
	}
	
	public void draw(Vector2f vertex, Vector2f texCoord){
		draw(vertex, texCoord, new Vector4f(1, 1, 1, 1));
	}
	
	public void draw(Vector2f vertex, Vector2f texCoord, Vector4f color){
		if(draws == maxDraws)
			flush();
		
		vertex.store(positions);
		texCoord.store(texCoords);
		color.store(colors);
		draws++;
	}
	
	private void flush(){
		positions.flip();
		texCoords.flip();
		colors.flip();
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		
		glVertexPointer(POSITION_SIZE, GL_FLOAT, 0, positions);
		glTexCoordPointer(TEXCOORD_SIZE, GL_FLOAT, 0, texCoords);
		glColorPointer(COLOR_SIZE, GL_FLOAT, 0, colors);
		
		glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, 0, positions);
		glVertexAttribPointer(1, TEXCOORD_SIZE, GL_FLOAT, false, 0, texCoords);
		glVertexAttribPointer(2, COLOR_SIZE, GL_FLOAT, false, 0, colors);
		
		glDrawArrays(drawMode.glMode, 0, draws);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		positions.clear();
		texCoords.clear();
		colors.clear();
		
		draws = 0;
	}
	
	public void end(){
		if(!isDrawing)
			System.out.println("ShapeRenderer begin() MUST be called before drawing is complete!");
		
		flush();
		
		glBlendFunc(BlendState.ALPHA.sFactor, BlendState.ALPHA.dFactor);
		
		isDrawing = false;
	}
	
}