package com.vecdef.model;

import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

public class PlayerModel extends Model {

	final float BODY_WIDTH = 28;
	final float BODY_HEIGHT = 11;
	final float BODY_OFFSET_X = 2;
	
	final float VISOR_PADDING = 10;
	
	final float WING_OFFSET_Y = 3;
	
	Vector4f BODY_COLOR = new Vector4f(0.8f, 0.8f, 0.8f, 1);
	Vector4f VISOR_COLOR = new Vector4f(0.6f, 0.6f, 0.6f, 1);
	Vector4f WING_COLOR = new Vector4f(0.8f, 0.8f, 0.8f, 1);
	Vector4f DETAIL_COLOR = new Vector4f(0.25f, 0.25f, 0.25f, 1);
	
	private PlayerModel(){
		add(new Vector2f(-BODY_WIDTH / 4f + BODY_OFFSET_X, 0), BODY_COLOR);
		add(new Vector2f(BODY_OFFSET_X, BODY_HEIGHT / 2f), BODY_COLOR);
		
		add(new Vector2f(BODY_OFFSET_X, BODY_HEIGHT / 2f), BODY_COLOR);
		add(new Vector2f(BODY_WIDTH / 2F + BODY_OFFSET_X, 0), BODY_COLOR);
		
		add(new Vector2f(BODY_WIDTH / 2F + BODY_OFFSET_X, 0), BODY_COLOR);
		add(new Vector2f(BODY_OFFSET_X, -BODY_HEIGHT / 2f), BODY_COLOR);
		
		add(new Vector2f(BODY_OFFSET_X, -BODY_HEIGHT / 2f), BODY_COLOR);
		add(new Vector2f(-BODY_WIDTH / 4f + BODY_OFFSET_X, 0), BODY_COLOR);
		
		float w = BODY_WIDTH;
		float h = BODY_HEIGHT; 
		
		//Visor
		add(new Vector2f(w / 2 - VISOR_PADDING, 0), VISOR_COLOR);
		add(new Vector2f(w / 4 - VISOR_PADDING, -h / 8), VISOR_COLOR);
		
		add(new Vector2f(w / 4 - VISOR_PADDING, -h / 8), VISOR_COLOR);
		add(new Vector2f(w / 8 - VISOR_PADDING, 0), VISOR_COLOR);
		
		add(new Vector2f(w / 8 - VISOR_PADDING, 0), VISOR_COLOR);
		add(new Vector2f(w / 4 - VISOR_PADDING, h / 8), VISOR_COLOR);
		
		add(new Vector2f(w / 4 - VISOR_PADDING, h / 8), VISOR_COLOR);
		add(new Vector2f(w / 2 - VISOR_PADDING, 0), VISOR_COLOR);
		
		//Bottom Wing
		add(new Vector2f(0, -h / 2 -WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(w / 12, -h / 2 - h / 3 -WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(w / 12, -h / 2 - h / 3 -WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(w / 12 + w / 4, -h - h / 6 -WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(w / 12 + w / 4, -h - h / 6 -WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(-w / 12, -h - h / 6 -WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(-w / 12, -h - h / 6 -WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(-w / 2, 0 -WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(-w / 2, 0 -WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(0, -h / 2 -WING_OFFSET_Y), WING_COLOR);
		
		//Top Wing
		add(new Vector2f(0, h / 2 + WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(w / 12, h / 2 + h / 3 + WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(w / 12, h / 2 + h / 3 + WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(w / 12 + w / 4, h + h / 6 + WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(w / 12 + w / 4, h + h / 6 + WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(-w / 12, h + h / 6 + WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(-w / 12, h + h / 6 + WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(-w / 2, 0 + WING_OFFSET_Y), WING_COLOR);
		
		add(new Vector2f(-w / 2, 0 + WING_OFFSET_Y), WING_COLOR);
		add(new Vector2f(0, h / 2 + WING_OFFSET_Y), WING_COLOR);
		
		
//		add(new Vector2f(-BODY_WIDTH / 2f + 1, -4), WING_COLOR);
//		add(new Vector2f(2, -BODY_HEIGHT / 2f - 4), WING_COLOR);
//		
////		add(new Vector2f(-BODY_WIDTH / 2f + 2, -8), WING_COLOR);
////		add(new Vector2f(3, -BODY_HEIGHT / 2f - 8), WING_COLOR);
//		
//		add(new Vector2f(-BODY_WIDTH / 2f + 3, -12), WING_COLOR);
//		add(new Vector2f(4, -BODY_HEIGHT / 2f - 12), WING_COLOR);
//		
//		add(new Vector2f(-BODY_WIDTH / 2f + 1, 4), WING_COLOR);
//		add(new Vector2f(2, BODY_HEIGHT / 2f + 4), WING_COLOR);
//		
////		add(new Vector2f(-BODY_WIDTH / 2f + 2, 8), WING_COLOR);
////		add(new Vector2f(3, BODY_HEIGHT / 2f + 8), WING_COLOR);
//		
//		add(new Vector2f(-BODY_WIDTH / 2f + 3, 12), WING_COLOR);
//		add(new Vector2f(4, BODY_HEIGHT / 2f + 12), WING_COLOR);
	}
	
	static PlayerModel instance = null;
	
	public static PlayerModel get(){
		if(instance == null)
			instance = new PlayerModel();
		return instance;
	}
	
}
