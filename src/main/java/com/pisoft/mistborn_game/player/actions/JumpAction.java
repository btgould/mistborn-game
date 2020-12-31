package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class JumpAction extends PlayerAction {
	
	public JumpAction() {
		super();
	}
	
	@Override
	public void resolve() {
		targetPlayer.setJumping(true);
		targetPlayer.setDoubleJumping(false);
		targetPlayer.setWallJumping(false);
		targetPlayer.setCrouching(false);

		targetPlayer.setCanJump(false);
		targetPlayer.setJumpReleased(false);

		targetPlayer.setySpeed(PlatformingConstants.getFullJumpSpeed());
	}
}
