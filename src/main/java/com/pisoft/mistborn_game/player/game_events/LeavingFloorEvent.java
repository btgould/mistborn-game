package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.actions.AirAccAction;

// TODO: does not prevent SlidingEvent on the same frame when sliding off ledge 
public class LeavingFloorEvent extends GameEvent {
	@Override
	public void resolve() {
		targetPlayer.setFalling(true);
		targetPlayer.setCanJump(false);
		targetPlayer.setGrounded(false);
		targetPlayer.setCrouching(false);
		targetPlayer.setLanding(false);
		
		targetPlayer.setWalking(false);
		targetPlayer.setRunning(false);
		targetPlayer.setSliding(false);
		
		if (targetPlayer.wantsToAccelerate()) {
			dispatchEvent(new AirAccAction(targetPlayer.getFacingSide()));
		}
	}
	
	@Override
	public boolean isCompatible(GameEvent other) {
		if (other instanceof FinishLandingEvent) {
			return false;
		}
		
		return true;
	}
}
