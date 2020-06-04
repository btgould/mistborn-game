package com.pisoft.mistborn_game.player.platforming;

public class PlatformingConstants {
    private static final double gravity = 1; // amount that ySpeed is changed for every frame the player falls
    private static final double airAcc = 0.5; // amount that xSpeed is changed for every frame the player moves in air
    private static final double walkAcc = 1; // amount that xSpeed is changed for every frame the player walks
    private static final double runAcc = 1; // amount that xSpeed is changed for every frame the player runs
    private static double maxAirSpeed; // max horiz airSpeed of player (set based on if player can run or not)
    private static final double maxWalkSpeed = 8; // max speed the player can walk
    private static final double maxRunSpeed = 15; // max speed the player can run
    private static final double friction = 0.8; // proportion of speed that remains per frame while sliding
    private static final double fullJumpSpeed = -17; // initial ySpeed when the player jumps
    private static final double shortJumpSpeed = -10; // ySpeed to set if the player releases jump early
    private static final double doubleJumpSpeed = -15; // initial ySpeed when the player double jumps
    private static final double wallJumpYSpeed = -15; // initial ySpeed when the player wall jumps
    private static final double wallJumpXSpeed = 10; // initial xSpeed away from wall when the player wall jumps

    // getters and setters
    // -----------------------------------------------------------------------------------------------
    public static double getGravity() {
        return gravity;
    }

    public static double getAirAcc() {
        return airAcc;
    }

    public static double getWalkAcc() {
        return walkAcc;
    }

    public static double getRunAcc() {
        return runAcc;
    }

    public static double getMaxAirSpeed() {
        return maxAirSpeed;
    }

    public static void setMaxAirSpeed(double maxAirSpeed) {
        PlatformingConstants.maxAirSpeed = maxAirSpeed;
    }

    public static double getMaxWalkSpeed() {
        return maxWalkSpeed;
    }

    public static double getMaxRunSpeed() {
        return maxRunSpeed;
    }

    public static double getFriction() {
        return friction;
    }

    public static double getFullJumpSpeed() {
        return fullJumpSpeed;
    }

    public static double getShortJumpSpeed() {
        return shortJumpSpeed;
    }

    public static double getDoubleJumpSpeed() {
        return doubleJumpSpeed;
    }

    public static double getWallJumpYSpeed() {
        return wallJumpYSpeed;
    }

    public static double getWallJumpXSpeed() {
        return wallJumpXSpeed;
    }
}