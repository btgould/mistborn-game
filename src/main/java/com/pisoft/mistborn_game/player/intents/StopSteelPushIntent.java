package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.StopSteelPushAction;

public class StopSteelPushIntent extends PlayerIntent {
	
	@Override
	public PlayerAction parseIntent() {
		return new StopSteelPushAction();
	}
}
