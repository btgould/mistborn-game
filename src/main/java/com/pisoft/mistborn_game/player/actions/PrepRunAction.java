package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class PrepRunAction extends PlayerAction {

	@Override
	public void resolve() {
		targetPlayer.setCanRun(true);
		PlatformingConstants.setMaxAirSpeed(PlatformingConstants.getMaxRunSpeed());

		if (targetPlayer.isAccelerating()
				&& Math.abs(targetPlayer.getxSpeed()) >= PlatformingConstants.getMaxWalkSpeed()) {
			Side direction = (targetPlayer.getxSpeed() > 0) ? Side.RIGHT : Side.LEFT;

			if (targetPlayer.isGrounded()) {
				PlayerAction sideEffect = new RunAction(direction);
				sideEffect.setTargetPlayer(targetPlayer);
				sideEffect.resolve();
			} else {
				PlayerAction sideEffect = new AirAccAction(direction);
				sideEffect.setTargetPlayer(targetPlayer);
				sideEffect.resolve();
			}
		}
	}
}
