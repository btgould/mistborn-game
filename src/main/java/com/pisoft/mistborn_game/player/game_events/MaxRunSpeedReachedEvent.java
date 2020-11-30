package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class MaxRunSpeedReachedEvent extends GameEvent {
	private Side direction;
	
	public MaxRunSpeedReachedEvent(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		int directionMultiplyer = (direction == Side.RIGHT) ? 1 : -1;

		targetPlayer.setxAcc(0);
		targetPlayer.setxSpeed(PlatformingConstants.getMaxRunSpeed() * directionMultiplyer);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		MaxRunSpeedReachedEvent clone = (MaxRunSpeedReachedEvent) super.clone();
		
		clone.direction = this.direction;
		
		return clone;
	}
}
