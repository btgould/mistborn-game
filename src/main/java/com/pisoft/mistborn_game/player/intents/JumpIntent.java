package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.DoubleJumpAction;
import com.pisoft.mistborn_game.player.actions.JumpAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.WallJumpAction;
import com.pisoft.mistborn_game.player.game_events.GameEvent;

public class JumpIntent extends PlayerIntent {
	
	public JumpIntent() {
		super();
	}

	@Override
	public void resolve() {
		// only accept jump intent if player has released jump button since last jump
		if (targetPlayer.isJumpReleased()) {
			if (targetPlayer.isGrounded()) {
				if (!targetPlayer.isCrouching() && targetPlayer.canJump()) {
					// grounded, not crouching, can jump --> jump
					dispatchEvent(new JumpAction());
				}
			} else {
				if (targetPlayer.isWallPushing()) {
					if (targetPlayer.getWallSide() != targetPlayer.getLastWallJumpSide()) {
						// not grounded, pushing on opposite wall as last wall jump --> wall jump
						dispatchEvent(new WallJumpAction());
					} else if (targetPlayer.canDoubleJump()) {
						// not grounded, pushing on same wall as last wall jump, can double jump -->
						// double jump
						dispatchEvent(new DoubleJumpAction());
					}
				} else if (targetPlayer.canDoubleJump()) {
					// not grounded, not WALL_FALLING, can double jump --> double jump
					dispatchEvent(new DoubleJumpAction());				
				}
			}
		}
	}
	
	@Override
	public boolean isCompatible(GameEvent other) {
		if (other instanceof CrouchIntent) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public Class<? extends PlayerAction> isEndedBy() {
		return StopJumpIntent.class;
	}
}
