package com.pisoft.mistborn_game.player.actions;

public class StopSteelPushAction extends PlayerAction {
	
	public StopSteelPushAction() {
		super();
	}
	
	@Override
	public void resolve() {
		targetPlayer.setSteelPushing(false);
		targetPlayer.setTargetMetal(null);
		
		if (targetPlayer.isGrounded() && targetPlayer.getxSpeed() != 0) {
			targetPlayer.setSliding(true);
		}
	}
	
	@Override
	public boolean isCompatible(PlayerAction action) {
		if (action instanceof SteelPushAction) {
			return false;
		}
		
		return true;
	}

}
