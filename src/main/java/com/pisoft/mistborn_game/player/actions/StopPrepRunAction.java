package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class StopPrepRunAction extends PlayerAction {
	
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
}
