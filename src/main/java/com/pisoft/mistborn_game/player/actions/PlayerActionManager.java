package com.pisoft.mistborn_game.player.actions;

import java.util.ArrayList;

import com.pisoft.mistborn_game.player.game_events.ConditionalGameEventQueue;
import com.pisoft.mistborn_game.player.game_events.GameEventListener;
import com.pisoft.mistborn_game.player.game_events.GameEventManager;

/**
 * Class to manage the resolution of a queue of <code>PlayerAction</code>s. It
 * contains a main queue, along with a set of buffers, each of which can have
 * its own condition on which to add events.
 * <p>
 * By default, there are two buffers used. One ensures that the action's valid
 * execution time has already passed using the <code>Game</code>'s current time,
 * and the other prevents any actions with target players that are lagging from
 * being executed.
 * <p>
 * During event resolution, each action is checked against the condition of
 * every active buffer. If it meets any of those conditions, it is not resolved,
 * but instead moved into the buffer until it no longer meets that condition. If
 * no buffer's condition is met, the action is resolved and removed from the
 * main queue.
 * 
 * @author gouldb
 */
public class PlayerActionManager extends GameEventManager<PlayerAction> implements GameEventListener {
	
	private static final int BUFFER_LENGTH = 5;

	/**
	 * Constructs a new <code>PlayerActionManager</code> with the default set of
	 * buffers: one for actions that have not reached their valid execution time
	 * yet, and one for actions with a target player that is lagging.
	 */
	public PlayerActionManager() {
		super(PlayerAction.class);

		queuedEvents.setComp((a1, a2) -> a1.compareTo(a2));

		buffers.add(new ConditionalGameEventQueue<PlayerAction>(
				a -> a.getTargetPlayer().getLagFrames() != 0 && !(a instanceof TransientAction)));
	}

	// action resolution methods
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Removes all conflicts in the same way as the standard
	 * {@link GameEventManager} does, but then also checks for pairs of start / stop
	 * actions that go together. If such a pair is found, and their target player is
	 * lagging a significant amount (as determined by <code>BUFFER_LENGTH</code>), the "start" action is removed from the queue.
	 */
	@Override
	protected void cleanUp() {
		super.cleanUp();

		ArrayList<PlayerAction> lagged = queuedEvents.filter(e -> e.getTargetPlayer().getLagFrames() >= BUFFER_LENGTH);
		ArrayList<PlayerAction> toRemove = new ArrayList<>();

		for (PlayerAction action : lagged) {
			Class<? extends PlayerAction> endAction = action.isEndedBy();

			for (PlayerAction end : lagged) {
				if (end.getClass() == endAction) {
					toRemove.add(action);
				}
			}
		}

		lagged.removeAll(toRemove);
		queuedEvents.addAll(lagged);
	}

	@Override
	protected void resolve(PlayerAction event) {
		System.out.println("Resolving action: " + event.getClass().getSimpleName());
		event.resolve();
		// TODO: this should be part of actions resolve method
		event.getTargetPlayer().setLagFrames(Math.max(event.getLagFrames(), event.getTargetPlayer().getLagFrames()));
	}
}
