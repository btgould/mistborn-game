package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.CrouchAction;

public class CrouchIntent extends PlayerIntent {
	
	public CrouchIntent() {
		super();
	}
	
	@Override
	public void resolve() {
		if (targetPlayer.isGrounded()) {
			// grounded --> crouch
			dispatchEvent(new CrouchAction());
		}		
	}
}
