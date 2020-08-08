package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.StopAccAction;
import com.pisoft.mistborn_game.player.actions.StopWallPushAction;

public class StopAccIntent extends PlayerIntent {
	
	private Side direction;

	public StopAccIntent(Side direction) {
		this.direction = direction;
	}

	@Override
	public PlayerAction parseIntent() {
		// only accept stop accelerate intent if it is in the same direction as the
		// player is accelerating
		if (direction == targetPlayer.getFacingSide()) {
			if (targetPlayer.isWallPushing()) {
				// at wall --> stop wall pushing
				return new StopWallPushAction();
			} else {
				// not at wall --> stop accelerating
				return new StopAccAction();
			}
		}
		
		return null;
	}
}
