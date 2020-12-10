package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;

import com.pisoft.mistborn_game.Game;

public class GameEventQueue {
	private ArrayList<GameEvent> queue = new ArrayList<>();
	private ArrayList<GameEvent> delayBuffer = new ArrayList<>();

	public boolean add(GameEvent event) {
		boolean indexFound = false;
		int index = 0;

		synchronized (queue) {
			for (int i = 0; i < queue.size(); i++) {
				if (!indexFound && event.getValidExecutionTime() < queue.get(i).getValidExecutionTime()) {
					// mark index to insert, stop searching
					index = i;
					indexFound = true;
				}
			}

			if (!indexFound) {
				// no element in queue has later execution time --> insert at last index
				index = queue.size();
			}

			// if it gets here, event needs to be added
			queue.add(index, event);
			return true;
		}
	}
	
	public GameEvent deque() {
		synchronized (queue) { 
			return queue.remove(0);
		}
	}
	
	public int size() {
		synchronized (queue) {
			return queue.size();
		}
	}
	
	public void addToDelayBuffer(GameEvent event) {
		synchronized (delayBuffer) {
			delayBuffer.add(event);
		}
	}
	
	public void updateDelayBuffer() {
		synchronized (delayBuffer) {
			synchronized (queue) {
				for (GameEvent event : delayBuffer) {
					if (event.getValidExecutionTime() <= Game.getCurrentTime()) {
						this.add(event);
					}
				}
				
				delayBuffer.removeAll(queue);
			}
		}
	}
}
