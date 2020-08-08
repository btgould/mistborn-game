package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class WallJumpAction extends PlayerAction {
	@Override
	public void resolve() {
		targetPlayer.setSliding(false);
		targetPlayer.setGrounded(false);
		targetPlayer.setLanding(false);
		targetPlayer.setJumping(false);
		targetPlayer.setDoubleJumping(false);
		targetPlayer.setWallJumping(true);
		targetPlayer.setFalling(true);
		targetPlayer.setCrouching(false);
		targetPlayer.setAtWall(false);
		targetPlayer.setWallPushing(false);

		targetPlayer.setLastWallJumpSide(targetPlayer.getWallSide());
		targetPlayer.setJumpReleased(false);

		targetPlayer.setxSpeed((targetPlayer.getWallSide() == Side.LEFT) ? PlatformingConstants.getWallJumpXSpeed()
				: -PlatformingConstants.getWallJumpXSpeed());
		targetPlayer.setySpeed(PlatformingConstants.getWallJumpYSpeed());
		
		Side facingSide = (targetPlayer.getLastWallJumpSide() == Side.LEFT) ? Side.RIGHT : Side.LEFT;
		targetPlayer.setFacingSide(facingSide);
		
		// TODO: add lag frames  / buffer for this
		if (targetPlayer.wantsToAccelerate()) {
			PlayerAction sideEffect = new AirAccAction(targetPlayer.getLastWallJumpSide());
			sideEffect.setTargetPlayer(targetPlayer);
			sideEffect.resolve();
		}
	}
}
