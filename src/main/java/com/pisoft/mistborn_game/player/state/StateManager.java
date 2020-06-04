package com.pisoft.mistborn_game.player.state;

import java.awt.event.KeyEvent;

import com.pisoft.mistborn_game.player.*;
import com.pisoft.mistborn_game.player.platforming.*;

public class StateManager {
    private Player targetPlayer; 

    public State getNextState(final State currentState) {
        State nextState = currentState;

        /*switch (currentState) {
            case IDLE:
                if (targetPlayer.isGrounded()) {
                    if (targetPlayer.isCrouching()) {
                        // grounded and crouching -> CROUCHING
                        nextState = State.CROUCHING;
                    } else if (targetPlayer.isWallPushing()) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        nextState = State.AT_WALL;
                    } else if (targetPlayer.isAccelerating()) {
                        // grounded, not crouching, not wall pushing, accelerating -> WALKING
                        nextState = State.WALKING;
                    }
                } else {
                    if (targetPlayer.isJumping()) {
                        // not grounded, jumping -> JUMPING
                        nextState = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        nextState = State.FALLING;
                    }
                }
                // grounded, not crouching, not wall pushing, not accelerating -> stay in IDLE
                break;

            case WALKING:
                if (targetPlayer.isGrounded()) {
                    if (targetPlayer.isCrouching()) {
                        // grounded and crouching -> CROUCHING
                        nextState = State.CROUCHING;
                    } else if (targetPlayer.isWallPushing()) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        nextState = State.AT_WALL;
                    } else if (targetPlayer.isSliding()) {
                        // grounded, not crouching, not wall pushing, sliding -> SLIDING
                        nextState = State.SLIDING;
                    } else if (Math.abs(targetPlayer.getxSpeed()) >= PlatformingConstants.getMaxWalkSpeed()
                            && targetPlayer.getCanRun()) {
                        // grounded, not crouching, not wall pushing, not sliding, speed >= max walk
                        // speed and can run -> RUNNING
                        nextState = State.RUNNING;
                    } else if (targetPlayer.getxSpeed() == 0 && !targetPlayer.isAccelerating()) {
                        // grounded, not crouching, not wall pushing, not sliding, speed = 0 -> IDLE
                        // needed because otherwise if xSpeed = 1 when released, checkForFriction sets
                        // sliding to false
                        nextState = State.IDLE;
                    }
                } else {
                    if (targetPlayer.isJumping()) {
                        // not grounded, jumping -> JUMPING
                        nextState = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        nextState = State.FALLING;
                    }
                }
                // grounded, not crouching, not at wall, not sliding, speed < max walk speed or
                // cant run -> stay in WALKING
                break;

            case RUNNING:
                if (targetPlayer.isGrounded()) {
                    if (targetPlayer.isCrouching()) {
                        // grounded and crouching -> CROUCHING
                        nextState = State.CROUCHING;
                    } else if (targetPlayer.isWallPushing()) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        nextState = State.AT_WALL;
                    } else if (targetPlayer.isSliding()) {
                        // grounded, not crouching, not wall pushing, sliding -> SLIDING
                        nextState = State.SLIDING;
                    } else if (!targetPlayer.getCanRun()) {
                        // TODO: should this also check for if speed < max walk speed?
                        // grounded, not crouching, not wall pushing, not sliding, cant run -> WALKING
                        nextState = State.WALKING;
                    }
                } else {
                    if (targetPlayer.isJumping()) {
                        // not grounded, jumping -> JUMPING
                        nextState = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        nextState = State.FALLING;
                    }
                }
                // grounded, not crouching, not wall pushing, not sliding, can run -> stay in
                // RUNNING
                break;

            case SLIDING:
                if (targetPlayer.isGrounded()) {
                    if (targetPlayer.isCrouching()) {
                        // grounded and crouching -> CROUCHING
                        nextState = State.CROUCHING;
                    } else if (targetPlayer.isWallPushing()) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        nextState = State.AT_WALL;
                    } else if (targetPlayer.isAccelerating()) {
                        // grounded, not crouching, not wall pushing, accelerating -> determine based on
                        // speed & run ability
                        if (Math.abs(targetPlayer.getxSpeed()) >= PlatformingConstants.getMaxWalkSpeed()
                                && targetPlayer.getCanRun()) {
                            nextState = State.RUNNING;
                        } else if (Math.abs(targetPlayer.getxSpeed()) > 0) {
                            nextState = State.WALKING;
                        }
                    } else if (targetPlayer.getxSpeed() == 0) {
                        // grounded, not crouching, not wall pushing, not accelerating, speed = 0 ->
                        // IDLE
                        nextState = State.IDLE;
                    }
                } else {
                    if (targetPlayer.isJumping()) {
                        // not grounded, jumping -> JUMPING
                        nextState = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        nextState = State.FALLING;
                    }
                }
                // grounded, not crouching, not wall pushing, not accelerating, speed != 0 ->
                // stay in SLIDING
                break;

            case JUMPING:
                // TODO: make this only State-oriented
                if (!player.controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_UP)
                        || Math.abs(targetPlayer.getySpeed()) <= Math.abs(PlatformingConstants.getShortJumpSpeed())) {
                    targetPlayer.setySpeed(PlatformingConstants.getShortJumpSpeed());

                    if (targetPlayer.isWallPushing()) {
                        // up not pressed or ySpeed too low, wall pushing -> WALL_FALLING
                        nextState = State.WALL_FALLING;
                    } else {
                        // up not presses or ySpeed too low, not wall pushing -> FALLING
                        nextState = State.FALLING;
                    }
                }
                // up pressed, ySpeed higher than shortJumpSpeed -> stay in JUMPING
                break;

            case DOUBLE_JUMPING:
                // TODO: can double jumping go into landing?
                if (targetPlayer.isGrounded()) {
                    // grounded -> LANDING
                    nextState = State.LANDING;
                } else if (targetPlayer.isWallPushing()) {
                    // not grounded, wallPushing -> WALL_FALLING
                    nextState = State.WALL_FALLING;
                } else {
                    // not grounded, not wallPushing -> FALLING
                    nextState = State.FALLING;
                }
                break;

            case WALL_JUMPING:
                // TODO: can wall jumping go into landing or wall falling?
                if (targetPlayer.isGrounded()) {
                    // grounded -> LANDING
                    nextState = State.LANDING;
                } else if (targetPlayer.isWallPushing()) {
                    // not grounded, wallPushing -> WALL_FALLING
                    nextState = State.WALL_FALLING;
                } else {
                    // not grounded, not wallPushing -> FALLING
                    nextState = State.FALLING;
                }
                break;

            case FALLING:
                if (targetPlayer.isLanding()) {
                    // landing -> LANDING
                    nextState = State.LANDING;
                } else if (targetPlayer.isDoubleJumping()) {
                    // not landing, double jumping -> DOUBLE_JUMPING
                    nextState = State.DOUBLE_JUMPING;
                } else if (targetPlayer.isWallPushing()) {
                    // not landing, not double jumping, wall pushing -> WALL_FALLING
                    nextState = State.WALL_FALLING;
                }
                // not landing, not double jumping, not wall pushing -> stay in FALLING
                break;

            case WALL_FALLING:
                if (targetPlayer.isLanding()) {
                    // landing -> LANDING
                    nextState = State.LANDING;
                } else if (targetPlayer.isWallJumping()) {
                    // not landing, wall jumping -> WALL_JUMPING
                    nextState = State.WALL_JUMPING;
                } else if (targetPlayer.isDoubleJumping()) {
                    // not landing, not wall jumping, double jumping -> DOUBLE_JUMPING
                    nextState = State.DOUBLE_JUMPING;
                } else if (!targetPlayer.isWallPushing()) {
                    // not landing, not wall jumping, not double jumping, not wall pushing ->
                    // FALLING
                    nextState = State.FALLING;
                }
                // not grounded, not wall jumping, not double jumping, wall pushing -> stay in
                // WALL_FALLING
                break;

            case LANDING:
                if (targetPlayer.isSliding()) {
                    // sliding (triggered by not pressing left or right) -> SLIDING
                    nextState = State.SLIDING;
                } else if (targetPlayer.getxSpeed() >= PlatformingConstants.getMaxWalkSpeed() && targetPlayer.getCanRun()) {
                    // not sliding -> determine based on xSpeed & run ability
                    nextState = State.RUNNING;
                } else if (Math.abs(targetPlayer.getxSpeed()) > 0) {
                    nextState = State.WALKING;
                } else {
                    nextState = State.IDLE;
                }
                break;

            case CROUCHING:
                if (targetPlayer.isGrounded()) {
                    if (targetPlayer.isCrouching()) {
                        // grounded, crouching -> CROUCHING
                        nextState = State.CROUCHING;
                    } else if (targetPlayer.isWallPushing()) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        nextState = State.AT_WALL;
                    } else {
                        if (targetPlayer.isAccelerating()) {
                            // grounded, not wall pushing, not crouching, and accelerating -> set based on
                            // speed & run ability
                            if (Math.abs(targetPlayer.getxSpeed()) >= PlatformingConstants
                                    .getMaxWalkSpeed()
                                    && targetPlayer.getCanRun()) {
                                nextState = State.RUNNING;
                            } else if (Math.abs(targetPlayer.getxSpeed()) > 0) {
                                nextState = State.WALKING;
                            }
                        } else if (targetPlayer.getxSpeed() == 0) {
                            // grounded, not wall pushing, not crouching, not accelerating, speed = 0 ->
                            // IDLE
                            nextState = State.IDLE;
                        } else {
                            // grounded, not wall pushing, not crouching, not accelerating, speed != 0 ->
                            // SLIDING
                            nextState = State.SLIDING;
                        }
                    }
                } else {
                    // not grounded -> FALLING (b/c cant jump)
                    nextState = State.FALLING;
                }
                break;

            case AT_WALL:
                if (targetPlayer.isGrounded()) {
                    if (targetPlayer.isCrouching()) {
                        // grounded, crouching -> CROUCHING
                        nextState = State.CROUCHING;
                    } else if (targetPlayer.isAccelerating()) {
                        // grounded, not crouching, accelerating (can only accelerate away from wall) ->
                        // WALKING
                        nextState = State.WALKING;
                    } else if (!targetPlayer.isWallPushing()) {
                        // grounded, not crouching, not accelerating, not wall pushing -> IDLE
                        nextState = State.IDLE;
                    }
                } else {
                    // not grounded -> JUMPING
                    nextState = State.JUMPING;
                }
                // grounded, not crouching, not accelerating, wallPushing -> stay in AT_WALL

                break;
        }*/

        if (targetPlayer.isGrounded()) {
            if (targetPlayer.isLanding()) {
                nextState = State.LANDING;
            } else if (targetPlayer.isCrouching()) {
                nextState = State.CROUCHING;
            } else if (targetPlayer.isWallPushing()) {
                nextState = State.AT_WALL;
            } else {
                if (targetPlayer.isAccelerating()) {
                    //TODO: figure out how to make this better (maybe add side that the player is facing)
                    if (Math.abs(targetPlayer.getxSpeed()) >= PlatformingConstants.getMaxWalkSpeed()
                                && targetPlayer.getCanRun()) {
                            nextState = State.RUNNING;
                        } else if (Math.abs(targetPlayer.getxSpeed()) > 0) {
                            nextState = State.WALKING;
                        }
                } else if (targetPlayer.getxSpeed() == 0) {
                    nextState = State.IDLE;
                } else {
                    nextState = State.SLIDING;
                }
            }
        } else {
            if (targetPlayer.isJumping()) {
                nextState = State.JUMPING;
            } else if (targetPlayer.isDoubleJumping()) {
                nextState = State.DOUBLE_JUMPING;
            } else if (targetPlayer.isWallJumping()) {
                nextState = State.WALL_JUMPING;
            } else if (targetPlayer.isWallPushing()) {
                nextState = State.WALL_FALLING;
            } else {
                nextState = State.FALLING;
            }
        }

        return nextState;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(Player player) {
        this.targetPlayer = player;
    }
}