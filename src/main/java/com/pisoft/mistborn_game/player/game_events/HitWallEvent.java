package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.intents.AccelerateIntent;

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

		// TODO: how do I not set xAcc if hit wall on the other side
		targetPlayer.setxAcc(0);
		targetPlayer.setxSpeed(0);
		
		if (targetPlayer.wantsToAccelerate() && targetPlayer.getFacingSide() != direction) {
			dispatchEvent(new AccelerateIntent(targetPlayer.getFacingSide()), Game.getPlayerActionManager());
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		HitWallEvent clone = (HitWallEvent) super.clone();
		
		clone.direction = this.direction;
		
		return clone;
	}
}
