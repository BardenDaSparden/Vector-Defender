package com.vecdef.gamestate;

import org.barden.input.Joystick;
import org.barden.input.JoystickListener;
import org.javatroid.audio.AudioPlayer;
import org.javatroid.audio.Sound;
import org.javatroid.core.Resources;
import org.javatroid.core.Timer;
import org.javatroid.core.TimerCallback;
import org.javatroid.graphics.BlendState;
import org.javatroid.graphics.OrthogonalCamera;
import org.javatroid.graphics.SpriteBatch;
import org.javatroid.graphics.Texture;
import org.javatroid.graphics.TextureRegion;
import org.javatroid.math.CubicInterpolator;
import org.javatroid.math.FastMath;
import org.javatroid.math.Interpolator;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector3f;
import org.javatroid.math.Vector4f;
import org.javatroid.text.BitmapFont;

import com.vecdef.rendering.ShapeRenderer;

public class MainMenuState extends GameState {

	public MainMenuState(VectorDefender instance) {
		super(instance);
	}
	
	OrthogonalCamera hudCamera;
	OrthogonalCamera camera;
	Texture title;
	Texture white;
	
	JoystickListener listener;
	JoystickListener p2Listener;
	JoystickListener p3Listener;
	JoystickListener p4Listener;
	
	final float JOYSTICK_THRESHOLD = 0.5f;
	final int JOYSTICK_MOVE_COOLDOWN = 15;
	Timer cooldownTimer;
	boolean bRegisterJoystickMove = true;
	
	Vector3f HSB = new Vector3f(60.0f / 360.0f, 1, 1f);
	Vector3f RGB = new Vector3f(1, 0, 0);
	Vector4f titleColor = new Vector4f(1, 0, 0, 1);
	TextureRegion flippedTitle;
	Vector4f[] reflectionColor = {
			new Vector4f(1, 1, 1, 0.05f),
			new Vector4f(1, 1, 1, 0.25f),
			new Vector4f(1, 1, 1, 0.25f),
			new Vector4f(1, 1, 1, 0.05f)
	};
	
	final int FADE_IN_TIME = 30;
	final int FADE_OUT_TIME = 30;
	
	Timer fadeInTimer;
	Timer fadeOutTimer;
	
	boolean isFadingOut = false;
	
	final Vector4f SELECTED_COLOR = new Vector4f(0.95f, 0.95f, 0.95f, 1.0f);
	final Vector4f NON_SELECTED_COLOR = new Vector4f(0.95f, 0.95f, 0.95f, 0.17f);
	Vector2f MENU_POSITION;
	
	String[] menuItems = {
			"Quick Match",
			"Custom Match",
			"Customization",
			"Statistics",
			"Settings",
			"Quit"
	};
	
	ColorState[] menuItemColors;
	
	int menuItemIdx = 0;
	
	Sound itemSelectSound;
	
	@Override
	public void initialize() {
		hudCamera = new OrthogonalCamera(instance.settings.width, instance.settings.height);
		camera = new OrthogonalCamera(instance.settings.width, instance.settings.height);
		
		title = Resources.getTexture("MainMenuTitle");
		white = Resources.getTexture("blank");
		
		listener = new JoystickListener() {
			@Override
			public void onButtonRelease(int button) {
				
			}
			
			@Override
			public void onButtonPress(int button) {
				if(button == Joystick.BUTTON_A || button == Joystick.BUTTON_START){
					onMenuConfirm();
				}
				
				if(button == Joystick.BUTTON_DPAD_UP){
					onMenuPrevious();
				}
				
				if(button == Joystick.BUTTON_DPAD_DOWN){
					onMenuNext();
				}
			}
		};
		
		p2Listener = new JoystickListener() {
			
			@Override
			public void onButtonRelease(int button) {
				if(button == Joystick.BUTTON_START){
					
				}
			}
			
			@Override
			public void onButtonPress(int button) {
				// TODO Auto-generated method stub
				
			}
		};
		
		instance.joystick1.addListener(listener);
		cooldownTimer = new Timer(JOYSTICK_MOVE_COOLDOWN);
		cooldownTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				bRegisterJoystickMove = true;
			}
		});
		cooldownTimer.start();
		
		fadeInTimer = new Timer(FADE_IN_TIME);
		fadeOutTimer = new Timer(FADE_OUT_TIME);
		
		fadeInTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				
			}
		});
		
		fadeOutTimer.setCallback(new TimerCallback() {
			@Override
			public void execute(Timer timer) {
				instance.setGameState(GameStates.PLAYING);
			}
		});
		
		fadeInTimer.reset();
		fadeOutTimer.reset();
		isFadingOut = false;
		
		fadeInTimer.start();
		
		MENU_POSITION = new Vector2f();
		MENU_POSITION.set(-1, 40);
		flippedTitle = new TextureRegion(title);
		flippedTitle.flipY();
		
		itemSelectSound = Resources.getSound("onSelect");
		
		menuItemColors = new ColorState[menuItems.length];
		for(int i = 0; i < menuItemColors.length; i++){
			menuItemColors[i] = new ColorState(NON_SELECTED_COLOR);
		}
		menuItemColors[menuItemIdx].transitionTo(SELECTED_COLOR);
	}

	void onMenuItemChange(int menuItemIdx){
		cooldownTimer.restart();
		
		for(int i = 0; i < menuItems.length; i++){
			ColorState colorState = menuItemColors[i];
			if(i == menuItemIdx)
				colorState.transitionTo(SELECTED_COLOR);
			else 
				colorState.transitionTo(NON_SELECTED_COLOR);
		}
		
		AudioPlayer.instance().play(itemSelectSound);
	}
	
	void onMenuPrevious(){
		if(menuItemIdx == 0)
			menuItemIdx = menuItems.length - 1;
		else
			menuItemIdx--;
		onMenuItemChange(menuItemIdx);
	}
	
	void onMenuNext(){
		if(menuItemIdx + 1 == menuItems.length)
			menuItemIdx = 0;
		else 
			menuItemIdx++;
		onMenuItemChange(menuItemIdx);
	}
	
	void onMenuConfirm(){
		String selected = menuItems[menuItemIdx];
		if(selected.equals("Quick Match")){
			isFadingOut = true;
			fadeOutTimer.start();
		} else if(selected.equals("Custom Match")){
			//TODO push settings menu
		} else if(selected.equals("Settings")){
			//TODO push settings menu
		} else if(selected.equals("Quit")){
			instance.quit();
		}
	}
	
	@Override
	public void update() {
		fadeInTimer.tick();
		fadeOutTimer.tick();
		cooldownTimer.tick();
		
		HSB.x += 1.0f/720.0f;
		java.awt.Color c = java.awt.Color.getHSBColor(HSB.x, HSB.y, HSB.z);
		
		RGB.x = c.getRed() / 255.0f;
		RGB.y = c.getGreen() / 255.0f;
		RGB.z = c.getBlue() / 255.0f;
		
		titleColor.set(RGB.x, RGB.y, RGB.z, 1);
		
		float lay = instance.input.getJoystick(0).getLeftY();
		if(Math.abs(lay) > JOYSTICK_THRESHOLD && bRegisterJoystickMove){
			if(lay > 0)
				onMenuPrevious();
			else
				onMenuNext();
			
			bRegisterJoystickMove = false;
		}
		
		reflectionColor[0].set(titleColor.x, titleColor.y, titleColor.z, reflectionColor[0].w);
		reflectionColor[1].set(titleColor.x, titleColor.y, titleColor.z, reflectionColor[1].w);
		reflectionColor[2].set(titleColor.x, titleColor.y, titleColor.z, reflectionColor[2].w);
		reflectionColor[3].set(titleColor.x, titleColor.y, titleColor.z, reflectionColor[3].w);
		
		for(int i = 0; i < menuItemColors.length; i++){
			ColorState colorState = menuItemColors[i];
			colorState.update();
		}
	}

	@Override
	public void render() {
		instance.renderer.setCamera(camera);
		
		ShapeRenderer shapeRenderer = instance.shapeRenderer;
		SpriteBatch batch = instance.batch;
		BitmapFont font = Resources.getFont("square36");
		//BitmapFont fontSmall = Resources.getFont("square16");
		batch.setColor(1, 1, 1, 1);
		
		shapeRenderer.setCamera(hudCamera);

		
		batch.setColor(titleColor);
		batch.begin(BlendState.ALPHA);
			batch.draw(0, instance.settings.height / 2 - title.getHeight() / 2 - 100, title.getWidth(), title.getHeight(), 0, title);
			batch.setColor(reflectionColor);
			batch.draw(0, instance.settings.height / 2 - title.getHeight() / 2 - 100 - title.getHeight(), title.getWidth(), title.getHeight(), 0, flippedTitle);
		batch.end();
		
		batch.setCamera(hudCamera);
		batch.begin(BlendState.ALPHA);
			final float MENU_ITEM_SPACE_Y = 40;
			for(int i = 0; i < menuItems.length; i++){
				String item = menuItems[i];
				ColorState colorState = menuItemColors[i];
				batch.setColor(colorState.get());
				font.drawStringCentered(MENU_POSITION.x, MENU_POSITION.y - (i * MENU_ITEM_SPACE_Y), item, batch);
				batch.setColor(1, 1, 1, 1);
			}
		batch.end();
		
//		float x = -instance.settings.width / 2.0f;
//		float y = -instance.settings.height / 2.0f + 50;
//		float x_i = instance.settings.width / 4;
//		
//		float s = FastMath.abs(FastMath.sin(HSB.x * 16.0f));
//		batch.setColor(1, 1, 1, s);
//		batch.begin(BlendState.ALPHA);
//			for(int i = 0; i < 4; i++){
//				Joystick j = instance.input.getJoystick(i);
//				String str = "Press Start to join...";
//				float offsetX = (x_i - fontSmall.getWidth(str)) / 2.0f;
//				if((j.isConnected() || i == 3) && (i != 0)){
//					fontSmall.drawString(x + (x_i * i) + (offsetX), y, str, batch);
//				}
//			}
//		batch.end();
//		
//		String str = "BardenDaSparden";
//		float offsetX = (x_i - fontSmall.getWidth(str)) / 2.0f;
//		batch.setColor(1, 1, 1, 1);
//		batch.begin(BlendState.ALPHA);
//			fontSmall.drawString(-instance.settings.width / 2.0f + offsetX, -instance.settings.height / 2.0f + 50, str, batch);
//		batch.end();
		
//		batch.setColor(0, 1, 1, 1);
//		batch.begin(BlendState.ALPHA);
//			batch.draw(0, 0, 2, instance.settings.height, 0, white);
//			batch.draw(0, 0, instance.settings.width, 2, 0, white);
//		batch.end();
		
		float opacity = (!isFadingOut) ? 1.0f - fadeInTimer.percentComplete() : fadeOutTimer.percentComplete();
	    batch.setColor(0, 0, 0, opacity);
	    batch.begin(BlendState.ALPHA);
	    	batch.draw(0, 0, instance.settings.width, instance.settings.height, 0, white);
	    batch.end();
	    batch.setCamera(camera);
	}

	@Override
	public void cleanUp() {
		instance.joystick1.removeListener(listener);
		menuItemIdx = 0;
	}
}

class ColorState {
	
	static final Interpolator INTERPOLATOR = new CubicInterpolator(new Vector2f(0.1f, 0.75f), new Vector2f(0.75f, 1.0f));
	static final int TRANSITION_TIME = 30;
	
	int time = 0;
	Vector4f color;
	Vector4f transitionColor;
	boolean transitionComplete = true;
	
	public ColorState(Vector4f initialColor){
		color = new Vector4f();
		transitionColor = new Vector4f();
		color.set(initialColor);
		transitionColor.set(initialColor);
	}
	
	public void update(){
		if(!transitionComplete && (time != TRANSITION_TIME)){
			time++;
			float w = (float)(time) / (float)(TRANSITION_TIME);
			color.x = INTERPOLATOR.interpolate(color.x, transitionColor.x, w);
			color.y = INTERPOLATOR.interpolate(color.y, transitionColor.y, w);
			color.z = INTERPOLATOR.interpolate(color.z, transitionColor.z, w);
			color.w = INTERPOLATOR.interpolate(color.w, transitionColor.w, w);
		}
	}
	
	public void transitionTo(Vector4f color){
		transitionColor.set(color);
		transitionComplete = false;
		time = 0;
	}

	public Vector4f get(){
		return color;
	}
	
}