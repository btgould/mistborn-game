package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.constants.GameEventLagConstants;
import com.pisoft.mistborn_game.player.constants.GameEventPriorityConstants;

public abstract class GameEvent implements GameEventDispatcher, Cloneable {
	protected Player targetPlayer;

	private ArrayList<GameEventListener> listeners = new ArrayList<>();

	private int lagFrames;
	private int priority;

	private long creationTime;
	private long validExecutionTime;

	private boolean isSideEffect;

	public GameEvent() {
		long time = Game.getCurrentTime();

		setCreationTime(time);
		setValidExecutionTime(time);
		setSideEffect(false);
		
		setPriority(GameEventPriorityConstants.getActionPriorities().getOrDefault(this.getClass(), 0));
		setLagFrames(GameEventLagConstants.getLagFrames().getOrDefault(this.getClass(), 0));
	}

	public abstract void resolve();

	// NOTE: I should override all dispatch methods to be safe
	@Override
	public void dispatchEvent(GameEvent e) {
		e.setSideEffect(true);

		GameEventDispatcher.super.dispatchEvent(e);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		GameEvent clone = (GameEvent) super.clone();

		clone.setLagFrames(this.getLagFrames());
		clone.setPriority(this.getPriority());

		// NOTE: figure out how we want to handle timestamps
		clone.setCreationTime(Game.getCurrentTime());
		clone.setValidExecutionTime(this.getValidExecutionTime());

		return clone;
	}

	/**
	 * Tests whether this event is compatible with another event (i.e. should be
	 * allowed to be queued at the same time.
	 * <p>
	 * Although generally events will not be dispatched under circumstances that
	 * would cause problems if they are resolved, no such guarantee can be made for
	 * events that are stored in a buffer for some time period (because of player
	 * lag, invalid execution times, etc.). Therefore, it is necessary to be able
	 * check if two events are compatible after they have already been dispatched.
	 * <p>
	 * By default, this method signals that this event is compatible with any other
	 * event. It should be overriden in any subclass where this is not true in order
	 * to achieve the desired behavior.
	 * 
	 * @param other The event to check against
	 * @return <code>true</code> if the events are compatible, <code>false</code>
	 *         otherwise
	 */
	public boolean isCompatible(GameEvent other) {
		return true;
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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
	}

	public boolean isSideEffect() {
		return isSideEffect;
	}

	public void setSideEffect(boolean isSideEffect) {
		this.isSideEffect = isSideEffect;
	};
}
