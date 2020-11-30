package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Player;

public abstract class GameEvent implements GameEventDispatcher, Cloneable {
	protected Player targetPlayer;

	private ArrayList<GameEventListener> listeners = new ArrayList<>();

	private int lagFrames;

	private long creationTime;
	private long validExecutionTime;

	public GameEvent() {
		long time = Game.getCurrentTime();

		setCreationTime(time);
		setValidExecutionTime(time);
	}

	public abstract void resolve();

	@Override
	public void dispatchEvent(GameEvent e) {

		e.setCreationTime(Long.MIN_VALUE);

		GameEventDispatcher.super.dispatchEvent(e);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		GameEvent clone = (GameEvent) super.clone();

		clone.setLagFrames(this.getLagFrames());

		// TODO: figure out how we want to handle timestamps
		clone.setCreationTime(Game.getCurrentTime());
		clone.setValidExecutionTime(this.getValidExecutionTime());
		
		return clone;
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}

	@Override
	public ArrayList<GameEventListener> getListeners() {
		return listeners;
	}

	public int getLagFrames() {
		return lagFrames;
	}

	public void setLagFrames(int lagFrames) {
		this.lagFrames = lagFrames;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getValidExecutionTime() {
		return validExecutionTime;
	}

	public void setValidExecutionTime(long validExecutionTime) {
		this.validExecutionTime = validExecutionTime;
	};
}
