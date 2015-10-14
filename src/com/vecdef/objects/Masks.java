package com.vecdef.objects;

public class Masks {
	public static final int NONE = 0x00;
	public static final EntityMasks Entities = new EntityMasks();
	public static final CollisionMasks Collision = new CollisionMasks();
}

class EntityMasks {
	public final int PLAYER 		= 0b0000000000000001;
	public final int BULLET 		= 0b0000000000000010;
	public final int ENEMY			= 0b0000000000000100;
	public final int MULTIPLIER 	= 0b0000000000001000;
	public final int PARTICLE 		= 0b0000000000010000;
	public final int OTHER			= 0b1000000000000000;
}

class CollisionMasks {
	public final int PLAYER 		= 0b0000000000000001;
	public final int BULLET			= 0b0000000000000010;
	public final int ENEMY			= 0b0000000000000100;
	public final int MULTIPLIER 	= 0b0000000000001000;
}