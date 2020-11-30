package com.pisoft.mistborn_game.player.actions;

public class JumpReleaseAction extends PlayerAction {
	public JumpReleaseAction() {
		super();
	}

	@Override
	public void resolve() {
		targetPlayer.setJumpReleased(true);
	}
}
