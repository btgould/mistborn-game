package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.StopPrepRunAction;

public class StopPrepRunIntent extends PlayerIntent {
	
	public StopPrepRunIntent() {
		super();
	}
	
	@Override
	public void resolve() {
		dispatchEvent(new StopPrepRunAction());
	}
}
