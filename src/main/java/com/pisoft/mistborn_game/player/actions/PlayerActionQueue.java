package com.pisoft.mistborn_game.player.actions;

import java.util.ArrayList;

/**
 * Thread-safe class to manage a queue of <code>PlayerAction</code> objects. It
 * has a main queue list, and a secondary lagBuffer list. While in the queue,
 * actions will always remain sorted in order of ascending creationTime.
 * <p>
 * The lagBuffer should be used to temporarily store actions that have a target
 * player that is lagging in a distinct place. To get these actions back, simply
 * call the <code>updateLagBuffer()</code> method. If the actions are ready to
 * be moved back (i.e. their target player is no longer lagging), then the
 * actions will be moved into the main queue. Otherwise, nothing will happen.
 * 
 * @author gouldb
 *
 */
public class PlayerActionQueue {

	private ArrayList<PlayerAction> queue = new ArrayList<>();
	private ArrayList<PlayerAction> lagBuffer = new ArrayList<>();

	/**
	 * Attempts to insert an action into the queue. The index where the action is
	 * inserted depends on its <code>creationTime</code>; this method will ensure
	 * that the queue remains sorted with the earliest <code>creationTime</code>
	 * coming first.
	 * 
	 * @param action The action to insert
	 * @return <code>true</code> if the action is actually added to the queue,
	 *         <code>false</code> if it is not.
	 */
	public boolean add(PlayerAction action) {
		boolean indexFound = false;
		int index = 0;

		synchronized (queue) {
			for (int i = 0; i < queue.size(); i++) {
				if (!indexFound && action.getCreationTime() < queue.get(i).getCreationTime()) {
					// mark index to insert action, stop searching
					index = i;
					indexFound = true;
				}

				// NOTE: add check for conflicting actions here, return false / remove from
				// queue if one is found
			}

			if (!indexFound) {
				// no element in queue was produced later --> insert at last index
				index = queue.size();
			}

			// if it gets here, action needs to be added
			queue.add(index, action);
			return true;
		}
	}

	/**
	 * Removes and returns the first element in the queue. If the queue is empty, an
	 * exception will be thrown.
	 * 
	 * @return The first element in the queue
	 */
	public PlayerAction deque() {
		synchronized (queue) {
			return queue.remove(0);
		}
	}

	/**
	 * Returns the size of the queue
	 * 
	 * @return the size of the queue
	 */
	public int size() {
		return queue.size();
	}

	/**
	 * Adds an action to a buffer meant to hold actions while their target player is
	 * lagging.
	 * 
	 * @param action The action to add
	 */
	public void addToLagBuffer(PlayerAction action) {
		synchronized (lagBuffer) {
			// NOTE: should I check for conflicting actions here?
			lagBuffer.add(action);
		}
	}

	/**
	 * Checks if the actions in the lag buffer have a target player that is no
	 * longer lagging. If they do, they are moved into the standard action queue.
	 */
	public void updateLagBuffer() {
		if (lagBuffer.size() > 0 && lagBuffer.get(0).getTargetPlayer().getLagFrames() == 0) {
			// buffer is ready to be emptied --> empty to queue
			synchronized (lagBuffer) {
				synchronized (queue) {
					for (PlayerAction action : lagBuffer) {
						this.add(action);
					}

					// NOTE: this assumes that all actions in the queue have the same target player
					lagBuffer.clear();
				}
			}
		}
	}
}
