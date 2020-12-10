package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.game_events.GameEvent;
import com.pisoft.mistborn_game.player.game_events.GameEventListener;

public class PlayerActionManager implements GameEventListener {

	private PlayerActionQueue queuedActions = new PlayerActionQueue();

	// action resolution methods
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Determines and performs the appropriate way to handle each action in the
	 * queue.
	 * <p>
	 * Any action that does not have a target player that is lagging will be
	 * resolved. If the target player is lagging, further checks are needed. If the
	 * action is marked with the <code>TransientAction</code> interface, it is
	 * resolved anyways. If it is not, it is moved into the queues lag buffer.
	 */
	public void resolveQueuedActions() {

		queuedActions.updateLagBuffer();

		while (queuedActions.size() > 0) {
			PlayerAction action = queuedActions.deque();

			if (action.getTargetPlayer().getLagFrames() == 0) {
				// target player is not lagging --> resolve action
				System.out.println("Resolving action: " + action.getClass().getSimpleName());
				action.resolve();
				action.getTargetPlayer().setLagFrames(action.getLagFrames());
			} else {
				if (action instanceof TransientAction) {
					// target player is lagging BUT action is transient --> resolve action
					System.out.println("Transiently resolving action: " + action.getClass().getSimpleName());
					action.resolve();
					action.getTargetPlayer()
							.setLagFrames(Math.max(action.getLagFrames(), action.getTargetPlayer().getLagFrames()));
				} else {
					// target player is lagging and action is not transient --> move to lag buffer
					queuedActions.addToLagBuffer(action);
				}
			}
		}
	}

	/**
	 * Receives an event and attempts to add it to the
	 * <code>PlayerActionQueue</code>. If the event is anything other than a
	 * <code>PlayerAction</code> object, an <code>IllegalStateException</code> will
	 * be thrown.
	 * 
	 * @throws IllegalStateException If attempting to receive anything other than a
	 *                               <code>PlayerAction</code> object.
	 */
	@Override
	public void receiveGameEvent(GameEvent event) {
		if (event instanceof PlayerAction) {
			PlayerAction action = (PlayerAction) event;
			queuedActions.add(action);
		} else {
			throw (new IllegalStateException(
					"PlayerActionManager attempted to receive something other than a PlayerAction"));
		}
	}
}
