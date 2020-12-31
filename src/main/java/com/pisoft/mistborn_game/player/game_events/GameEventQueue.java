package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Thread safe class to manage a queue of <code>GameEvent</code> objects.
 * <p>
 * Each queue uses a <code>Comparator</code> in order to sort its elements, and
 * the queue will remain sorted in whatever order is specified.
 * 
 * @author gouldb
 *
 * @param <T> The type of <code>GameEvent</code>s to store
 */
public class GameEventQueue<T extends GameEvent> {
	protected ArrayList<T> queue = new ArrayList<>();
	private Comparator<T> comp;

	/**
	 * Constructs a new <code>GameEventQueue</code> with the default
	 * <code>Comparator</code>. Elements will be sorted in ascending order by their
	 * valid execution times.
	 */
	public GameEventQueue() {
		this.comp = (e1, e2) -> Long.compare(e1.getValidExecutionTime(), e2.getValidExecutionTime());
	}

	/**
	 * Constructs a new <code>GameEventQueue</code> with the specified
	 * <code>Comparator</code>. Elements will be sorted in ascending order by the
	 * given <code>Comparator</code>.
	 * 
	 * @param comp The <code>Comparator</code> to use when sorting the list
	 */
	public GameEventQueue(Comparator<T> comp) {
		this.comp = comp;
	}

	/**
	 * Adds an element to the queue.
	 * <p>
	 * Whenever an element is added, it is inserted at the necessary position to
	 * ensure the queue remains sorted by whatever order this queue's
	 * <code>Comparator</code> specifies.
	 * 
	 * @param event The event to insert
	 */
	public void add(T event) {
		boolean indexFound = false;
		int index = 0;

		synchronized (queue) {
			for (int i = 0; i < queue.size(); i++) {
				if (!indexFound && comp.compare(event, queue.get(i)) < 0) {
					// mark index to insert, stop searching
					index = i;
					indexFound = true;
				}
			}

			// no element in queue is "greater" than this one --> insert at last index
			if (!indexFound) {
				index = queue.size();
			}

			// if it gets here, event needs to be added
			queue.add(index, event);
		}
	}

	/**
	 * Removes and returns the first element in the queue
	 * 
	 * @return The first element in the queue
	 */
	public T deque() {
		synchronized (queue) {
			return queue.remove(0);
		}
	}

	/**
	 * Returns as an <code>ArrayList</code> the set of all elements in the queue
	 * that meet some condition.
	 * 
	 * @param cond The condition to check elements by
	 * @return The set of all elements that meet the condition
	 */
	public ArrayList<T> find(Predicate<T> cond) {
		ArrayList<T> ret = new ArrayList<>();

		synchronized (queue) {
			for (T event : queue) {
				if (cond.test(event)) {
					ret.add(event);
				}
			}
		}

		return ret;
	}

	/**
	 * Removes and returns as an <code>ArrayList</code> the set of all elements in
	 * the queue that meet some condition.
	 * 
	 * @param cond The condition on which to remove elements
	 * @return The set of all elements that meet the condition
	 */
	public ArrayList<T> filter(Predicate<T> cond) {
		ArrayList<T> ret = find(cond);

		synchronized (queue) {
			queue.removeAll(ret);
		}

		return ret;
	}

	/**
	 * Adds all elements in the given collection to the queue.
	 * 
	 * @param c The collection of elements to add
	 */
	public void addAll(Collection<? extends T> c) {
		synchronized (queue) {
			for (T event : c) {
				add(event);
			}
		}
	}

	/**
	 * Removes all elements in the given collection from the queue.
	 * 
	 * @param c The collection of elements to remove
	 */
	public void removeAll(Collection<? extends T> c) {
		synchronized (queue) {
			for (T event : c) {
				queue.remove(event);
			}
		}
	}

	/**
	 * Gets the current size of this queue
	 * 
	 * @return The size of the queue
	 */
	public int size() {
		synchronized (queue) {
			return queue.size();
		}
	}

	// getters / setters
	// -----------------------------------------------------------------------------------------------
	/**
	 * Gets the current <code>Comparator</code> used to sort this queue.
	 * 
	 * @return The <code>Comparator</code> this queue is using
	 */
	public Comparator<T> getComp() {
		return comp;
	}

	/**
	 * Gets the current <code>Comparator</code> used to sort this queue. Also sorts
	 * the queue using this new comparator.
	 * 
	 * @param comp The new <code>Comparator</code> to use in this queue
	 */
	public void setComp(Comparator<T> comp) {
		this.comp = comp;

		synchronized (queue) {
			queue.sort(comp);
		}
	}
}
