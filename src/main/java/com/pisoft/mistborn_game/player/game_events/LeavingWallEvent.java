package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.actions.AirAccAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;

public class LeavingWallEvent extends GameEvent {
	private Side direction;
	
	public LeavingWallEvent(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		targetPlayer.setAtWall(false);
		targetPlayer.setWallSide(Side.NONE);

		if (targetPlayer.wantsToAccelerate()) {
			PlayerAction sideEffect = new AirAccAction(direction);
			sideEffect.setTargetPlayer(targetPlayer);
			sideEffect.resolve();
		}
	}
}
