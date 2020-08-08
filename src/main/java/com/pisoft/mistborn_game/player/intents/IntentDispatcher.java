package com.pisoft.mistborn_game.player.intents;

import java.util.ArrayList;

public interface IntentDispatcher {
	//listener management
	// ---------------------------------------------------------------------------------------------------
	public ArrayList<IntentListener> listeners = new ArrayList<>();
	
	default void addIntentListener(IntentListener listener) {
		listeners.add(listener);
	}
	
	default void removeIntentListener(IntentListener listener) {
		listeners.remove(listener);
	}
	
	default void removeIntentListener(int index) {
		listeners.remove(index);
	}

	// object dispatch method
	// ---------------------------------------------------------------------------------------------------
	default void dispatchIntent(PlayerIntent intent) {
		for (IntentListener listener : listeners) {
			listener.receiveIntent(intent);
		}
	}
}
