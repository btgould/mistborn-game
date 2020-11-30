package com.pisoft.mistborn_game.player.game_events;

public class FinishLandingEvent extends GameEvent {
	@Override
	public void resolve() {
		targetPlayer.setLanding(false);
	}
}
