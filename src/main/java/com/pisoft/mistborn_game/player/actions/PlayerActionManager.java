package com.pisoft.mistborn_game.player.actions;

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
	@Override
	protected void resolve(PlayerAction event) {
		System.out.println("Resolving action: " + event.getClass().getSimpleName());
		event.resolve();
		// TODO: this should be part of actions resolve method
		event.getTargetPlayer()
				.setLagFrames(Math.max(event.getLagFrames(), event.getTargetPlayer().getLagFrames()));
	}
}
