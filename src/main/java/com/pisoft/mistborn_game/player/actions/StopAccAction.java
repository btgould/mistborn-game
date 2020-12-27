package com.pisoft.mistborn_game.player.actions;

public class StopAccAction extends PlayerAction {

	public StopAccAction() {
		super();
	}

	@Override
	public void resolve() {
		targetPlayer.setWantsToAccelerate(false);
		targetPlayer.setAccelerating(false);
		targetPlayer.setWalking(false);
		targetPlayer.setRunning(false);
		if (Math.abs(targetPlayer.getxSpeed()) > 0 && targetPlayer.isGrounded()) {
			targetPlayer.setSliding(true);
		}

		targetPlayer.setxAcc(0);
	}
}
