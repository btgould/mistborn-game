package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.StopSteelPushAction;

public class StopSteelPushIntent extends PlayerIntent {
	
	public StopSteelPushIntent() {
		super();
	}
	
	@Override
	public void resolve() {
		dispatchEvent(new StopSteelPushAction());
	}
}
