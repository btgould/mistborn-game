package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class StopPrepRunAction extends PlayerAction {
	
	public StopPrepRunAction() {
		super();
	}
	
	@Override
	public void resolve() {
		targetPlayer.setCanRun(false);
		targetPlayer.setRunning(false);
		if (targetPlayer.getxSpeed() != 0) {
			targetPlayer.setWalking(true);
		}
		PlatformingConstants.setMaxAirSpeed(PlatformingConstants.getMaxWalkSpeed());

		targetPlayer.setxAcc(0);
	}
	
	@Override
	public boolean isCompatible(PlayerAction action) {
		if (action instanceof PrepRunAction) {
			return false;
		}
		
		return true;
	}
}
