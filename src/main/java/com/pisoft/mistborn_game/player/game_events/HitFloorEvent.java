package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;
import com.pisoft.mistborn_game.player.intents.AccelerateIntent;

// TODO: walk / run action side effect
public class HitFloorEvent extends GameEvent {
	@Override 
	public void resolve() {
		targetPlayer.setGrounded(true);
		targetPlayer.setLanding(true);
		targetPlayer.setJumping(false);
		targetPlayer.setCanJump(true);
		targetPlayer.setDoubleJumping(false);
		targetPlayer.setCanDoubleJump(true);
		targetPlayer.setWallJumping(false);
		targetPlayer.setLastWallJumpSide(Side.NONE);
		
		targetPlayer.setFalling(false);

		if (targetPlayer.getxSpeed() != 0) {
			if (!targetPlayer.wantsToAccelerate()) {
				// not accelerating, speed > 0 --> sliding
				targetPlayer.setSliding(true);
			} else {
				dispatchEvent(new AccelerateIntent(targetPlayer.getFacingSide()), Game.getPlayerActionManager());
			}
		}

		targetPlayer.setySpeed(0);
		
		dispatchEvent(new FinishLandingEvent(), PlatformingConstants.getLandingDelay());
	}
}
