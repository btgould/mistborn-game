package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.StopCrouchAction;

public class StopCrouchIntent extends PlayerIntent {
	
	@Override
	public PlayerAction parseIntent() {
		if (targetPlayer.isCrouching()) {
			// if (targetPlayer.getState() == State.CROUCHING) {
			// crouching --> stop crouching
			return new StopCrouchAction();
		}
		
		return null;
	}
}
