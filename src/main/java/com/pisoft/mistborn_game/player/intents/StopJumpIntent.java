package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.JumpReleaseAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.ShortJumpAction;
import com.pisoft.mistborn_game.player.state.State;

public class StopJumpIntent extends PlayerIntent {
	
	@Override
	public PlayerAction parseIntent() {
		if (targetPlayer.getState() == State.JUMPING) {
			// jumping --> shorten jump
			return new ShortJumpAction();
		} else {
			// not jumping --> jump relased
			return new JumpReleaseAction();
		}
	}
}
