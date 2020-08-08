package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.player.Player;

public abstract class GameEvent {
	protected Player targetPlayer;
	
	private int lagFrames;
	
	public void resolve() {};
	
	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}

	public int getLagFrames() {
		return lagFrames;
	}

	public void setLagFrames(int lagFrames) {
		this.lagFrames = lagFrames;
	};
}
