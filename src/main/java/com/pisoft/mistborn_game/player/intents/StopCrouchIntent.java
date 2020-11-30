package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.StopCrouchAction;

public class StopCrouchIntent extends PlayerIntent {
	
	public StopCrouchIntent() {
		super();
	}
	
	@Override
	public void resolve() {
		if (targetPlayer.isCrouching()) {
			// if (targetPlayer.getState() == State.CROUCHING) {
			// crouching --> stop crouching
			dispatchEvent(new StopCrouchAction());
		}		
	}
}
