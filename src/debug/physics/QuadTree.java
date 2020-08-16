package debug.physics;

import java.util.ArrayList;

import org.javatroid.math.Vector2f;

public class QuadTree<E extends RigidBody> {

	private static final int MAX_SUBDIVISIONS = 10;
	private static final int MAX_OBJECT_PER_NODE = 100;
	
	int level;
	AABB bounds;
	ArrayList<E> elements;
	QuadTree<E>[] nodes;
	
	@SuppressWarnings("unchecked")
	public QuadTree(AABB bounds, int level){
		this.level = level;
		this.bounds = bounds;
		this.elements = new ArrayList<E>();
		this.nodes = new QuadTree[4];
	}
	
	private boolean hasChildren(){
		return nodes[0] != null;
	}
	
	private boolean canSplit(){
		return level < MAX_SUBDIVISIONS;
	}
	
	//returns the the index the the AABB box belongs to. -1 = parent, 0 = TR, 1 = TL, 2 = BL, 3 = BR
	private int getIndex(AABB box){
		Vector2f min = box.getMin();
		Vector2f max = box.getMax();
		
		float qw = bounds.getWidth() / 4;
		float qh = bounds.getHeight() / 4;
		Vector2f center = bounds.getCenter();
		
		AABB TR = new AABB(center.x + qw, center.y + qh, qw * 2, qh * 2);
		AABB TL = new AABB(center.x - qw, center.y + qh, qw * 2, qh * 2);
		AABB BL = new AABB(center.x - qw, center.y - qh, qw * 2, qh * 2);
		AABB BR = new AABB(center.x + qw, center.y - qh, qw * 2, qh * 2);
		
		if(TR.contains(min) && TR.contains(max))
			return 0; //TR
		else if(TL.contains(min) && TL.contains(max))
			return 1; //TL
		else if(BL.contains(min) && BL.contains(max))
			return 2; //BL
		else if(BR.contains(min) && BR.contains(max))
			return 3; //BR
		else
			return -1; //Parent
	}
	
	public void insert(E object){
		int index = getIndex(object.getVolume().getAABB());
		
		if(index == -1){
			if(elements.size() < MAX_OBJECT_PER_NODE){
				elements.add(object);
			}
		} else {
			if(canSplit()){
				split();
				nodes[index].insert(object);
			} else {
				elements.add(object);
			}
		}
	}
	
	public void get(E object, ArrayList<E> list){
		
		int index = getIndex(object.getVolume().getAABB());
		
		if(index == -1){
			list.addAll(elements);
			
			if(hasChildren()){
				for(int i = 0; i < 4; i++){
					nodes[i].get(object, list);
				}
			}
			
		} else {
			if(hasChildren()){
				nodes[index].get(object, list);
			}
		}
	}
	
	public void split(){
		
		if(hasChildren())
			return;
		
		if(level >= MAX_SUBDIVISIONS)
			return;
		
		float qw = bounds.getWidth() / 4;
		float qh = bounds.getHeight() / 4;
		Vector2f center = bounds.getCenter();
		
		AABB TR = new AABB(center.x + qw, center.y + qh, qw * 2, qh * 2);
		AABB TL = new AABB(center.x - qw, center.y + qh, qw * 2, qh * 2);
		AABB BL = new AABB(center.x - qw, center.y - qh, qw * 2, qh * 2);
		AABB BR = new AABB(center.x + qw, center.y - qh, qw * 2, qh * 2);
		
		nodes[0] = new QuadTree<E>(TR, level + 1);
		nodes[1] = new QuadTree<E>(TL, level + 1);
		nodes[2] = new QuadTree<E>(BL, level + 1);
		nodes[3] = new QuadTree<E>(BR, level + 1);
	}
	
	public void clear(){
		elements.clear();
		if(hasChildren()){
			for(int i = 0; i < 4; i++){
				nodes[i].clear();
				nodes[i] = null;
			}	
		}
	}
	
//	public void debugDraw(Renderer2D renderer){
//		
//		Vector2f min = bounds.getMin();
//		Vector2f max = bounds.getMax();
//		
//		Vertex[] vertices = new Vertex[4];
//		vertices[0] = new Vertex(new org.javatroid.math.Vector2f(min.x, min.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 1, 1));
//		vertices[1] = new Vertex(new org.javatroid.math.Vector2f(max.x, min.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 1, 1));
//		vertices[2] = new Vertex(new org.javatroid.math.Vector2f(max.x, max.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 1, 1));
//		vertices[3] = new Vertex(new org.javatroid.math.Vector2f(min.x, max.y), new org.javatroid.math.Vector2f(0, 0), new org.javatroid.math.Vector4f(1, 0, 1, 1));
//		
//		renderer.begin(DrawMode.LINE_LOOP);
//		renderer.draw(vertices);
//		renderer.end();
//		
//		if(hasChildren()){
//			for(int i = 0; i < 4; i++){
//				nodes[i].debugDraw(renderer);
//			}
//		}
//	}
}
