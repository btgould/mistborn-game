package com.pisoft.mistborn_game.player.state;

import com.pisoft.mistborn_game.player.*;
import com.pisoft.mistborn_game.player.constants.PlatformingConstants;

public class StateManager {
    private Player targetPlayer; 

    public State getNextState(final State currentState) {
        State nextState = currentState;

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