package com.pisoft.mistborn_game.player.actions;

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

	/**
	 * Natural ordering for this class is oldest first, unless either action is a
	 * "side effect". All side effect actions should be resolved immediately after
	 * the original action that created them, and are therefore "older" than any
	 * non-side effect action.
	 * <p>
	 * Older actions are catagorized as "less than" older actions.
	 */
	@Override
	public int compareTo(PlayerAction other) {
		if (this.isSideEffect() && other.isSideEffect()) {
			return Long.compare(this.getCreationTime(), other.getCreationTime());
		} else if (this.isSideEffect()) {
			return -1;
		} else if (other.isSideEffect()) {
			return 1;
		} else {
			return Long.compare(this.getCreationTime(), other.getCreationTime());
		}
	}

	/**
	 * Should be used to find the action (if any exists) that corresponds to
	 * "stopping" this action. This allows start / stop pairs of actions to be
	 * removed from queues entirely if a significant amount of time will pass before
	 * the player is ready to resolve actions again.
	 * <p>
	 * The default implementation returns <code>null</code>, but any subclass that
	 * wishes to communicate that it has a corresponding stop action should override
	 * it.
	 * 
	 * @return <code>null</code>, or the class of the action that will "stop" this
	 *         one, if it exists
	 */
	public Class<? extends PlayerAction> isEndedBy() {
		return null;
	}
}
