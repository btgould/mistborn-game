package com.pisoft.mistborn_game.player.constants;

/**
 * Storage class for variables affecting feel of platforming.
 * <p>
 * All measurements in px/sec or px/sec/sec.
 * 
 * @author gouldb
 *
 */
public class PlatformingConstants {
	private static final double GRAVITY = 1; // amount that ySpeed is changed for every frame the player falls
	private static final double AIR_ACC = 0.5; // amount that xSpeed is changed for every frame the player moves in air
	private static final double WALK_ACC = 1; // amount that xSpeed is changed for every frame the player walks
	private static final double RUN_ACC = 1; // amount that xSpeed is changed for every frame the player runs
	private static final double MAX_WALK_SPEED = 8; // max speed the player can walk
	private static final double MAX_RUN_SPEED = 15; // max speed the player can run
	private static double maxAirSpeed = MAX_WALK_SPEED; // max horiz airSpeed of player (set based on if player can run
														// or not)
	private static final double FRICTION = 0.8; // proportion of speed that remains per frame while sliding
	private static final double FULL_JUMP_SPEED = -17; // initial ySpeed when the player jumps
	private static final double SHORT_JUMP_SPEED = -10; // ySpeed to set if the player releases jump early
	private static final double DOUBLE_JUMP_SPEED = -15; // initial ySpeed when the player double jumps
	private static final double WALL_JUMP_Y_SPEED = -15; // initial ySpeed when the player wall jumps
	private static final double WALL_JUMP_X_SPEED = 10; // initial xSpeed away from wall when the player wall jumps
	private static final double SLOW_DOWN_AMOUNT = 1; // amount for player to slow when prep run released while running
	private static final int LANDING_DELAY = 5; // number of frames that the landing process takes (TODO: this should
												// probably be moved)

	// getters and setters
	// -----------------------------------------------------------------------------------------------
	public static double getGravity() {
		return GRAVITY;
	}

	public static double getAirAcc() {
		return AIR_ACC;
	}

	public static double getWalkAcc() {
		return WALK_ACC;
	}

	public static double getRunAcc() {
		return RUN_ACC;
	}

	public static double getMaxAirSpeed() {
		return maxAirSpeed;
	}

	public static void setMaxAirSpeed(double maxAirSpeed) {
		PlatformingConstants.maxAirSpeed = maxAirSpeed;
	}

	public static double getMaxWalkSpeed() {
		return MAX_WALK_SPEED;
	}

	public static double getMaxRunSpeed() {
		return MAX_RUN_SPEED;
	}

	public static double getFriction() {
		return FRICTION;
	}

	public static double getFullJumpSpeed() {
		return FULL_JUMP_SPEED;
	}

	public static double getShortJumpSpeed() {
		return SHORT_JUMP_SPEED;
	}

	public static double getDoubleJumpSpeed() {
		return DOUBLE_JUMP_SPEED;
	}

	public static double getWallJumpYSpeed() {
		return WALL_JUMP_Y_SPEED;
	}

	public static double getWallJumpXSpeed() {
		return WALL_JUMP_X_SPEED;
	}

	public static double getSlowDownAmount() {
		return SLOW_DOWN_AMOUNT;
	}

	public static int getLandingDelay() {
		return LANDING_DELAY;
	}
}