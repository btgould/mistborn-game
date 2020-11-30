package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.actions.AirAccAction;
import com.pisoft.mistborn_game.player.actions.CantAccAction;
import com.pisoft.mistborn_game.player.actions.CrouchTurnAction;
import com.pisoft.mistborn_game.player.actions.RunAction;
import com.pisoft.mistborn_game.player.actions.WalkAction;
import com.pisoft.mistborn_game.player.actions.WallPushAction;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class AccelerateIntent extends PlayerIntent {

	private Side direction;

	public AccelerateIntent(Side direction) {
		super();

		this.direction = direction;
	}

	@Override
	public void resolve() {
		if (targetPlayer.isCrouching()) {
			// crouching --> crouch turn
			dispatchEvent(new CrouchTurnAction(direction));
		} else if (targetPlayer.getWallSide() == direction) {
			// wall in the way --> wall push
			dispatchEvent(new WallPushAction(direction));
		} else {
			Side movingDirection = (targetPlayer.getxSpeed() > 0) ? Side.RIGHT : Side.LEFT;

			if (targetPlayer.isGrounded()) {
				if (Math.abs(targetPlayer.getxSpeed()) < PlatformingConstants.getMaxWalkSpeed()
						|| direction != movingDirection) {
					// no wall in the way, grounded, not crouching, speed < max walk speed or
					// turning around --> walk
					dispatchEvent(new WalkAction(direction));
				} else if (targetPlayer.getCanRun()
						&& (Math.abs(targetPlayer.getxSpeed()) < PlatformingConstants.getMaxRunSpeed()
								|| direction != movingDirection)) {
					// no wall in the way, grounded, not crouching, speed >= max walk speed, speed <
					// max run speed, can run --> run
					dispatchEvent(new RunAction(direction));
				} else {
					// no wall in the way, grounded, not crouching, speed >= max walk speed, speed
					// >= max run speed OR can't run --> can't acc
					dispatchEvent(new CantAccAction(direction));
				}
			} else {
				if (Math.abs(targetPlayer.getxSpeed()) < PlatformingConstants.getMaxAirSpeed()
						|| direction != movingDirection) {
					// no wall in the way, not grounded, speed < max air speed --> air acc
					dispatchEvent(new AirAccAction(direction));
				} else {
					// no wall in the way, not grounded, speed >= max air speed --> can't acc
					dispatchEvent(new CantAccAction(direction));
				}
			}
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		AccelerateIntent clone = (AccelerateIntent) super.clone();
		
		clone.direction = this.direction;
		
		return clone;
	}
}
