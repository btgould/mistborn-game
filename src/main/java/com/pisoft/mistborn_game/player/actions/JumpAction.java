package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class JumpAction extends PlayerAction {
	
	public JumpAction() {
		super();
	}
	
	@Override
	public void resolve() {
		targetPlayer.setSliding(false);
		targetPlayer.setGrounded(false);
		targetPlayer.setLanding(false);
		targetPlayer.setWalking(false);
		targetPlayer.setRunning(false);
		targetPlayer.setJumping(true);
		targetPlayer.setDoubleJumping(false);
		targetPlayer.setWallJumping(false);
		targetPlayer.setFalling(true);
		targetPlayer.setCrouching(false);

		targetPlayer.setCanJump(false);
		targetPlayer.setJumpReleased(false);

		targetPlayer.setySpeed(PlatformingConstants.getFullJumpSpeed());
	}
	
	@Override
	public boolean isCompatible(PlayerAction action) {
		if (action instanceof CrouchAction) {
			return false;
		}
		
		return true;
	}
}
