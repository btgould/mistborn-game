package com.pisoft.mistborn_game;

import com.pisoft.mistborn_game.display.Display;
import com.pisoft.mistborn_game.levels.Level;
import com.pisoft.mistborn_game.levels.LoadedLevels;
import com.pisoft.mistborn_game.player.actions.PlayerActionManager;
import com.pisoft.mistborn_game.player.constants.GameEventLagConstants;
import com.pisoft.mistborn_game.player.constants.GameEventPriorityConstants;
import com.pisoft.mistborn_game.player.game_events.GameEvent;
import com.pisoft.mistborn_game.player.game_events.GameEventManager;

/**
 * Main wrapping class for all game objects and information.
 * <p>
 * Contains the main game loop, as while as <code>tick</code> and
 * <code>render</code> methods.
 * 
 * @author gouldb
 *
 */
public class Game implements Runnable {
	private static Display display;

	private boolean running = false;
	private GameState state;

	private Thread thread;

	private static Level activeLevel;

	private static GameEventManager<GameEvent> gameEventManager = new GameEventManager<>(GameEvent.class);
	private static PlayerActionManager playerActionManager = new PlayerActionManager();

	// for debugging: increase delay between ticks to read stat menu
	private static int tickDelay = (int) Math.pow(10, 9) / 30;
	private int printDelay = (int) Math.pow(10, 9);
	private static long currentTime = 3;

	/**
	 * Basic constructor for the <code>Game</code> class.
	 * <p>
	 * Also initializes game resources and state.
	 * 
	 */
	public Game() {
		// TODO: figure out how to not paint this until a level is initialized
		display = new Display();

		initResources();
		setActiveLevel(LoadedLevels.getLevel1());

		state = GameState.PLAYING;
	}

	/**
	 * Loads all game resources.
	 * 
	 */
	private void initResources() {
		LoadedLevels.initLevels();

		GameEventPriorityConstants.initActionPriorities();
		GameEventLagConstants.initLagFrames();
	}

	/**
	 * Contains the main game loop logic.
	 * <p>
	 * The game renders as quickly as it can.
	 * <p>
	 * The tick speed is dependent on the <code>tickDelay</code> variable, which can
	 * be set with <code>setTickDelay(int tickDelay)</code>. This variable specifies
	 * the delay in ns between each tick. However, there are also other operations
	 * that take time within the game loop, so no hard guarantee is made about the
	 * exact tick speed.
	 * <p>
	 * Information about the actual number of ticks and renders over a given
	 * interval are printed to the console at the end of that interval. The size of
	 * this interval (in ns) can be set with
	 * <code>setPrintDelay(int printDelay)</code>.
	 * 
	 */
	@Override
	public void run() {
		long lastTickTime = System.nanoTime();
		long lastPrintTime = System.nanoTime();
		int numTicks = 0, numRenders = 0;

		while (running) {

			switch (state) {
			case PLAYING:

				currentTime = System.nanoTime();

				render();
				numRenders++;

				if (currentTime - lastTickTime >= tickDelay) {
					lastTickTime = currentTime;
					tick();
					numTicks++;
				}

				if (currentTime - lastPrintTime >= printDelay) {
					lastPrintTime = currentTime;
					System.out.println("FPS: " + numRenders + ", " + "Ticks: " + numTicks);
					System.out.println("");
					numTicks = 0;
					numRenders = 0;
				}

				break;

			default:
				break;
			}
		}

		stop();
	}

	// playing methods
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Resolves all <code>GameEvent</code> objects that have been generated since
	 * the last call of this method, and changes the state of the active
	 * <code>Player</code> object accordingly.
	 * 
	 * TODO: This should be able to tick an arbitrary number / type of objects
	 */
	private void tick() {
		System.out.println("Frame break");

		playerActionManager.resolveQueuedEvents();
		activeLevel.getPlayer().tick();
		gameEventManager.resolveQueuedEvents();

		System.out.println();
	}

	/**
	 * Updates the current visual display of the game.
	 * 
	 */
	private void render() {
		display.getBoard().repaint();
	}

	// thread methods
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Starts the main game loop.
	 * 
	 */
	public void start() {
		if (running) {
			return;
		}

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stops the main game loop.
	 * 
	 */
	public void stop() {
		if (!running) {
			return;
		}

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Gets the active level.
	 * 
	 * @return The <code>Level</code> object that is currently in use.
	 * 
	 */
	public static Level getActiveLevel() {
		return activeLevel;
	}

	/**
	 * Sets the active level, and changes manager targets to ensure events are
	 * processed properly.
	 * 
	 * @param activeLevel The <code>Level</code> object to set as active.
	 * 
	 */
	public static void setActiveLevel(Level activeLevel) {
		activeLevel.getPlayer().addGameEventListener(gameEventManager);
		display.getBoard().getKeyBinder().setTargetPlayer(activeLevel.getPlayer());

		Game.activeLevel = activeLevel;
	}

	public static Display getDisplay() {
		return display;
	}

	public static void setDisplay(Display display) {
		Game.display = display;
	}

	public static GameEventManager<GameEvent> getGameEventManager() {
		return gameEventManager;
	}

	public static void setGameEventManager(GameEventManager<GameEvent> gameEventManager) {
		Game.gameEventManager = gameEventManager;
	}

	public static PlayerActionManager getPlayerActionManager() {
		return playerActionManager;
	}

	public static void setPlayerActionManager(PlayerActionManager playerActionManager) {
		Game.playerActionManager = playerActionManager;
	}

	/**
	 * Gets the delay between successive calls to the <code>tick()</code> function.
	 * 
	 * @return The current tickDelay.
	 * 
	 */
	public static int getTickDelay() {
		return tickDelay;
	}

	/**
	 * Sets the delay between successive calls to the <code>tick()</code> function.
	 * 
	 * @param tickDelay The new tickDelay to use
	 * 
	 */
	public void setTickDelay(int tickDelay) {
		Game.tickDelay = tickDelay;
	}

	/**
	 * Gets the delay between successive prints of tick and render information.
	 * 
	 * @return The current tickDelay.
	 * 
	 */
	public int getPrintDelay() {
		return printDelay;
	}

	/**
	 * Sets the delay between successive prints of tick and render information.
	 * 
	 * @return The current tickDelay.
	 * 
	 */
	public void setPrintDelay(int printDelay) {
		this.printDelay = printDelay;
	}

	/**
	 * Returns the current time as a <code>long</code> in nanoseconds (updated
	 * before every render). Use this method to avoid repeated calls to the
	 * potentially costly <code>System.nanoTime()</code>.
	 * 
	 * @return The current time as specified by <code>System.nanoTime()</code>.
	 */
	public static long getCurrentTime() {
		return currentTime;
	}

	public static void setCurrentTime(long currentTime) {
		Game.currentTime = currentTime;
	}
}
