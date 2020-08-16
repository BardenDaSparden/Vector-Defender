package debug.physics;

import org.javatroid.math.FastMath;
import org.javatroid.math.Vector2f;

public class Manifold {
	
	RigidBody A, B;
	
	boolean overlap;
	Vector2f normal;
	float penetration;
	Vector2f[] contacts = {null, null};
	float e;
	float sf;
	float kf;
	
	public Manifold(RigidBody A, RigidBody B){
		this.A = A;
		this.B = B;
		this.normal = new Vector2f();
		this.penetration = 0;
	}
	
	public void solveCollision(){
		Collisions.solve(this, A.getVolume(), B.getVolume());
	}
	
	public void initialize(){
		e = FastMath.min(A.getMaterial().getRestitution(), B.getMaterial().getRestitution());
		sf = FastMath.sqrt(A.getMaterial().getStaticFriction() + B.getMaterial().getStaticFriction());
		kf = FastMath.sqrt(A.getMaterial().getKineticFriction() + B.getMaterial().getKineticFriction());
		
		for(int i = 0; i < contacts.length; i++){
			Vector2f contact = contacts[i];
			if(contact == null)
				continue;
			
			Vector2f ra = contact.sub(A.getPosition());
			Vector2f rb = contact.sub(B.getPosition());
			
			Vector2f relativeVelocity = B.getVelocity().add(Vector2f.cross(B.getAngularVelocity(), rb)).sub(
										A.getVelocity()).sub(Vector2f.cross(A.getAngularVelocity(), ra));
			
			float resting = 1f;
			
			if(relativeVelocity.lengthSquared() < resting + 0.0001f)
				e = 0.0f;
		}
		
	}
	
	public void applyMTV(){
		Vector2f MTV = normal.scale(penetration);
		float tm = A.getMass() + B.getMass();
		Vector2f pa = MTV.scale(B.getMass() / tm);
		Vector2f pb = MTV.scale(A.getMass() / tm);
		
		A.setPosition(A.getPosition().add(pa));
		B.setPosition(B.getPosition().sub(pb));
	}
	
	public void newImpulse(){
		
		for(int i = 0; i < contacts.length; i++){
			Vector2f contact = contacts[i];
			if(contact == null)
				continue;
			
			Vector2f ra = contact.sub(A.getPosition());
			Vector2f rb = contact.sub(B.getPosition());
			
			Vector2f relativeVelocity = B.getVelocity().add(Vector2f.cross(B.getAngularVelocity(), rb)).sub(A.getVelocity()).sub(Vector2f.cross(A.getAngularVelocity(), ra));
			relativeVelocity = B.getVelocity().sub(A.getVelocity());
			
			float velocityAlongNormal = Vector2f.dot(relativeVelocity, normal);
			
			if(velocityAlongNormal < 0)
				return;
			
			float raCrossN = Vector2f.cross(ra, normal);
			float rbCrossN = Vector2f.cross(rb, normal);
			
			float invMassSum = A.getInvMass() + B.getInvMass() +
							   (raCrossN * raCrossN * A.getInvMass()) + (rbCrossN * rbCrossN * B.getInvMass());
			
			invMassSum = A.getInvMass() + B.getInvMass();
			
			float j = -(1 + e) * velocityAlongNormal;
			j /= invMassSum;
			
			Vector2f impulse = normal.scale(j);
			
			A.applyImpulse(impulse.negate(), ra);
			B.applyImpulse(impulse, rb);
			
			Vector2f t = relativeVelocity.sub(normal.scale(Vector2f.dot(relativeVelocity, normal)));
		    t = t.normalize();

		    // j tangent magnitude
		    float jt = -Vector2f.dot(relativeVelocity, t);
		    jt /= invMassSum;

		    // Don't apply tiny friction impulses
		    if(Math.abs(jt) < 0.0001f)
		      return;

		    // Coulumb's law
		    Vector2f frictionImpulse = new Vector2f();
		    if(Math.abs(jt) < j * sf)
		    	frictionImpulse = frictionImpulse.scale(jt);
		    else
		    	frictionImpulse = frictionImpulse.scale(-jt * kf);
			
		}
		
	}
	
	public void applyImpulse(){
		
		for(int i = 0; i < contacts.length; i++){
			Vector2f contact = contacts[i];
			
			if(contact == null)
				continue;
			
			Vector2f ra = contact.sub(A.getPosition());
			Vector2f rb = contact.sub(B.getPosition());
			
//			Vector2f vA = A.getVelocity();
//			Vector2f vB = B.getVelocity();
			
			Vector2f relativeVelocity = B.getVelocity().add(Vector2f.cross(B.getAngularVelocity(), rb)).sub(A.getVelocity()).sub(Vector2f.cross(A.getAngularVelocity(), ra));
			
			float velAlongNormal = Vector2f.dot(relativeVelocity, normal);
			
			if(velAlongNormal < 0){
				return;
			}
			
			float raCrossN = Vector2f.cross(ra, normal);
			float rbCrossN = Vector2f.cross(rb, normal);
			
			float invMassSum = A.getInvMass() + B.getInvMass() + (raCrossN * raCrossN) * A.getInvInertia() + (rbCrossN * rbCrossN) * B.getInvInertia();
			
			float j = -(1 + e) * velAlongNormal;
			j /= invMassSum;
			
			Vector2f impulse = normal.scale(j);
			
			A.applyImpulse(impulse.negate(), contact);
			B.applyImpulse(impulse, contact);
			
			relativeVelocity = B.getVelocity().add(Vector2f.cross(B.getAngularVelocity(), rb)).sub(A.getVelocity()).sub(Vector2f.cross(A.getAngularVelocity(), ra));
			raCrossN = Vector2f.cross(ra, normal);
			rbCrossN = Vector2f.cross(rb, normal);
			
			invMassSum = A.getInvMass() + B.getInvMass() + (raCrossN * raCrossN) * A.getInvInertia() + (rbCrossN * rbCrossN) * B.getInvInertia();
			Vector2f t = relativeVelocity.sub(normal.scale(Vector2f.dot(relativeVelocity, normal)));
		    t = t.normalize();

		    // j tangent magnitude
		    float jt = -Vector2f.dot(relativeVelocity, t);
		    jt /= invMassSum;

		    // Don't apply tiny friction impulses
		    if(Math.abs(jt) < 0.0001f)
		      return;

		    // Coulumb's law
		    Vector2f frictionImpulse = new Vector2f();
		    if(Math.abs(jt) < j * sf)
		    	frictionImpulse = frictionImpulse.scale(jt);
		    else
		    	frictionImpulse = frictionImpulse.scale(-jt * kf);

			System.out.println("J : " + j);
			System.out.println("Jt : " + jt);
			
			System.out.println(Vector2f.cross(normal, t));
			System.out.println(Vector2f.dot(t, relativeVelocity.normalize()));
			
			
			//A.applyImpulse(frictionImpulse.negate(), contact);
			//B.applyImpulse(frictionImpulse, contact);
			
//			newVA = A.getVelocity();
//			newVB = B.getVelocity();
//			
//			Vector2f fVA = newVA.sub(frictionImpulse.scale(A.getInvMass()));
//			Vector2f fVB = newVB.add(frictionImpulse.scale(B.getInvMass()));
//			
//			A.setVelocity(fVA);
//			B.setVelocity(fVB);
			
		}
	}
	
//	public void applyImpulse2(){
//
//		int numContacts = 0;
//		for(int i = 0; i < contacts.length; i++){
//			if(contacts[i] != null)
//				numContacts++;
//		}
//		
//		for (int i = 0; i < contacts.length; ++i)
//		{
//			
//			if(contacts[i] == null)
//				continue;
//			
//			Vector2f contact = contacts[i];
//			Vector2f ra = contact.sub(A.getPosition());
//			Vector2f rb = contact.sub(B.getPosition());
//
//			// Relative velocity
//			Vector2f relativeVelocity = B.getVelocity().add( Vector2f.cross(B.getAngularVelocity(), rb)).sub(A.getVelocity()).sub(Vector2f.cross(A.getAngularVelocity(), ra));
//
//			// Relative velocity along the normal
//			float contactVelocity = Vector2f.dot( relativeVelocity, normal );
//
//			// Do not resolve if velocities are separating
//			if (contactVelocity > 0){
//				return;
//			}
//
//			// real raCrossN = Cross( ra, normal );
//			// real rbCrossN = Cross( rb, normal );
//			// real invMassSum = A->im + B->im + Sqr( raCrossN ) * A->iI + Sqr(
//			// rbCrossN ) * B->iI;
//			float raCrossN = Vector2f.cross( ra, normal );
//			float rbCrossN = Vector2f.cross( rb, normal );
//			float invMassSum = A.getInvMass() + B.getInvMass() + (raCrossN * raCrossN) * A.getInvInertia() + (rbCrossN * rbCrossN) * B.getInvInertia();
//
//			float e = FastMath.min(A.getMaterial().getRestitution(), B.getMaterial().getRestitution());
//			
//			// Calculate impulse scalar
//			float j = -(1.0f + e) * contactVelocity;
//			j /= invMassSum;
//			j /= (float)numContacts;
//
//			// Apply impulse
//			Vector2f impulse = normal.scale(j);
//			A.applyImpulse(impulse.negate(),contact);
//			B.applyImpulse(impulse,contact);
//
//			relativeVelocity = B.getVelocity().add(Vector2f.cross(B.getAngularVelocity(), rb)).sub(A.getVelocity()).sub(Vector2f.cross( A.getAngularVelocity(), ra));
//
//			Vector2f t = relativeVelocity.clone();
//			t.add( normal.scale( -Vector2f.dot( relativeVelocity, normal ) ));
//			t.normalize();
//
//			float jt = -Vector2f.dot(relativeVelocity, t);
//			jt /= invMassSum;
//			jt /= (float)numContacts;
//
//			// Don't apply tiny friction impulses
//			if (jt < 0.01f){
//				return;
//			}
//
//			float sfa = A.getMaterial().getStaticFriction();
//			float sfb = B.getMaterial().getStaticFriction();
//			
//			float kfa = A.getMaterial().getKineticFriction();
//			float kfb = B.getMaterial().getKineticFriction();
//			
//			float staticFriction = sfa;
//			float kineticFriction = kfa;
//			
//			Vector2f tangentImpulse;
//			if (StrictMath.abs( jt ) < j * staticFriction)
//				tangentImpulse = t.scale(jt);
//			else
//				tangentImpulse = t.scale(j).scale(-kineticFriction);
//			
//
//			A.applyImpulse(tangentImpulse.negate(),contact);
//			B.applyImpulse(tangentImpulse,contact);
//		}
//	}
	
public void positionCorrection(){
		
		float percent = 0.8f; // usually 20% to 80%
		float slop = 0.01f; // usually 0.01 to 0.1
		
		Vector2f correction = normal.scale(Math.max( penetration - slop, 0.0f ) / (A.getInvMass() + B.getInvMass()) * percent);
		
		Vector2f posA = A.getPosition();
		Vector2f posB = B.getPosition();
		
		Vector2f pA = correction.scale(A.getInvMass());
		Vector2f pB = correction.scale(B.getInvMass());
		
		
		A.setPosition(posA.add(pA));
		B.setPosition(posB.add(pB));
		
	}
}
