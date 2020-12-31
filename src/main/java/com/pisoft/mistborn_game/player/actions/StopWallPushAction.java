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
}
