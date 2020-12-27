package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;
import com.pisoft.mistborn_game.player.intents.AccelerateIntent;

public class PrepRunAction extends PlayerAction {

	public PrepRunAction() {
		super();
	}

	@Override
	public void resolve() {
		targetPlayer.setCanRun(true);
		PlatformingConstants.setMaxAirSpeed(PlatformingConstants.getMaxRunSpeed());

		// TODO: does not dispatch after wall jump
		if (targetPlayer.isAccelerating()
				&& Math.abs(targetPlayer.getxSpeed()) >= PlatformingConstants.getMaxWalkSpeed()) {
			Side direction = (targetPlayer.getxSpeed() > 0) ? Side.RIGHT : Side.LEFT;

			dispatchEvent(new AccelerateIntent(direction));
		}
	}
}
