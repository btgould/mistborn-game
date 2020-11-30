package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class CantAccAction extends PlayerAction {

	private Side direction;

	public CantAccAction(Side direction) {
		this.direction = direction;
	}

	@Override
	public void resolve() {
		targetPlayer.setFacingSide(direction);
		targetPlayer.setWantsToAccelerate(true);
		if (targetPlayer.isGrounded()) {
			if (Math.abs(targetPlayer.getxSpeed()) <= PlatformingConstants.getMaxWalkSpeed()) {
				targetPlayer.setWalking(true);
			} else {
				targetPlayer.setRunning(true);
			}
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		CantAccAction clone = (CantAccAction) super.clone();
		
		clone.direction = this.direction;
		
		return clone;
	}
}
