package com.vecdef.util;

public class CollisionMasks {
	
	public final int PLAYER 		= 0b0000000000000001;
	//public final int BULLET			= 0b0000000000000010;
	public final int ENEMY			= 0b0000000000000100;
	public final int MULTIPLIER 	= 0b0000000000001000;
	public final int BLACK_HOLE 	= 0b0000000000010000;
	public final int ABILITY 		= 0b0000000000100000;
	public final int BULLET			= 0b0000001111000000;
	public final int BULLET_P1 		= 0b0000000001000000;
	public final int BULLET_P2 		= 0b0000000010000000;
	public final int BULLET_P3 		= 0b0000000100000000;
	public final int BULLET_P4 		= 0b0000001000000000;
	
	@Deprecated
	public String asString(int mask){
		StringBuilder source = new StringBuilder();
		
		boolean bPlayer = (mask & PLAYER) == PLAYER;
		boolean bBullet = (mask & BULLET) == BULLET;
		boolean bEnemy = (mask & ENEMY) == ENEMY;
		boolean bMultiplier = (mask & MULTIPLIER) == MULTIPLIER;
		boolean bBlackHole = (mask & BLACK_HOLE) == BLACK_HOLE;
		
		if(bPlayer)
			source.append(" PLAYER ");
		
		if(bBullet)
			source.append(" BULLET ");
		
		if(bEnemy)
			source.append(" ENEMY ");
		
		if(bMultiplier)
			source.append(" MULTIPLIER ");
		
		if(bBlackHole)
			source.append(" BLACK_HOLE ");
		
		return source.toString();
		//return "";
	}
	
}