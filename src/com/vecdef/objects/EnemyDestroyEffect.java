package com.vecdef.objects;

import java.util.ArrayList;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;
import org.javatroid.math.Vector4f;

import com.vecdef.model.LinePrimitive;
import com.vecdef.model.Mesh;
import com.vecdef.model.MeshLayer;
import com.vecdef.model.Primitive;

public class EnemyDestroyEffect {

	public EnemyDestroyEffect(Vector2f position, Mesh mesh){
		ArrayList<MeshLayer> layers = mesh.getLayers();
		
		for(int i = 0; i < layers.size(); i++){
			
			MeshLayer layer = layers.get(i);
			ArrayList<Primitive> shapes = layer.getPrimitives();
			
			for(int j = 0; j < shapes.size(); j++){
				Primitive p = shapes.get(j);
				
				if(p instanceof LinePrimitive){
					LinePrimitive line = (LinePrimitive)p;
					ArrayList<Vector2f> vertices = line.getVertices();
					ArrayList<Vector4f> colors = line.getColors();
					
					for(int k = 0; k < vertices.size(); k+=2){
						
						Vector2f v0 = vertices.get(k);
						Vector2f v1 = vertices.get(k+1);
						
						Vector4f c0 = colors.get(k);
						
						Particle particle = new Particle(v0, v1, c0);
						particle.getTransform().setTranslation(position.clone());
						
						particle.maxLife = 120;
						
						float angle = FastMath.random() * 360f;
						float speed = 2;
						
						particle.angularVelocity = FastMath.randomf(-3, 3);
						particle.velocity.x = FastMath.cosd(angle) * speed;
						particle.velocity.y = FastMath.sind(angle) * speed;
						
						EntityManager.add(particle);
					}
				}
			}
		}
		
	}
	
}
