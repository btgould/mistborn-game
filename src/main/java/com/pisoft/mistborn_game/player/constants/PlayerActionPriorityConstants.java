package com.pisoft.mistborn_game.player.constants;

import java.util.HashMap;

import com.pisoft.mistborn_game.player.actions.*;
import com.pisoft.mistborn_game.player.intents.StopJumpIntent;

/**
 * This class serves as a library for constants that determine the priority of
 * each <code>PlayerAction</code> object.
 * <p>
 * These values are stored in a <code>Class</code> to <code>Integer</code>
 * <code>HashMap</code>. All <code>Class</code> key objects must extend the
 * abstract class <code>PlayerAction</code> to be valid.
 * <p>
 * Before any <code>get()</code> calls are made, the method
 * <code>initActionPriorities()</code> must be called, as this method actually
 * adds all the useful mappings. Before this method is called, the map is empty.
 * 
 * @author gouldb
 *
 */
public class PlayerActionPriorityConstants {
	private static HashMap<Class<? extends PlayerAction>, Integer> actionPriorities = new HashMap<Class<? extends PlayerAction>, Integer>();

	/**
	 * Adds all mappings between class and priority to the map. Before calling this
	 * function, the map is empty.
	 * <p>
	 * Must be called before any calls to <code>get()</code> are needed.
	 * 
	 */
	public static void initActionPriorities() {
		actionPriorities.put(WalkAction.class, 0);
		actionPriorities.put(RunAction.class, 0);
		actionPriorities.put(AirAccAction.class, 0);
		actionPriorities.put(WallPushAction.class, 0);
		actionPriorities.put(CantAccAction.class, 0);

		actionPriorities.put(JumpAction.class, 0);
		actionPriorities.put(DoubleJumpAction.class, 0);
		actionPriorities.put(WallJumpAction.class, 0);

		actionPriorities.put(CrouchAction.class, 0);
		actionPriorities.put(CrouchTurnAction.class, 0);

		actionPriorities.put(PrepRunAction.class, 0);

		actionPriorities.put(SteelPushAction.class, 0);

		actionPriorities.put(StopAccAction.class, 0);
		actionPriorities.put(StopWallPushAction.class, 0);

		actionPriorities.put(JumpReleaseAction.class, 0);
		actionPriorities.put(ShortenJumpAction.class, 0);

		actionPriorities.put(StopCrouchAction.class, 0);

		actionPriorities.put(StopPrepRunAction.class, 0);

		actionPriorities.put(StopSteelPushAction.class, 0);
		
		actionPriorities.put(StopJumpIntent.class, 0);
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Gets the map of action priorities. 
	 * 
	 * @return The <code>HashMap</code> library of action priorities. 
	 */
	public static HashMap<Class<? extends PlayerAction>, Integer> getActionPriorities() {
		return actionPriorities;
	}

	public static void setActionPriorities(HashMap<Class<? extends PlayerAction>, Integer> actionPriorities) {
		PlayerActionPriorityConstants.actionPriorities = actionPriorities;
	}
}
