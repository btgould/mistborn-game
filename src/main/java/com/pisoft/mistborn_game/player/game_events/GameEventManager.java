package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;

import com.pisoft.mistborn_game.Game;

/**
 * Class to manage the resolution of a queue of <code>GameEvent</code>s. It
 * contains a main queue, along with a set of buffers, each of which can have
 * its own condition on which to add events.
 * <p>
 * By default, the only buffer used ensures that the event's valid execution
 * time has already passed using the <code>Game</code>'s current time.
 * <p>
 * During event resolution, each event is checked against the condition of every
 * active buffer. If it meets any of those conditions, it is not resolved, but
 * instead moved into the buffer until it no longer meets that condition. If no
 * buffer's condition is met, the event is resolved and removed from the main
 * queue.
 * 
 * @author gouldb
 */
public class GameEventManager<T extends GameEvent> implements GameEventListener {

	protected GameEventQueue<T> queuedEvents = new GameEventQueue<>();
	protected ArrayList<ConditionalGameEventQueue<T>> buffers = new ArrayList<>();

	private Class<T> cls;

	/**
	 * Constructs a new <code>GameEventManager</code> with the default buffer for
	 * events whose valid execution time has not yet passed.
	 * <p>
	 * A class object of the type that this queue is meant to store must be passed
	 * as an argument in order to allow runtime casting to a generic type. It should
	 * be the same as the type declared in the generic parameter, otherwise,
	 * undefined behavior may occur.
	 */
	public GameEventManager(Class<T> cls) {
		getBuffers().add(new ConditionalGameEventQueue<T>(
				(e1, e2) -> Long.compare(e1.getValidExecutionTime(), e2.getValidExecutionTime()),
				e -> Game.getCurrentTime() < e.getValidExecutionTime()));

		this.cls = cls;
	}

	// event resolution methods
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Determines the appropriate way to handle each event in this manager's queues
	 * and does so.
	 * <p>
	 * First, some clean up actions are performed. By default, this consists of
	 * removing the least important set of events for every conflict in the manager,
	 * but can be overriden to include other things as well. Next, each event is
	 * checked against every buffer's condition. If it meets any condition, it is
	 * added into the first buffer whose condition it meets. If it does not meet any
	 * conditions, it is resolved and removed from the manager.
	 */
	public void resolveQueuedEvents() {

		cleanUp();

		// handle each event in the main queue
		mainLoop: while (queuedEvents.size() > 0) {
			T event = queuedEvents.deque();

			// check event against each buffer's condition
			for (ConditionalGameEventQueue<T> buffer : getBuffers()) {
				if (buffer.test(event)) {
					buffer.add(event);
					continue mainLoop;
				}
			}

			// if it gets here, event can be resolved --> resolve it
			resolve(event);
		}
	}

	/**
	 * Prepares the manager for event resolution. If any special conditions should
	 * be checked before resolution, the subclass that wishes to check them should
	 * override this method.
	 * <p>
	 * By default, ensures that no event that is ready to be resolved has any
	 * conflicts present in this manager.
	 * <p>
	 * For each event about to be resolved, the entire manager (including buffers)
	 * is scanned, and every conflicting event is found. Then, the priority of the
	 * event is compared to that of its conflicts. If any conflict has a strictly
	 * higher priority than the event about to be resolved, the event is removed
	 * from the manager. Otherwise, all conflicts are removed from the manager.
	 * <p>
	 * In order to efficiently scan across all buffers, buffered events are removed
	 * from whatever buffer they were in and moved into a temporary list. The final
	 * version of this list (with all conflicts removed) is then added back into the
	 * main queue. This means that after calling this method, there will be events
	 * in the main queue that actually belong in a buffer. In practice, this should
	 * not be a problem, because events are always checked for buffer conditions
	 * before resolution anyways.
	 */
	protected void cleanUp() {
		// move all events into centralized location
		ArrayList<T> events = new ArrayList<>();

		for (ConditionalGameEventQueue<T> buffer : getBuffers()) {
			queuedEvents.addAll(buffer.harvest()); // any events ready to be resolved go to main queue
			events.addAll(buffer.filter(e -> true)); // else go to general list
		}

		events.addAll(queuedEvents.find(e -> true)); // keep list of events about to be resolved

		// check conflicts for all events that are about to be resolved
		// NOTE: I think this might be wrong for events that will go into a buffer but
		// are on their first cycle in the manager.
		while (queuedEvents.size() > 0) {
			// get event to process
			T event = queuedEvents.deque();
			ArrayList<T> conflicts = new ArrayList<T>();

			// find all conflicts of processed event
			for (T queued : events) {
				if (!event.isCompatible(queued)) {
					conflicts.add(queued);
				}
			}

			boolean eventImportant = true;

			// if any conflict is more important than the event, event should be disarded
			for (T conflict : conflicts) {
				if (conflict.getPriority() > event.getPriority()) {
					eventImportant = false;
					break;
				}
			}

			if (eventImportant) {
				// event is most important --> keep it, discard conflicts
				events.removeAll(conflicts);
			} else {
				// event is not as important as conflicts --> discard it
				events.remove(event);
			}
		}

		// add all resulting processed events back into queue
		queuedEvents.addAll(events);
	}

	/**
	 * Default behavior to use when resolving an event.
	 * 
	 * @param event The event to resolve
	 */
	protected void resolve(T event) {
		System.out.println("Resolving event: " + event.getClass().getSimpleName());
		event.resolve();
	}

	/**
	 * Adds an event to the main queue.
	 */
	@Override
	public void receiveGameEvent(GameEvent event) {
		if (event != null) {
			queuedEvents.add(cls.cast(event));
		}
	}

	// buffer management methods
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Adds a buffer to this manager's list
	 * 
	 * @param buffer The buffer to add
	 */
	public void addBuffer(ConditionalGameEventQueue<T> buffer) {
		getBuffers().add(buffer);
	}

	/**
	 * Removes a buffer from this manager's list
	 * 
	 * @param buffer The buffer to remove
	 */
	public void removeBuffer(ConditionalGameEventQueue<T> buffer) {
		getBuffers().remove(buffer);
	}

	/**
	 * Removes the buffer at the specified index from this manager's list
	 * 
	 * @param index Index to remove at
	 */
	public void removeBuffer(int index) {
		getBuffers().remove(index);
	}

	/**
	 * Removes all buffers from this manager.
	 * <p>
	 * Note: this will remove ALL buffers, including default ones. Because of this,
	 * events in this manager will always be executed the cycle after they have been
	 * added. This could cause confusing / undesired behavior, so use this method
	 * with caution.
	 */
	public void clearBuffers() {
		getBuffers().clear();
	}

	/**
	 * Gets the list of buffers currently in use by this manager
	 * 
	 * @return This manager's list of buffers
	 */
	public ArrayList<ConditionalGameEventQueue<T>> getBuffers() {
		return buffers;
	}

	/**
	 * Sets the list of buffers this manager uses
	 * 
	 * @param buffers The new set of buffers to use
	 */
	public void setBuffers(ArrayList<ConditionalGameEventQueue<T>> buffers) {
		this.buffers = buffers;
	}
}
