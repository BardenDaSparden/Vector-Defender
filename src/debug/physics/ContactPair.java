package debug.physics;

public class ContactPair {

	RigidBody A, B;
	
	public ContactPair(RigidBody A, RigidBody B){
		this.A = A;
		this.B = B;
	}
	
	public boolean equals(Object object){
		
		boolean result = false;
		
		if(object != null && object instanceof ContactPair){
			ContactPair pair = (ContactPair) object;
			result = (A == pair.A && B == pair.B || A == pair.B && B == pair.A);
		}
		
		return result;
	}
	
}
