package com.pisoft.mistborn_game.player.actions;

import java.util.ArrayList;
import java.util.Comparator;

import com.pisoft.mistborn_game.player.game_events.GameEventQueue;

/**
 * Thread-safe class to manage a queue of <code>PlayerAction</code> objects.
 * <p>
 * Each queue uses a <code>Comparator</code> in order to sort its elements, and
 * the queue will remain sorted in whatever order is specified.
 * 
 * @author gouldb
 *
 */
public class PlayerActionQueue extends GameEventQueue<PlayerAction> {

	/**
	 * Constructs a new <code>PlayerActionQueue</code> with the default
	 * <code>Comparator</code>. Elements will be sorted in ascending order by their
	 * creation times.
	 */
	public PlayerActionQueue() {
		setComp((e1, e2) -> e1.compareTo(e2));
	}

	/**
	 * Constructs a new <code>PlayerActionQueue</code> with the specified
	 * <code>Comparator</code>. Elements will be sorted in ascending order by the
	 * given <code>Comparator</code>.
	 * 
	 * @param comp The <code>Comparator</code> to use when sorting the list
	 */
	public PlayerActionQueue(Comparator<PlayerAction> comp) {
		super(comp);
	}

	/**
	 * Attempts to add an element to the queue.
	 * <p>
	 * Before adding, each element in the queue is checked to make sure that no
	 * conflicting actions can be queued at the same time. If a conflicting action
	 * is found, the priority of each action is then checked. If the old action has
	 * a higher priority, the new one is not added. Otherwise, the old action is
	 * removed from the queue before adding the new one.
	 * <p>
	 * If the element is successfully added, it is inserted at the necessary
	 * position to ensure the queue remains sorted by whatever order this queue's
	 * <code>Comparator</code> specifies.
	 * 
	 * @param action The action to insert
	 * @return <code>true</code> if the action is actually added to the queue,
	 *         <code>false</code> if it is not.
	 */
	@Override
	public boolean add(PlayerAction action) {
		boolean indexFound = false;
		int index = 0;
		ArrayList<PlayerAction> toRemove = new ArrayList<>();

		synchronized (queue) {
			for (int i = 0; i < queue.size(); i++) {
				PlayerAction queued = queue.get(i);

				if (!indexFound && getComp().compare(action, queued) < 0) {
					// mark index to insert action, stop searching
					index = i;
					indexFound = true;
				}

				// NOTE: it would be nice to only override part of this method
				// check for incompatible actions before adding
				if (!action.isCompatible(queued)) {
					if (queued.getPriority() > action.getPriority()) {
						// queued action has higher priority --> don't add new action
						return false;
					} else {
						// new action has higher priority --> remove queued action
						toRemove.add(queued);
					}
				}
			}

			// remove all less important conflicting actions
			queue.removeAll(toRemove);

			if (!indexFound) {
				// no element in queue was produced later --> insert at last index
				index = queue.size();
			}

			// if it gets here, action needs to be added
			queue.add(index, action);
			return true;
		}
	}
}
