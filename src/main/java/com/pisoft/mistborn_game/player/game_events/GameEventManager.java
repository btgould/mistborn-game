package com.pisoft.mistborn_game.player.game_events;

import java.util.concurrent.ArrayBlockingQueue;

import com.pisoft.mistborn_game.Game;

public class GameEventManager implements GameEventListener {

	private ArrayBlockingQueue<GameEvent> queuedEvents = new ArrayBlockingQueue<GameEvent>(50);

	// event resolution methods
	// ---------------------------------------------------------------------------------------------------
	public void resolveQueuedEvents() {

		int skippedEvents = 0;
		
		while (queuedEvents.size() > skippedEvents) {
			GameEvent event = queuedEvents.remove();
			
			if (Game.getCurrentTime() >= event.getValidExecutionTime()) {
				System.out.println("Resolving event: " + event.getClass().getSimpleName());
				
				event.resolve();
			} else {
				// this works because the queue is FIFO
				queuedEvents.add(event);
				skippedEvents++;
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
