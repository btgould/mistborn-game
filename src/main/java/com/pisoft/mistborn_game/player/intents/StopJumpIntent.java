package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.JumpReleaseAction;
import com.pisoft.mistborn_game.player.actions.ShortenJumpAction;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class StopJumpIntent extends PlayerIntent {
	
	public StopJumpIntent() {
		super();
		
	}

	@Override
	public void resolve() {
		if (targetPlayer.getySpeed() <= PlatformingConstants.getShortJumpSpeed() && targetPlayer.isJumping()) {
			// jumping --> shorten jump
			dispatchEvent(new ShortenJumpAction());
		} else {
			// not jumping --> jump relased
			dispatchEvent(new JumpReleaseAction());
		}
	}
}
