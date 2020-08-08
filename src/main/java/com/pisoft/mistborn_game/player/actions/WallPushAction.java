package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;

public class WallPushAction extends PlayerAction {

	private Side direction;
	
	public WallPushAction(Side direction) {
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
}
