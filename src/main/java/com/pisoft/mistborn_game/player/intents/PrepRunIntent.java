package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.PrepRunAction;

public class PrepRunIntent extends PlayerIntent {
	
	@Override
	public PlayerAction parseIntent() {
		return new PrepRunAction();
	}
}
