package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class DoubleJumpAction extends PlayerAction {
	
	public DoubleJumpAction() {
		super();
	}
	
	@Override
	public void resolve() {
		targetPlayer.setSliding(false);
		targetPlayer.setGrounded(false);
		targetPlayer.setLanding(false);
		targetPlayer.setJumping(false);
		targetPlayer.setDoubleJumping(true);
		targetPlayer.setWallJumping(false);
		targetPlayer.setFalling(true);
		targetPlayer.setCrouching(false);

		targetPlayer.setCanDoubleJump(false);
		targetPlayer.setJumpReleased(false);

		targetPlayer.setySpeed(PlatformingConstants.getDoubleJumpSpeed());
	}
}
