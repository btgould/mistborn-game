package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.actions.AirAccAction;
import com.pisoft.mistborn_game.player.actions.CrouchTurnAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.RunAction;
import com.pisoft.mistborn_game.player.actions.WalkAction;
import com.pisoft.mistborn_game.player.actions.WallPushAction;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class AccelerateIntent extends PlayerIntent {

	private Side direction;

	public AccelerateIntent(Side direction) {
		this.direction = direction;
	}

	@Override
	public PlayerAction parseIntent() {
		// only accept accelerate intent if player is not already accelerating the other
		// direction
		if (!(targetPlayer.isAccelerating() && targetPlayer.getFacingSide() != direction)) {
			if (targetPlayer.isCrouching()) {
				// crouching --> crouch turn
				return new CrouchTurnAction(direction);
			} else if (targetPlayer.getWallSide() == direction) {
				// wall in the way --> wall push
				return new WallPushAction(direction);
			} else {
				Side movingDirection = (targetPlayer.getxSpeed() > 0) ? Side.RIGHT : Side.LEFT;

				if (targetPlayer.isGrounded()) {
					if (Math.abs(targetPlayer.getxSpeed()) < PlatformingConstants.getMaxWalkSpeed()
							|| direction != movingDirection) {
						// no wall in the way, grounded, not crouching, speed < max walk speed or
						// turning around --> walk
						return new WalkAction(direction);
					} else if (targetPlayer.getCanRun()
							&& (Math.abs(targetPlayer.getxSpeed()) < PlatformingConstants.getMaxRunSpeed()
									|| direction != movingDirection)) {
						// no wall in the way, grounded, not crouching, speed > max walk speed, can run
						// --> run
						return new RunAction(direction);
					}
				} else {
					// no wall in the way, not grounded --> air acc
					if (Math.abs(targetPlayer.getxSpeed()) < PlatformingConstants.getMaxAirSpeed()
							|| direction != movingDirection) {
						return new AirAccAction(direction);
					}
				}
			}
		}

		// for any accelerate intent, player wants to accelerate
		return new PlayerAction() {
			@Override
			public void resolve() {
				targetPlayer.setWantsToAccelerate(true);
			}
		};
	}
}
