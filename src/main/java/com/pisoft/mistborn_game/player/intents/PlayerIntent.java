package com.pisoft.mistborn_game.player.intents;

import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.actions.PlayerAction;

public abstract class PlayerIntent {
	protected Player targetPlayer;
	
	public PlayerAction parseIntent() {
		return null;
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
