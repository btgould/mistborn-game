package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.actions.PlayerActionDispatcher;
import com.pisoft.mistborn_game.player.Player;

public class IntentParser implements IntentListener, PlayerActionDispatcher {

	private Player targetPlayer;

	// intent object parsing method
	// ---------------------------------------------------------------------------------------------------
	@Override
	public void receiveIntent(PlayerIntent intent) {
		intent.setTargetPlayer(targetPlayer);
		dispatchAction(intent.parseIntent());
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}
}
