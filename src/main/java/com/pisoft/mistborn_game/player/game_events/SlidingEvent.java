package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class SlidingEvent extends GameEvent {
	@Override
	public void resolve() {
		targetPlayer.setxSpeed(targetPlayer.getxSpeed() * PlatformingConstants.getFriction());

		if (Math.abs(targetPlayer.getxSpeed()) <= 1) {
			targetPlayer.setxSpeed(0);
			targetPlayer.setSliding(false);
		}
	}
}
