package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.constants.PlayerActionLagConstants;
import com.pisoft.mistborn_game.player.constants.PlayerActionPriorityConstants;
import com.pisoft.mistborn_game.player.game_events.GameEvent;

/**
 * Abstract class encapsulating actions that can be performed by the player.
 * <p>
 * Note: This class has a natural ordering that is inconsistent with
 * <code>equals()</code>
 * 
 * @author gouldb
 *
 */
public abstract class PlayerAction extends GameEvent implements Comparable<PlayerAction> {

	private int priority;

	public PlayerAction() {
		super();
		
		setPriority(PlayerActionPriorityConstants.getActionPriorities().getOrDefault(this.getClass(), 0));
		setLagFrames(PlayerActionLagConstants.getLagFrames().getOrDefault(this.getClass(), 0));
	}

	/**
	 * Tests whether this action is compatible with another action. Despite intents
	 * only dispatching actions if the player state is valid for that action, it is
	 * possible for actions that should be mutually exclusive to be in the queue at
	 * the same time (e.g. if multiple actions are queued while the player is
	 * lagging).
	 * <p>
	 * This method can be used to ensure that no conflicting actions are ever queued
	 * together, by checking against the existing queue before adding a new action.
	 * 
	 * @param action The action to be checked against.
	 * @return <code>true</code> if the actions are compatible, <code>false</code> otherwise.
	 * 
	 */
	public boolean isCompatible(PlayerAction action) {
		return true;
	}

	/**
	 * Natural ordering for this class is newest first.
	 * <p>
	 * Newer actions are catagorized as "less than" older actions. 
	 * 
	 */
	@Override
	public int compareTo(PlayerAction other) {	
		if (this.getCreationTime() == other.getCreationTime()) {
			return 0;
		}
		
		return (this.getCreationTime() < other.getCreationTime()) ? -1 : 1;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		PlayerAction clone = (PlayerAction) super.clone();
		
		clone.setPriority(this.getPriority());
		
		return clone;
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
