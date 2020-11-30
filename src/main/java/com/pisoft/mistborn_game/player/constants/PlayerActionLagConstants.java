package com.pisoft.mistborn_game.player.constants;

import java.util.HashMap;

import com.pisoft.mistborn_game.player.actions.*;

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
public class PlayerActionLagConstants {
	private static HashMap<Class<? extends PlayerAction>, Integer> lagFrames = new HashMap<Class<? extends PlayerAction>, Integer>();

	/**
	 * Adds all mappings between class and lag frames to the map. Before calling this
	 * function, the map is empty.
	 * <p>
	 * Must be called before any calls to <code>get()</code> are needed.
	 * 
	 */
	public static void initLagFrames() {
		lagFrames.put(WalkAction.class, 0);
		lagFrames.put(RunAction.class, 0);
		lagFrames.put(AirAccAction.class, 0);
		lagFrames.put(WallPushAction.class, 0);
		lagFrames.put(CantAccAction.class, 0);

		lagFrames.put(JumpAction.class, 0);
		lagFrames.put(DoubleJumpAction.class, 0);
		lagFrames.put(WallJumpAction.class, 10);

		lagFrames.put(CrouchAction.class, 0);
		lagFrames.put(CrouchTurnAction.class, 0);

		lagFrames.put(PrepRunAction.class, 0);

		lagFrames.put(SteelPushAction.class, 0);

		lagFrames.put(StopAccAction.class, 0);
		lagFrames.put(StopWallPushAction.class, 0);

		lagFrames.put(JumpReleaseAction.class, 0);
		lagFrames.put(ShortenJumpAction.class, 0);

		lagFrames.put(StopCrouchAction.class, 0);

		lagFrames.put(StopPrepRunAction.class, 0);

		lagFrames.put(StopSteelPushAction.class, 0);
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Gets the map of lag frames. 
	 * 
	 * @return The <code>HashMap</code> library of lag frames. 
	 */
	public static HashMap<Class<? extends PlayerAction>, Integer> getLagFrames() {
		return lagFrames;
	}

	public static void setLagFrames(HashMap<Class<? extends PlayerAction>, Integer> lagFrames) {
		PlayerActionLagConstants.lagFrames = lagFrames;
	}

}
