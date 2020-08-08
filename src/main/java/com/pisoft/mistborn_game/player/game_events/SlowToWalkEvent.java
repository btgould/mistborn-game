package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class SlowToWalkEvent extends GameEvent {
	private Side direction;
	
	public SlowToWalkEvent(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		int directionMultiplyer = (direction == Side.RIGHT) ? 1 : -1;
		double slowDownAmount = PlatformingConstants.getSlowDownAmount();

		if (Math.abs(targetPlayer.getxSpeed()) - slowDownAmount < PlatformingConstants.getMaxWalkSpeed()) {
			slowDownAmount = Math.abs(targetPlayer.getxSpeed()) - PlatformingConstants.getMaxWalkSpeed();
		}

		targetPlayer.setxSpeed(targetPlayer.getxSpeed() - (slowDownAmount * directionMultiplyer));
	}

}
