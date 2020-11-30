package com.pisoft.mistborn_game.player.game_events;

import java.util.ArrayList;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Player;

public interface GameEventDispatcher {
	// listener management
	// ---------------------------------------------------------------------------------------------------
	public ArrayList<GameEventListener> getListeners();

	public Player getTargetPlayer();

	default void addGameEventListener(GameEventListener listener) {
		getListeners().add(listener);
	}

	default void removeGameEventListener(GameEventListener listener) {
		getListeners().remove(listener);
	}

	default void removeGameEventListener(int index) {
		getListeners().remove(index);
	}

	// object dispatch methods
	// ---------------------------------------------------------------------------------------------------
	default void dispatchEvent(GameEvent event) {
		if (event != null) {
			event.setTargetPlayer(getTargetPlayer());
			event.getListeners().clear();
			event.getListeners().addAll(this.getListeners());

			for (GameEventListener listener : getListeners()) {
				listener.receiveGameEvent(event);
			}
		}
	}

	default void dispatchEvent(GameEvent event, GameEventListener listener) {
		if (event != null) {
			event.setCreationTime(Game.getCurrentTime());

			event.setTargetPlayer(getTargetPlayer());
			event.getListeners().clear();
			event.addGameEventListener(listener);

			listener.receiveGameEvent(event);
		}
	}

	default void dispatchEvent(GameEvent event, int delayFrames) {
		if (event != null) {
			event.setCreationTime(Game.getCurrentTime());
			event.setValidExecutionTime(event.getCreationTime() + Game.getTickDelay() * delayFrames);

			event.setTargetPlayer(getTargetPlayer());
			event.getListeners().clear();
			event.getListeners().addAll(this.getListeners());

			for (GameEventListener listener : getListeners()) {
				listener.receiveGameEvent(event);
			}
		}
	}
}
