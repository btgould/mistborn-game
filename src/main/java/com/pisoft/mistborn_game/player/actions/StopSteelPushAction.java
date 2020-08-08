package com.pisoft.mistborn_game.player.actions;

public class StopSteelPushAction extends PlayerAction {
	
	@Override
	public void resolve() {
		targetPlayer.setSteelPushing(false);
		targetPlayer.setTargetMetal(null);
		
		if (targetPlayer.isGrounded() && targetPlayer.getxSpeed() != 0) {
			targetPlayer.setSliding(true);
		}
	}
}
