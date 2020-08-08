package com.pisoft.mistborn_game.player.actions;

public class JumpReleaseAction extends PlayerAction {

	@Override
	public void resolve() {
		targetPlayer.setJumpReleased(true);
	}
}
