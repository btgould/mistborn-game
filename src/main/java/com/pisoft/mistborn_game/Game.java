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

	// for debugging: increase delay between frames to read stat menu
	private final int INTERVAL_DELAY = 1000 / 30;

	public Game() {
		initResources();
		LoadedLevels.initLevels();
		setActiveLevel(LoadedLevels.getLevel1());

		display = new Display();
	}

	private void initResources() {

	}

	public void run() {

		while (running) {
			switch (state) {
			case PLAYING:
				long beforeTime, timeDiff, sleep;

				beforeTime = System.currentTimeMillis();

				tick();
				render();

				timeDiff = System.currentTimeMillis() - beforeTime;
				sleep = INTERVAL_DELAY - timeDiff;

				if (sleep < 0) {
					sleep = 2;
				}

				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
				}
				;

				beforeTime = System.currentTimeMillis();
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
