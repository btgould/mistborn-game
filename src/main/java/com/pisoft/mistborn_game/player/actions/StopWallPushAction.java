package com.pisoft.mistborn_game.player.actions;

public class StopWallPushAction extends PlayerAction {

	@Override
	public void resolve() {
		targetPlayer.setWantsToAccelerate(false);
		targetPlayer.setWallPushing(false);
	}
}
