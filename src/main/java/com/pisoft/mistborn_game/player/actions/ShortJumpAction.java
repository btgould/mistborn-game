package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class ShortJumpAction extends PlayerAction {
	
	@Override
	public void resolve() {
		targetPlayer.setJumping(false);
		targetPlayer.setJumpReleased(true);

		targetPlayer.setySpeed(PlatformingConstants.getShortJumpSpeed());
	}
}
