package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Class to store a queue of events that all meet some condition. It implements
 * all functionality of a standard <code>GameEventQueue</code>, but also
 * provides methods for testing its own elements against some condition.
 * <p>
 * It is reccommended that events should first meet this queue's condition
 * before being added to it, but this is not enforced, and so it is possible to
 * add events to this queue that do not strictly belong in it. Due to the nature
 * of some conditions, (i.e. time based ones), it is even expected that at some
 * points this queue will contain elements that do not meet its condition, and
 * therefore additional functionality to detect and remove these elements is
 * also provided.
 * 
 * @author gouldb
 *
 * @param <T>
 */
public class ConditionalGameEventQueue<T extends GameEvent> extends GameEventQueue<T> {

	private Predicate<T> condition;

	/**
	 * Constructs a new <code>ConditionalGameEventQueue</code> with the specified
	 * condition and default sorting behavior.
	 * 
	 * @param condition The condition to be associated with this queue
	 */
	public ConditionalGameEventQueue(Predicate<T> condition) {
		this.condition = condition;
	}

	/**
	 * Constructs a new <code>ConditionalGameEventQueue</code> with the specified
	 * condition and sorting behavior.
	 * 
	 * @param comp      The comparator to use when sorting the queue
	 * @param condition The condition to be associated with this queue
	 */
	public ConditionalGameEventQueue(Comparator<T> comp, Predicate<T> condition) {
		super(comp);
		this.condition = condition;
	}

	/**
	 * Creates a new <code>ConditionalGameEventQueue</code> with the same condition
	 * and sorting behavior as another queue.
	 * 
	 * @param other
	 */
	public ConditionalGameEventQueue(ConditionalGameEventQueue<T> other) {
		setComp(other.getComp());
		setCondition(other.getCondition());
	}

	/**
	 * Checks if an event belongs in this queue.
	 * 
	 * @param event The event to check
	 * @return <code>true</code> if the event meets this queue's condition,
	 *         <code>false</code> otherwise
	 */
	public boolean test(T event) {
		return condition.test(event);
	}

	/**
	 * Removes and returns the set of all elements that no longer belong in this
	 * queue (i.e. those that do not meet its condition).
	 * 
	 * @return The set of elements in the queue that no longer meet its condition
	 */
	public ArrayList<T> harvest() {
		return filter(condition.negate());
	}

	// getters / setters
	// -----------------------------------------------------------------------------------------------
	public Predicate<T> getCondition() {
		return condition;
	}

	public void setCondition(Predicate<T> condition) {
		this.condition = condition;
	}

}
