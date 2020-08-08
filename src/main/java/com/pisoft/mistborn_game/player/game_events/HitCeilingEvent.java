package com.pisoft.mistborn_game.player.game_events;

public class HitCeilingEvent extends GameEvent {
	@Override
	public void resolve() {
		targetPlayer.setySpeed(0);
	}
}
