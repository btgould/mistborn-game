package com.pisoft.mistborn_game.player;

import com.pisoft.mistborn_game.player.game_events.GameEventManager;
import com.pisoft.mistborn_game.player.intents.IntentParser;

public class PlayerController {
	private Player player = new Player();
	
	private IntentParser intentParser = new IntentParser();
	private GameEventManager gameEventManager = new GameEventManager();
	
	/**
	 * Wrapping class for <code>Player</code> object and its associated action handlers.<p>
	 * Ensures that all action processing objects can work together. i.e., allows event processing 
	 * to move smoothly from intent generation, to parsing, to action resolving.
	 */
	public PlayerController() {
		intentParser.setTargetPlayer(player);
		intentParser.addPlayerActionListener(gameEventManager);		
		player.addGameEventListener(gameEventManager);
		gameEventManager.setTargetPlayer(player);
	}
	
	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public IntentParser getIntentParser() {
		return intentParser;
	}

	public void setIntentParser(IntentParser intentParser) {
		this.intentParser = intentParser;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public GameEventManager getGameEventManager() {
		return gameEventManager;
	}

	public void setGameEventManager(GameEventManager gameEventManager) {
		this.gameEventManager = gameEventManager;
	}
}
