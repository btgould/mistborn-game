package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.CrouchAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;

public class CrouchIntent extends PlayerIntent {
	
	@Override
	public PlayerAction parseIntent() {
		if (targetPlayer.isGrounded()) {
			// grounded --> crouch
			return new CrouchAction();
		}
		
		return null;
	}
}
