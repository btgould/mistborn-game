package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class StopCrouchAction extends PlayerAction {
	
	@Override
	public void resolve() {
		targetPlayer.setCanJump(true);
		targetPlayer.setCrouching(false);
		
		if (targetPlayer.wantsToAccelerate()) {
			if (targetPlayer.getCanRun() && Math.abs(targetPlayer.getxSpeed()) >= PlatformingConstants.getMaxWalkSpeed()) {
				PlayerAction sideEffect = new RunAction(targetPlayer.getFacingSide());
				sideEffect.setTargetPlayer(targetPlayer);
				sideEffect.resolve();
			} else {
				PlayerAction sideEffect = new WalkAction(targetPlayer.getFacingSide());
				sideEffect.setTargetPlayer(targetPlayer);
				sideEffect.resolve();
			}
		}
	}
}
