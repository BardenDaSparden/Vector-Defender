//package debug.physics;
//
//import org.javatroid.core.Application;
//import org.javatroid.core.BaseGame;
//import org.javatroid.core.Input;
//import org.javatroid.math.FastMath;
//import org.javatroid.math.Vector2f;
//
//import static org.lwjgl.opengl.GL11.*;
//
//public class PhysicsEngineDemo implements BaseGame{
//
//	public static void main(String[] args){
//		Application application = new Application(60, new PhysicsEngineDemo());
//		application.start();
//	}
//
//	PhysicsScene scene;
//	
//	RigidBody body2;
//
//	Input input;
//	
//	public PhysicsEngineDemo(){
//		input = new Input();
//	}
//	
//	public void setupWindow() {
//		//Window.setVsync(true);
//		//Window.setMSAA(MSAA.X8);
//	}
//
//	public void create() {
//		scene = new PhysicsScene(10, 1.0f / 60f);
//		
//		Polygon bottomWall = Polygon.createRectangle(0, 0, 430, 30, 0);
//		Polygon bottomWall2 = Polygon.createRectangle(0, 0, 430, 30, 0);
//		Polygon leftWall = Polygon.createRectangle(0, 0, 300, 30, 0);
//		Polygon leftWall2 = Polygon.createRectangle(0, 0, 30, 430, 0);
//		Polygon rightWall = Polygon.createRectangle(0, 0, 300, 30, 0);
//		Polygon line = new Polygon(new Vector2f(-200, 200), new Vector2f(-70, 70), new Vector2f(-70, 71), new Vector2f(-200, 201));
//		
//		Material wallMaterial = new Material(1, 0.5f);
//		RigidBody wall = new RigidBody(bottomWall, wallMaterial);
//		wall.setOrientation(5);
//		RigidBody wall2 = new RigidBody(leftWall, wallMaterial);
//		wall2.setOrientation(25);
//		RigidBody wall3 = new RigidBody(rightWall, wallMaterial);
//		wall3.setOrientation(-35);
//		RigidBody wall4 = new RigidBody(bottomWall2, wallMaterial);
//		wall4.setPosition(new Vector2f(-250, -256));
//		RigidBody wall5 = new RigidBody(leftWall2, wallMaterial);
//		wall5.setPosition(new Vector2f(-400, -50));
//		
//		RigidBody lineBody = new RigidBody(line, wallMaterial);
//		lineBody.setStatic();
//		lineBody.setPosition(new Vector2f(-175, 175));
//		
//		wall.setStatic();
//		wall2.setStatic();
//		wall3.setStatic();
//		wall4.setStatic();
//		wall5.setStatic();
//		
//		wall.setPosition(new Vector2f(140, -240));
//		wall2.setPosition(new Vector2f(200, 200));
//		wall3.setPosition(new Vector2f(-200, 0));
//		
//		Circle c = new Circle(new Vector2f(0, 0), 40);
//		body2 = new RigidBody(c, wallMaterial);
//		body2.setPosition(new Vector2f(200, 350));
//		
//		scene.add(wall);
//		scene.add(wall2);
//		scene.add(wall3);
//		scene.add(wall4);
//		scene.add(wall5);
//		scene.add(body2);
//	}
//	
//	public void update() {
//		Vector2f mouse = new Vector2f(input.getMouseX() * 1 /*Window.getWidth() / 2*/, input.getMouseY() * 1 /*Window.getHeight() / 2*/);
//		
//		float s = 10000;
//		
//		if(input.isKeyDown(Input.KEY_S)){
//			scene.applyForce(new Vector2f(0, -s));
//		}
//		
//		if(input.isKeyDown(Input.KEY_W)){
//			scene.applyForce(new Vector2f(0, s));
//		}
//		
//		if(input.isKeyDown(Input.KEY_A)){
//			scene.applyForce(new Vector2f(-s, 0));
//		}
//		
//		if(input.isKeyDown(Input.KEY_D)){
//			scene.applyForce(new Vector2f(s, 0));
//		}
//		
//		if(input.isMouseButtonPressed(0)){
//			Circle c = new Circle(mouse, 8 + FastMath.random() * 8);
//			Material m = new Material(1, 0.2f);
//			RigidBody body = new RigidBody(c, m);
//			body.setPosition(mouse);
//			scene.add(body);
//		}
//		
//		if(input.isMouseButtonPressed(1)){
//			float si = 16 + FastMath.random() * 32;
//			Polygon p = Polygon.createRectangle(0, 0, si, si, 0);
//			Material m = new Material(1, 0.2f);
//			RigidBody b = new RigidBody(p, m);
//			b.setPosition(mouse);
//			scene.add(b);
//		}
//		
//		if(input.isKeyPressed(Input.KEY_SPACE)){
//			System.out.println(scene.getSize());
//		}
//		
//		if(input.isKeyPressed(Input.KEY_GRAVE)){
//			scene.toggleDebugMode();
//		}
//		
//		scene.update();
//	}
//
//	public void draw(float i) {
//		glClear(GL_COLOR_BUFFER_BIT);
//	}
//
//	public void dispose() {
//		
//	}
//	
//}
