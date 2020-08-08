package com.pisoft.mistborn_game.player.actions;

import java.util.ArrayList;

public interface PlayerActionDispatcher {
	//listener management
	// ---------------------------------------------------------------------------------------------------
	public ArrayList<PlayerActionListener> listeners = new ArrayList<>();
	
	default void addPlayerActionListener(PlayerActionListener listener) {
		listeners.add(listener);
	}
	
	default void removePlayerActionListener(PlayerActionListener listener) {
		listeners.remove(listener);
	}
	
	default void removePlayerActionListener(int index) {
		listeners.remove(index);
	}
	
	// object dispatch method
	// ---------------------------------------------------------------------------------------------------
	default void dispatchAction(PlayerAction action) {
		for (PlayerActionListener listener : listeners) {
			listener.receiveAction(action);
		}
	}
}
