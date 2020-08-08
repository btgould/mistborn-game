package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.game_events.GameEvent;

public abstract class PlayerAction extends GameEvent {
	
	private int priority;
		
	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
