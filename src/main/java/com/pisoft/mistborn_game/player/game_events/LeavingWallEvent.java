package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.intents.AccelerateIntent;

public class LeavingWallEvent extends GameEvent {
	private Side direction;
	
	public LeavingWallEvent(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		targetPlayer.setAtWall(false);
		targetPlayer.setWallPushing(false);
		targetPlayer.setWallSide(Side.NONE);

		if (targetPlayer.wantsToAccelerate() && targetPlayer.getFacingSide() == direction) {
			dispatchEvent(new AccelerateIntent(direction), Game.getPlayerActionManager());
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		LeavingWallEvent clone = (LeavingWallEvent) super.clone();
		
		clone.direction = this.direction;
		
		return clone;
	}
}
