package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.StopPrepRunAction;

public class StopPrepRunIntent extends PlayerIntent {
	
	@Override
	public PlayerAction parseIntent() {
		return new StopPrepRunAction();
	}

}
