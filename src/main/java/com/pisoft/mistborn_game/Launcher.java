package com.pisoft.mistborn_game;

/**
 * Main class for this game. Creates a new <code>Game</code> object, and starts
 * it in a new thread.
 * 
 * @author gouldb
 * 
 */
public class Launcher {
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
}
