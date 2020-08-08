package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class MaxWalkSpeedReachedEvent extends GameEvent {
	private Side direction;
	
	public MaxWalkSpeedReachedEvent(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		int directionMultiplyer = (direction == Side.RIGHT) ? 1 : -1;

		if (targetPlayer.getCanRun()) {
			targetPlayer.setWalking(false);
			targetPlayer.setRunning(true);
			targetPlayer.setxAcc(PlatformingConstants.getRunAcc() * directionMultiplyer);
		} else {
			targetPlayer.setxAcc(0);
			targetPlayer.setxSpeed(PlatformingConstants.getMaxWalkSpeed() * directionMultiplyer);
		}
	}
}
