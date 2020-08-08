package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.DoubleJumpAction;
import com.pisoft.mistborn_game.player.actions.JumpAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.WallJumpAction;

public class JumpIntent extends PlayerIntent {

	@Override
	public PlayerAction parseIntent() {
		// only accept jump intent if player has released jump button since last jump
		if (targetPlayer.isJumpReleased()) {
			if (targetPlayer.isGrounded()) {
				if (!targetPlayer.isCrouching() && targetPlayer.canJump()) {
					// grounded, not crouching, can jump --> jump
					return new JumpAction();
				}
			} else {
				if (/* targetPlayer.getState() == State.WALL_FALLING */ targetPlayer.isWallPushing()) {
					if (targetPlayer.getWallSide() != targetPlayer.getLastWallJumpSide()) {
						// not grounded, pushing on opposite wall as last wall jump --> wall jump
						return new WallJumpAction();
					} else if (targetPlayer.canDoubleJump()) {
						// not grounded, pushing on same wall as last wall jump, can double jump -->
						// double jump
						return new DoubleJumpAction();
					}
				} else if (targetPlayer.canDoubleJump()) {
					// not grounded, not WALL_FALLING, can double jump --> double jump
					return new DoubleJumpAction();				
				}
			}
		}

		return null;
	}
}
