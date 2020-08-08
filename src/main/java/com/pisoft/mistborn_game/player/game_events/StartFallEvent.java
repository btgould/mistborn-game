package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.actions.AirAccAction;
import com.pisoft.mistborn_game.player.actions.PlayerAction;

public class StartFallEvent extends GameEvent {
	@Override
	public void resolve() {
		targetPlayer.setFalling(true);
		targetPlayer.setCanJump(false);
		targetPlayer.setGrounded(false);
		targetPlayer.setCrouching(false);
		targetPlayer.setLanding(false);
		
		targetPlayer.setWalking(false);
		targetPlayer.setRunning(false);
		
		if (targetPlayer.wantsToAccelerate()) {
			PlayerAction sideEffect = new AirAccAction(targetPlayer.getFacingSide());
			sideEffect.setTargetPlayer(targetPlayer);
			sideEffect.resolve();
		}
	}
}
