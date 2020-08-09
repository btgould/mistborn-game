package com.pisoft.mistborn_game;

import com.pisoft.mistborn_game.display.Display;
import com.pisoft.mistborn_game.levels.Level;
import com.pisoft.mistborn_game.levels.LoadedLevels;

public class Game implements Runnable {
	private static Display display;

	private boolean running = false;
	private GameState state = GameState.PLAYING;

	private Thread thread;

	private static Level activeLevel;

	// for debugging: increase delay between ticks to read stat menu
	private final int TICK_DELAY = (int) Math.pow(10, 9) / 30;
	private final int PRINT_DELAY = (int) Math.pow(10, 9);

	public Game() {
		initResources();
		LoadedLevels.initLevels();
		setActiveLevel(LoadedLevels.getLevel1());

		display = new Display();
	}

	private void initResources() {

	}

	public void run() {
		long lastTickTime = System.nanoTime();
		long lastPrintTime = System.nanoTime();
		int numTicks = 0, numRenders = 0;

		while (running) {
			
			switch (state) {
			case PLAYING:
				
				long currentTime = System.nanoTime();
				
				render();
				numRenders++;
				
				if (currentTime - lastTickTime >=  TICK_DELAY) {
					lastTickTime = currentTime;
					tick();
					numTicks++;
				}
				
				if (currentTime - lastPrintTime >= PRINT_DELAY) {
					lastPrintTime = currentTime;
					System.out.println("FPS: " + numRenders + ", " + "Ticks: " + numTicks);
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
	private void tick() {
		activeLevel.getPlayerController().getGameEventManager().resolveQueuedActions();
		activeLevel.getPlayerController().getPlayer().tick();
		activeLevel.getPlayerController().getGameEventManager().resolveQueuedEvents();
	}

	private void render() {
		display.getBoard().repaint();
	}

	// thread methods
	// ---------------------------------------------------------------------------------------------------
	public void start() {
		if (running) {
			return;
		}

		running = true;
		thread = new Thread(this);
		thread.start();
	}

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
	public static Level getActiveLevel() {
		return activeLevel;
	}

	public static void setActiveLevel(Level activeLevel) {
		Game.activeLevel = activeLevel;
	}

	public static Display getDisplay() {
		return display;
	}

	public static void setDisplay(Display display) {
		Game.display = display;
	}
}
