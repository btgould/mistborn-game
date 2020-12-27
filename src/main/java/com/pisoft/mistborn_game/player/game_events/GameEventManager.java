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
	 * First, all events that are ready to be removed from any of the buffers are
	 * harvested and added to the main queue. Then every element in the main queue
	 * is checked against each buffer's condition (this is needed because (a) new
	 * events could have been added since that last resolution cycle, and (b) an
	 * event could meet the condition of multiple buffers). Finally, any events that
	 * were not moved into a new buffer are resolved and removed from the main
	 * queue.
	 */
	public void resolveQueuedEvents() {
		// check if any delayed events are ready to be resolved
		for (ConditionalGameEventQueue<T> buffer : getBuffers()) {
			queuedEvents.addAll(buffer.harvest());
		}

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
