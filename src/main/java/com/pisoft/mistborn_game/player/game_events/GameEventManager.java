package com.pisoft.mistborn_game.player.game_events;

import com.pisoft.mistborn_game.Game;

public class GameEventManager implements GameEventListener {

	private GameEventQueue queuedEvents = new GameEventQueue();

	// event resolution methods
	// ---------------------------------------------------------------------------------------------------
	public void resolveQueuedEvents() {
		queuedEvents.updateDelayBuffer();
		
		while (queuedEvents.size() > 0) {
			GameEvent event = queuedEvents.deque();
			
			if (Game.getCurrentTime() >= event.getValidExecutionTime()) {
				// enough time has passed to resolve the event --> resolve
				System.out.println("Resolving event: " + event.getClass().getSimpleName());
				event.resolve();
			} else {
				// the event is not yet ready to resolve --> put in delay buffer
				queuedEvents.addToDelayBuffer(event);
			}
		}
	}

	@Override
	public void receiveGameEvent(GameEvent event) {
		if (event != null) {
			queuedEvents.add(event);
		}
	}
}
