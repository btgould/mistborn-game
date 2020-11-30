package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.actions.StopAccAction;
import com.pisoft.mistborn_game.player.actions.StopWallPushAction;

public class StopAccIntent extends PlayerIntent {

	private Side direction;

	public StopAccIntent(Side direction) {
		super();
		
		this.direction = direction;
	}

	@Override
	public void resolve() {
		// only accept stop accelerate intent if it is in the same direction as the
		// player is accelerating
		if ((targetPlayer.isAccelerating() && direction == targetPlayer.getFacingSide()) || !targetPlayer.isAccelerating()) {
			if (targetPlayer.isWallPushing()) {
				// at wall --> stop wall pushing
				dispatchEvent(new StopWallPushAction());
			} else {
				// not at wall --> stop accelerating
				dispatchEvent(new StopAccAction());
			}
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		StopAccIntent clone = (StopAccIntent) super.clone();
		
		clone.direction = this.direction;
		
		return clone;
	}
}
