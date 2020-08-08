package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class HitFloorEvent extends GameEvent {
	@Override 
	public void resolve() {
		targetPlayer.setGrounded(true);
		targetPlayer.setLanding(true);
		targetPlayer.setCanJump(true);
		targetPlayer.setCanDoubleJump(true);
		targetPlayer.setLastWallJumpSide(Side.NONE);
		targetPlayer.setFalling(false);

		if (targetPlayer.getxSpeed() != 0) {
			if (!targetPlayer.isAccelerating()) {
				// not accelerating, speed > 0 --> sliding
				targetPlayer.setSliding(true);
			} else if (targetPlayer.getxSpeed() > PlatformingConstants.getMaxWalkSpeed() && targetPlayer.getCanRun()) {
				// accelerating, speed > maxWalkSpeed, canRun --> running
				targetPlayer.setRunning(true);
				targetPlayer.setWalking(false);
			} else {
				// acclerating, speed <= maxWalkSpeed or can't run --> walking
				targetPlayer.setRunning(false);
				targetPlayer.setWalking(true);
			}
		}

		targetPlayer.setySpeed(0);
	}
}
