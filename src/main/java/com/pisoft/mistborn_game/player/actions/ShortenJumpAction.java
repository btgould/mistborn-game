package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class ShortenJumpAction extends PlayerAction {
	
	public ShortenJumpAction() {
		super();
	}
	
	@Override
	public void resolve() {
		targetPlayer.setJumpReleased(true);

		targetPlayer.setySpeed(PlatformingConstants.getShortJumpSpeed());
	}
}
