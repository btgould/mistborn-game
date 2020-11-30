package com.pisoft.mistborn_game.player.actions;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class provides methods to easily manage a queue of
 * <code>PlayerAction</code> objects. It extends
 * <code>PriorityBlockingQueue</code>, and therefore has all of the basic
 * functionality of a collection, but has special conveniences to manage a queue
 * of actions.
 * 
 * @author gouldb
 *
 */
public class PlayerActionQueue extends PriorityBlockingQueue<PlayerAction> {

	private static final long serialVersionUID = 1L;

	/**
	 * Inserts an action into the queue.
	 * 
	 * @param action The action to be added to the queue
	 * @return <code>true</code> if the action is successfully added to the queue,
	 *         <code>false</code> otherwise.
	 * 
	 */
	@Override
	public boolean add(PlayerAction action) {
		if (action != null) {
			super.add(action);

			return true;
		} else {
			return false;
		}
	}
}
