package com.pisoft.mistborn_game.player.constants;

import java.util.HashMap;

import com.pisoft.mistborn_game.player.actions.WalkAction;
import com.pisoft.mistborn_game.player.actions.WallJumpAction;
import com.pisoft.mistborn_game.player.game_events.GameEvent;

/**
 * This class serves as a library for constants that determine the number of lag
 * frames of each <code>PlayerAction</code> object.
 * <p>
 * These values are stored in a <code>Class</code> to <code>Integer</code>
 * <code>HashMap</code>. All <code>Class</code> key objects must extend the
 * abstract class <code>PlayerAction</code> to be valid.
 * <p>
 * Before any <code>get()</code> calls are made, the method
 * <code>initLagFrames()</code> must be called, as this method actually
 * adds all the useful mappings. Before this method is called, the map is empty.
 * 
 * @author gouldb
 *
 */
public class GameEventLagConstants {
	private static HashMap<Class<? extends GameEvent>, Integer> lagFrames = new HashMap<>();

	/**
	 * Adds all mappings between class and lag frames to the map. Before calling this
	 * function, the map is empty.
	 * <p>
	 * Must be called before any calls to <code>get()</code> are needed.
	 * 
	 */
	public static void initLagFrames() {
		lagFrames.put(WallJumpAction.class, 10);
		lagFrames.put(WalkAction.class, 0);
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Gets the map of lag frames. 
	 * 
	 * @return The <code>HashMap</code> library of lag frames. 
	 */
	public static HashMap<Class<? extends GameEvent>, Integer> getLagFrames() {
		return lagFrames;
	}

	public static void setLagFrames(HashMap<Class<? extends GameEvent>, Integer> lagFrames) {
		GameEventLagConstants.lagFrames = lagFrames;
	}

}
