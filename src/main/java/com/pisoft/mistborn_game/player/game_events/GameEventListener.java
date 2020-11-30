package com.pisoft.mistborn_game.player.game_events;

// TODO: listeners should have a target game object

/**
 * Interface for listening for <code>GameEvent</code> objects.
 * 
 * @author gouldb
 *
 */
public interface GameEventListener {
	/**
	 * Method to dictate how to respond to any received event.
	 * 
	 * @param event The event to receive.
	 */
	public void receiveGameEvent(GameEvent event);
}
