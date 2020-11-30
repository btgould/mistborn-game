package com.pisoft.mistborn_game.player.game_events;

public class JumpPeakedEvent extends GameEvent {
	
	@Override
	public void resolve() {
		targetPlayer.setJumping(false);
		targetPlayer.setDoubleJumping(false);
		targetPlayer.setWallJumping(false);
	}
}
