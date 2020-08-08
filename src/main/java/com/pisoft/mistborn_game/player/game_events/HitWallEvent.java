package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.Side;

public class HitWallEvent extends GameEvent {
	private Side direction;
	
	public HitWallEvent(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		targetPlayer.setAtWall(true);
		targetPlayer.setWallSide(direction);
		if (targetPlayer.wantsToAccelerate()) {
			targetPlayer.setWallPushing(true);
		}
		targetPlayer.setAccelerating(false);
		targetPlayer.setWalking(false);
		targetPlayer.setRunning(false);

		// how do I not set xAcc if hit wall on the other side
		targetPlayer.setxAcc(0);
		targetPlayer.setxSpeed(0);
	}

}
