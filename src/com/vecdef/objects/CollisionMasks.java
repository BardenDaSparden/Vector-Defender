package com.vecdef.objects;

public class CollisionMasks {
	public final int PLAYER 		= 0b0000000000000001;
	public final int BULLET			= 0b0000000000000010;
	public final int ENEMY			= 0b0000000000000100;
	public final int MULTIPLIER 	= 0b0000000000001000;
	public final int BLACK_HOLE 	= 0b0000000000010000;
	public final int ABILITY 		= 0b0000000000100000;
	
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
	}
	
}