package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class WalkAction extends PlayerAction {
	
	private Side direction;
	
	public WalkAction(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		targetPlayer.setFacingSide(direction);
		targetPlayer.setWantsToAccelerate(true);
		targetPlayer.setAccelerating(true);
		targetPlayer.setWalking(true);
		targetPlayer.setRunning(false);
		targetPlayer.setSliding(false);
		targetPlayer.setAtWall(false);
		targetPlayer.setWallPushing(false);
		targetPlayer.setWallSide(Side.NONE);

		// player must be facing either LEFT or RIGHT
		double accMultiplier = (targetPlayer.getFacingSide() == Side.RIGHT) ? 1 : -1;
		double xAcc = PlatformingConstants.getWalkAcc();
		targetPlayer.setxAcc(xAcc * accMultiplier);
	}
}
