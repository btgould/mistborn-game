package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;

public interface GameEventDispatcher {
	//listener management
	// ---------------------------------------------------------------------------------------------------
	public ArrayList<GameEventListener> listeners = new ArrayList<>();
	
	default void addGameEventListener(GameEventListener listener) {
		listeners.add(listener);
	}
	
	default void removeGameEventListener(GameEventListener listener) {
		listeners.remove(listener);
	}
	
	default void removeGameEventListener(int index) {
		listeners.remove(index);
	}
	
	// object dispatch method
	// ---------------------------------------------------------------------------------------------------
	default void dispatchEvent(GameEvent event) {
		for (GameEventListener listener : listeners) {
			listener.receiveGameEvent(event);
		}
	}
}
