package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.intents.AccelerateIntent;

public class StopCrouchAction extends PlayerAction {

	public StopCrouchAction() {
		super();
	}

	@Override
	public void resolve() {
		targetPlayer.setCanJump(true);
		targetPlayer.setCrouching(false);

		if (targetPlayer.wantsToAccelerate()) {
			dispatchEvent(new AccelerateIntent(targetPlayer.getFacingSide()));

		}
	}

	@Override
	public boolean isCompatible(PlayerAction action) {
		if (action instanceof CrouchAction) {
			return false;
		}

		return true;
	}
}
