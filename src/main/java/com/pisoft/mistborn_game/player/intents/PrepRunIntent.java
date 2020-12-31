package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.PrepRunAction;

public class PrepRunIntent extends PlayerIntent {
	
	public PrepRunIntent() {
		super();
	}
	
	@Override
	public void resolve() {
		dispatchEvent(new PrepRunAction());
	}
	
	@Override
	public Class<? extends PlayerAction> isEndedBy() {
		return StopPrepRunIntent.class;
	}
}
