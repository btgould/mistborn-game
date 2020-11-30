package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.game_events.GameEvent;
import com.pisoft.mistborn_game.player.game_events.GameEventListener;

public class PlayerActionManager implements GameEventListener {

	private PlayerActionQueue queuedActions = new PlayerActionQueue();
	private PlayerActionQueue transientActions = new PlayerActionQueue();

	// action resolution methods
	// ---------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public void resolveQueuedActions() {
		// resolve standard actions when not lagging 
		while (queuedActions.size() > 0) {
			
			PlayerAction action = queuedActions.element();
			Player player = action.getTargetPlayer();
			
			// decrement lag, break if player is lagging 
			// TODO: lag should be decremented even if there are no actions in the queue
			if (player.getLagFrames() != 0) {
				player.setLagFrames(player.getLagFrames() - 1);
				break;
			}
			
			queuedActions.remove();
			
			System.out.println("Resolving action: " + action.getClass().getSimpleName());

			action.resolve();
			player.setLagFrames(action.getLagFrames());
		}
		
		// resolve transient actions always
		while (transientActions.size() > 0) {
			
			PlayerAction action = transientActions.remove();
			Player player = action.getTargetPlayer();
			
			System.out.println("Resolving transient action: " + action.getClass().getSimpleName());

			action.resolve();
			player.setLagFrames(Math.max(action.getLagFrames(), player.getLagFrames()));
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
			if (action instanceof TransientAction) {
				transientActions.add(action);
			} else {
				queuedActions.add(action);
			}
		} else {
			throw (new IllegalStateException(
					"PlayerActionManager attempted to receive something other than a PlayerAction"));
		}
	}
}
