package com.pisoft.mistborn_game.player.actions;

public class StopWallPushAction extends PlayerAction {
	
	public StopWallPushAction() {
		super();
	}

	@Override
	public void resolve() {
		targetPlayer.setWantsToAccelerate(false);
		targetPlayer.setWallPushing(false);
	}
	
	@Override
	public boolean isCompatible(PlayerAction action) {
		if (action instanceof WallPushAction) {
			return false;
		}
		
		return true;
	}
}
