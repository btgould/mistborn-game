package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;

import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.PlayerActionListener;

public class GameEventManager implements GameEventListener, PlayerActionListener {
	private Player targetPlayer;

	private ArrayList<GameEvent> queuedEvents = new ArrayList<>();
	private ArrayList<PlayerAction> queuedActions = new ArrayList<>();

	private boolean resolving = false;

	// event resolution methods
	// ---------------------------------------------------------------------------------------------------
	public void resolveQueuedEvents() {
		resolving = true;

		for (GameEvent event : queuedEvents) {
			event.setTargetPlayer(targetPlayer);
			event.resolve();
		}

		queuedEvents.clear();

		resolving = false;
	}

	@Override
	public void receiveGameEvent(GameEvent event) {
		if (event != null && !resolving) {
			queuedEvents.add(event);
		}
	}

	// action resolution methods
	// ---------------------------------------------------------------------------------------------------
	public void resolveQueuedActions() {
		resolving = true;

		for (PlayerAction action : queuedActions) {
			action.setTargetPlayer(targetPlayer);
			action.resolve();
		}

		queuedActions.clear();

		resolving = false;
	}

	// TODO: filter by priority, exclusivity, etc.
	@Override
	public void receiveAction(PlayerAction action) {
		if (action != null) {
			if (!resolving) {
				queuedActions.add(action);
			}
		}
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}
}
