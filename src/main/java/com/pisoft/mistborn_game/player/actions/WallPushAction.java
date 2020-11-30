package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;

public class WallPushAction extends PlayerAction {

	private Side direction;
	
	public WallPushAction(Side direction) {
		super();
		
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		targetPlayer.setWantsToAccelerate(true);
		targetPlayer.setAccelerating(false);
		targetPlayer.setSliding(false);
		targetPlayer.setAtWall(true);
		targetPlayer.setWallPushing(true);
		targetPlayer.setFacingSide(direction);

		targetPlayer.setxAcc(0);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		WallPushAction clone = (WallPushAction) super.clone();
		
		clone.direction = this.direction;
		
		return clone;
	}
}
