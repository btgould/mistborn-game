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

	// TODO: double check this one
	@Override
	public boolean isCompatible(PlayerAction action) {
		if (action instanceof WalkAction || action instanceof RunAction || action instanceof AirAccAction) {
			return false;
		}

		return true;
	}
}
