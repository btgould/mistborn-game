package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.CrouchAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.game_events.GameEvent;

public class CrouchIntent extends PlayerIntent {
	
	public CrouchIntent() {
		super();
	}
	
	@Override
	public void resolve() {
		if (targetPlayer.isGrounded()) {
			// grounded --> crouch
			dispatchEvent(new CrouchAction());
		}		
	}
	
	@Override
	public boolean isCompatible(GameEvent other) {
		if (other instanceof JumpIntent) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public Class<? extends PlayerAction> isEndedBy() {
		return StopCrouchIntent.class;
	}
}
