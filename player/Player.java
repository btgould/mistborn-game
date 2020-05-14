package player;

import levels.Metal;
import levels.Platform;

import java.awt.event.KeyEvent;

import player.state.*;

public class Player {

    private double xPos = 350;
    private double yPos = 190;
    private double xSpeed = 0;
    private double ySpeed = 0;

    private double width = 50;
    private double height = 100;

    // used to communicate with other objects
    // -----------------------------------------------------------------------------------------------
    private StateManager stateManager; 

    // used to tweak the feel of the movement
    // -----------------------------------------------------------------------------------------------
    private double gravity = 1; // amount that ySpeed is changed for every frame the player falls
    private double airAcc = 0.5; // amount that xSpeed is changed for every frame the player moves in air
    private double walkAcc = 1; // amount that xSpeed is changed for every frame the player walks
    private double runAcc = 1; // amount that xSpeed is changed for every frame the player runs
    private double maxAirSpeed; // max horiz airSpeed of player (set based on if player can run or not)
    private double maxWalkSpeed = 8; // max speed the player can walk
    private double maxRunSpeed = 15; // max speed the player can run
    private double friction = 0.8; // proportion of speed that remains per frame while sliding
    private double fullJumpSpeed = -17; // initial ySpeed when the player jumps
    private double shortJumpSpeed = -10; // ySpeed to set if the player releases jump early
    private double doubleJumpSpeed = -15; // initial ySpeed when the player double jumps
    private double wallJumpYSpeed = -15; // initial ySpeed when the player wall jumps
    private double wallJumpXSpeed = 10; // initial xSpeed away from wall when the player wall jumps

    private boolean canJump;
    private boolean canDoubleJump;
    private boolean jumpReleased;

    // used to determine player state. not equivalent to state,
    // because player can be accelerating and falling, for example
    // -----------------------------------------------------------------------------------------------
    private boolean accelerating;
    private boolean canRun;
    private boolean sliding;

    private boolean grounded;
    private boolean jumping;
    private boolean doubleJumping;
    private boolean wallJumping;
    private boolean falling;

    private boolean crouching;
    private boolean atWall;
    private boolean wallPushing;

    private Side wallSide;
    private Side lastWallJumpSide;

    private State state;

    private Metal targetedMetal;


    // -----------------------------------------------------------------------------------------------
    public Player() {
        this.state = State.IDLE;

        registerStateManager(new StateManager());
    }

    public double getShortJumpSpeed() {
		return shortJumpSpeed;
	}

	public void setShortJumpSpeed(double shortJumpSpeed) {
		this.shortJumpSpeed = shortJumpSpeed;
	}

	public double getMaxWalkSpeed() {
		return maxWalkSpeed;
	}

	public void setMaxWalkSpeed(double maxWalkSpeed) {
		this.maxWalkSpeed = maxWalkSpeed;
	}

	public void tick() {

        switch (state) {
            case IDLE:
                // assumptions from being in IDLE state
                this.accelerating = false;
                this.sliding = false;
                this.grounded = true;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = false;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForJump();
                checkForCrouch();

                // move
                move();
                checkForCollision();
                checkIfFalling();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.wallPushing) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.accelerating) {
                        // grounded, not crouching, not wall pushing, accelerating -> WALKING
                        this.state = State.WALKING;
                    }
                } else {
                    if (this.jumping) {
                        // not grounded, jumping -> JUMPING
                        this.state = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        this.state = State.FALLING;
                    }
                }
                // grounded, not crouching, not wall pushing, not accelerating -> stay in IDLE
                break;

            case WALKING:
                // assumptions from being in WALKING state
                this.accelerating = true;
                this.sliding = false;
                this.grounded = true;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = false;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForRunAbility();
                checkForJump();
                checkForCrouch();
                checkForFriction();

                // move
                move();
                checkForCollision();
                checkIfFalling();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.wallPushing) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.sliding) {
                        // grounded, not crouching, not wall pushing, sliding -> SLIDING
                        this.state = State.SLIDING;
                    } else if (Math.abs(this.xSpeed) >= this.getMaxWalkSpeed() && this.canRun) {
                        // grounded, not crouching, not wall pushing, not sliding, speed >= max walk
                        // speed and can run -> RUNNING
                        this.state = State.RUNNING;
                    } else if (this.xSpeed == 0 && !this.accelerating) {
                        // grounded, not crouching, not wall pushing, not sliding, speed = 0 -> IDLE
                        // needed because otherwise if xSpeed = 1 when released, checkForFriction sets
                        // sliding to false
                        this.state = State.IDLE;
                    }
                } else {
                    if (this.jumping) {
                        // not grounded, jumping -> JUMPING
                        this.state = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        this.state = State.FALLING;
                    }
                }
                // grounded, not crouching, not at wall, not sliding, speed < max walk speed or
                // cant run -> stay in WALKING
                break;

            case RUNNING:
                // assumptions from being in RUNNING state
                this.accelerating = true;
                this.sliding = false;
                this.grounded = true;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = false;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForRunAcc();
                checkForRunAbility();
                checkForJump();
                checkForCrouch();
                checkForFriction();

                // move
                move();
                checkForCollision();
                checkIfFalling();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.wallPushing) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.sliding) {
                        // grounded, not crouching, not wall pushing, sliding -> SLIDING
                        this.state = State.SLIDING;
                    } else if (!this.canRun) {
                        // TODO: should this also check for if speed < max walk speed?
                        // grounded, not crouching, not wall pushing, not sliding, cant run -> WALKING
                        this.state = State.WALKING;
                    }
                } else {
                    if (this.jumping) {
                        // not grounded, jumping -> JUMPING
                        this.state = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        this.state = State.FALLING;
                    }
                }
                // grounded, not crouching, not wall pushing, not sliding, can run -> stay in
                // RUNNING
                break;

            case SLIDING:
                // assumptions from being in SLIDING state
                this.accelerating = false;
                this.sliding = true;
                this.grounded = true;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = false;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForRunAbility();
                checkForJump();
                checkForCrouch();
                checkForFriction();

                // move
                move();
                checkForCollision();
                checkIfFalling();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.wallPushing) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.accelerating) {
                        // grounded, not crouching, not wall pushing, accelerating -> determine based on
                        // speed & run ability
                        if (Math.abs(this.xSpeed) >= this.getMaxWalkSpeed() && this.canRun) {
                            this.state = State.RUNNING;
                        } else if (Math.abs(this.xSpeed) > 0) {
                            this.state = State.WALKING;
                        }
                    } else if (this.xSpeed == 0) {
                        // grounded, not crouching, not wall pushing, not accelerating, speed = 0 ->
                        // IDLE
                        this.state = State.IDLE;
                    }
                } else {
                    if (this.jumping) {
                        // not grounded, jumping -> JUMPING
                        this.state = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        this.state = State.FALLING;
                    }
                }
                // grounded, not crouching, not wall pushing, not accelerating, speed != 0 ->
                // stay in SLIDING
                break;

            case JUMPING:
                // assumptions from being in JUMPING state
                this.sliding = false;
                this.grounded = false;
                this.jumping = true;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = true;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForAirAcc();
                checkForRunAbility();
                fall();

                // move
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                if (!controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_UP)
                        || Math.abs(this.ySpeed) <= Math.abs(this.getShortJumpSpeed())) {
                    this.ySpeed = this.getShortJumpSpeed();

                    if (this.wallPushing) {
                        // up not pressed or ySpeed too low, wall pushing -> WALL_FALLING
                        this.state = State.WALL_FALLING;
                    } else {
                        // up not presses or ySpeed too low, not wall pushing -> FALLING
                        this.state = State.FALLING;
                    }
                }
                // up pressed, ySpeed higher than shortJumpSpeed -> stay in JUMPING
                break;

            case DOUBLE_JUMPING:
                // TODO: make double jumping state last for more than one frame
                // assumptions from being in DOUBLE_JUMPING state
                this.sliding = false;
                this.grounded = false;
                this.jumping = false;
                this.doubleJumping = true;
                this.wallJumping = false;
                this.falling = true;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForAirAcc();
                checkForRunAbility();
                fall();

                // move
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                // TODO: can double jumping go into landing?
                if (this.grounded) {
                    // grounded -> LANDING
                    this.state = State.LANDING;
                } else if (this.wallPushing) {
                    // not grounded, wallPushing -> WALL_FALLING
                    this.state = State.WALL_FALLING;
                } else {
                    // not grounded, not wallPushing -> FALLING
                    this.state = State.FALLING;
                }
                break;

            case FALLING:
                // assumptions from being in FALLING state
                this.sliding = false;
                this.grounded = false;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = true;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForAirAcc();
                checkForRunAbility();
                checkForDoubleJump();
                fall();

                // move
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                if (this.grounded) {
                    // grounded -> LANDING
                    this.state = State.LANDING;
                } else if (this.doubleJumping) {
                    // not grounded, double jumping -> DOUBLE_JUMPING
                    this.state = State.DOUBLE_JUMPING;
                } else if (this.wallPushing) {
                    // not grounded, not double jumping, wall pushing -> WALL_FALLING
                    this.state = State.WALL_FALLING;
                }
                // not grounded, not double jumping, not wall pushing -> stay in FALLING
                break;

            case WALL_FALLING:
                // assumptions from being in WALL_FALLING state
                this.accelerating = false;
                this.sliding = false;
                this.grounded = false;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = true;
                this.crouching = false;
                this.wallPushing = true;

                // check if assumptions were true
                checkForAirAcc();
                checkForRunAbility();
                checkForWallJump();
                checkForDoubleJump();
                fall();

                // move
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                if (this.grounded) {
                    // grounded -> LANDING
                    this.state = State.LANDING;
                } else if (this.wallJumping) {
                    // not grounded, wall jumping -> WALL_JUMPING
                    this.state = State.WALL_JUMPING;
                } else if (this.doubleJumping) {
                    // not grounded, not wall jumping, double jumping -> DOUBLE_JUMPING
                    this.state = State.DOUBLE_JUMPING;
                } else if (!this.wallPushing) {
                    // not grounded, not wall jumping, not double jumping, not wall pushing ->
                    // FALLING
                    this.state = State.FALLING;
                }
                // not grounded, not wall jumping, not double jumping, wall pushing -> stay in
                // WALL_FALLING
                break;

            case WALL_JUMPING:
                // TODO: make wall jumping state last for more than one frame
                // assumptions from being in WALL_JUMPING state
                this.accelerating = true;
                this.sliding = false;
                this.grounded = false;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = true;
                this.falling = true;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForAirAcc();
                checkForRunAbility();
                fall();

                // move
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                // TODO: can wall jumping go into landing or wall falling?
                if (this.grounded) {
                    // grounded -> LANDING
                    this.state = State.LANDING;
                } else if (this.wallPushing) {
                    // not grounded, wallPushing -> WALL_FALLING
                    this.state = State.WALL_FALLING;
                } else {
                    // not grounded, not wallPushing -> FALLING
                    this.state = State.FALLING;
                }
                break;

            // TODO: make landing state last for more than one frame
            case LANDING:
                // assumptions from being in LANDING state
                this.accelerating = true;
                this.sliding = false;
                this.grounded = true;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = false;
                this.crouching = false;
                this.wallPushing = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForFriction();

                // move
                move();
                checkForCollision();
                checkIfFalling();

                // determine next state
                if (this.sliding) {
                    // sliding (triggered by not pressing left or right) -> SLIDING
                    this.state = State.SLIDING;
                } else if (this.xSpeed >= this.getMaxWalkSpeed() && this.canRun) {
                    // not sliding -> determine based on xSpeed & run ability
                    this.state = State.RUNNING;
                } else if (Math.abs(this.xSpeed) > 0) {
                    this.state = State.WALKING;
                } else {
                    this.state = State.IDLE;
                }
                break;

            // TODO: actually change height when crouching, force crouch when space too
            // small
            case CROUCHING:
                // assumptions from being in CROUCHING state
                this.accelerating = false;
                this.sliding = true;
                this.grounded = true;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = false;
                this.crouching = true;
                this.wallPushing = false;

                // check if assumptions were true
                checkForCrouch();
                checkForFriction();

                // move
                move();
                checkForCollision();
                checkIfFalling();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded, crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.wallPushing) {
                        // grounded, not crouching, wall pushing -> AT_WALL
                        this.state = State.AT_WALL;
                    } else {
                        if (this.accelerating) {
                            // grounded, not wall pushing, not crouching, and accelerating -> set based on
                            // speed & run ability
                            if (Math.abs(this.xSpeed) >= this.getMaxWalkSpeed() && this.canRun) {
                                this.state = State.RUNNING;
                            } else if (Math.abs(this.xSpeed) > 0) {
                                this.state = State.WALKING;
                            }
                        } else if (this.xSpeed == 0) {
                            // grounded, not wall pushing, not crouching, not accelerating, speed = 0 ->
                            // IDLE
                            this.state = State.IDLE;
                        } else {
                            // grounded, not wall pushing, not crouching, not accelerating, speed != 0 ->
                            // SLIDING
                            this.state = State.SLIDING;
                        }
                    }
                } else {
                    // not grounded -> FALLING (b/c cant jump)
                    this.state = State.FALLING;
                }
                break;

            case AT_WALL:
                // assumptions from being in AT_WALL state
                this.accelerating = false;
                this.sliding = false;
                this.grounded = true;
                this.jumping = false;
                this.doubleJumping = false;
                this.wallJumping = false;
                this.falling = false;
                this.crouching = false;
                this.wallPushing = true;

                // check if assumptions are true
                checkForWalkAcc();
                checkForJump();
                checkForCrouch();
                checkForFriction();

                // move
                move();
                checkForCollision();
                checkIfFalling();
                checkIfAtWall();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded, crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.accelerating) {
                        // grounded, not crouching, accelerating (can only accelerate away from wall) ->
                        // WALKING
                        this.state = State.WALKING;
                    } else if (!this.wallPushing) {
                        // grounded, not crouching, not accelerating, not wall pushing -> IDLE
                        this.state = State.IDLE;
                    }
                } else {
                    // not grounded -> JUMPING
                    this.state = State.JUMPING;
                }
                // grounded, not crouching, not accelerating, wallPushing -> stay in AT_WALL
                break;
        }
    }

    // movement methods
    // -----------------------------------------------------------------------------------------------
    private void checkForWalkAcc() {
        this.wallPushing = false;

        // speed up to the left
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * getMaxWalkSpeed()
                && !this.crouching) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= this.walkAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_RIGHT) && this.xSpeed < getMaxWalkSpeed()
                && !this.crouching) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += this.walkAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        if (Math.abs(this.xSpeed) > this.getMaxWalkSpeed()) {
            if (this.xSpeed > 0) {
                this.xSpeed--;
            } else {
                this.xSpeed++;
            }

            if (Math.abs(this.xSpeed) < this.getMaxWalkSpeed()) {
                if (this.xSpeed > 0) {
                    this.xSpeed = this.getMaxWalkSpeed();
                } else {
                    this.xSpeed = -1 * this.getMaxWalkSpeed();
                }
            }
        }
    }

    private void checkForAirAcc() {
        this.wallPushing = false;

        if (this.canRun) {
            this.maxAirSpeed = this.maxRunSpeed;
        } else {
            this.maxAirSpeed = this.getMaxWalkSpeed();
        }

        // speed up to the left
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * maxAirSpeed) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= this.airAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_RIGHT) && this.xSpeed < maxAirSpeed) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += this.airAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

    }

    private void checkForRunAcc() {
        this.wallPushing = false;

        // speed up to the left
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * maxRunSpeed
                && !this.crouching) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= this.runAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_RIGHT) && this.xSpeed < maxRunSpeed
                && !this.crouching) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += this.runAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }
    }

    private void checkForRunAbility() {
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_SHIFT)) {
            this.canRun = true;
        } else {
            this.canRun = false;
        }
    }

    private void checkForFriction() {
        if ((!(controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_RIGHT))
                && !(controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_LEFT)))
                || this.state == State.CROUCHING) {
            this.xSpeed *= this.friction;

            this.sliding = true;

            if (Math.abs(this.xSpeed) <= 1) {
                this.xSpeed = 0;
                this.sliding = false;
            }
        }
    }

    private void checkForJump() {
        // jump
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_UP) && this.canJump == true) {
            this.ySpeed = this.fullJumpSpeed;

            this.jumpReleased = false;
            this.canJump = false;
            this.grounded = false;
            this.falling = true;
            this.jumping = true;

            this.lastWallJumpSide = Side.NONE;
        }
    }

    private void checkForDoubleJump() {
        if (!controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_UP)) {
            this.jumpReleased = true;
        }

        if (this.jumpReleased && controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_UP) && this.canDoubleJump
                && !this.wallJumping) {
            this.ySpeed = this.doubleJumpSpeed;

            this.canDoubleJump = false;
            this.doubleJumping = true;
            this.jumpReleased = false;
        }
    }

    private void checkForWallJump() {
        if (!controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_UP)) {
            this.jumpReleased = true;
        }

        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_UP) && this.wallPushing
                && this.wallSide != this.lastWallJumpSide && this.jumpReleased) {
            this.ySpeed = this.wallJumpYSpeed;
            this.jumpReleased = false;

            if (this.wallSide == Side.RIGHT) {
                this.xSpeed = -1 * this.wallJumpXSpeed;
            } else if (this.wallSide == Side.LEFT) {
                this.xSpeed = this.wallJumpXSpeed;
            }

            this.lastWallJumpSide = this.wallSide;

            this.wallJumping = true;
        }

    }

    private void checkForCrouch() {
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_DOWN) && this.grounded == true) {
            this.crouching = true;
        } else {
            this.crouching = false;
        }
    }

    private void checkIfFalling() {
        this.yPos++;

        if (!collided()) {
            this.falling = true;
            this.canJump = false;
            this.grounded = false;
        }

        this.yPos--;
    }

    private void checkIfAtWall() {
        Side sideChecked = Side.NONE;

        if (this.wallSide == Side.RIGHT) {
            this.xPos++;
            sideChecked = Side.RIGHT;
        } else if (this.wallSide == Side.LEFT) {
            this.xPos--;
            sideChecked = Side.LEFT;
        }

        if (!collided()) {
            this.atWall = false;
            this.wallSide = Side.NONE;
        }

        if (sideChecked == Side.RIGHT) {
            this.xPos--;
        } else if (sideChecked == Side.LEFT) {
            this.xPos++;
        }
    }

    // returns true if overlapping with a platform
    private boolean collided() {
        // check every platform for collision
        for (int platNum = 0; platNum < Platform.getPlatforms().size(); platNum++) {
            Platform platform = Platform.getPlatforms().get(platNum);
            boolean onSameX = false;
            boolean onSameY = false;

            if (this.xPos + this.width + 2 > platform.getxPos()
                    && platform.getxPos() + platform.getWidth() + 2 > this.xPos) {
                onSameX = true;
            }

            if (this.yPos + this.height + 2 > platform.getyPos()
                    && platform.getyPos() + platform.getHeight() + 2 > this.yPos) {
                onSameY = true;
            }

            // if player is in the same space as any platform, return true
            if (onSameX && onSameY) {
                return true;
            }
        }
        // if it gets here, player is not in the same space as any platform, so return
        // false
        return false;
    }

    // TODO: collision bugs
    // colliding with roof works weirdly
    // can occasionally jump to top of a wall by colliding weirldly with floor
    private void checkForCollision() {
        if (collided()) {
            double slope = Math.abs(this.ySpeed / this.xSpeed);

            double xOffset = 0;
            double yOffset = 0;

            int xTickAmount;
            int yTickAmount;

            if (this.xSpeed != 0) {
                xTickAmount = (this.xSpeed > 0) ? -1 : 1;
            } else {
                xTickAmount = 0;
            }

            if (this.ySpeed != 0) {
                yTickAmount = (this.ySpeed > 0) ? -1 : 1;
            } else {
                yTickAmount = 0;
            }

            boolean causedByX = false;
            boolean causedByY = false;

            this.xPos -= this.xSpeed;
            if (collided()) {
                // collision caused by y movement
                causedByY = true;
            }
            this.xPos += this.xSpeed;
            this.yPos -= this.ySpeed;
            if (collided()) {
                // collision caused by x movement
                causedByX = true;
            }
            this.yPos += this.ySpeed;

            if (causedByX) {
                this.xPos = Math.round(this.xPos);
            }
            if (causedByY) {
                this.yPos = Math.round(this.yPos);
            }

            while (collided()) {
                if (xOffset == 0 && yOffset == 0) {
                    if (slope >= 1) {
                        // tick y
                        this.yPos += yTickAmount;
                        yOffset++;
                    } else {
                        // works b/c both cannot be 0, or player would never collide
                        // tick x
                        this.xPos += xTickAmount;
                        xOffset++;
                    }
                } else if (yOffset / xOffset >= slope) {
                    // tick x
                    this.xPos += xTickAmount;
                    xOffset++;
                } else {
                    // tick y
                    this.yPos += yTickAmount;
                    yOffset++;
                }
            }

            if (causedByY) {
                // player hit a floor or a ceiling
                if (this.ySpeed > 0) {
                    this.grounded = true;
                    this.canJump = true;
                    this.canDoubleJump = true;
                    this.lastWallJumpSide = Side.NONE;
                    this.falling = false;
                }

                this.ySpeed = 0;
            }

            if (causedByX) {
                // player hit a wall
                this.atWall = true;

                if (this.xSpeed > 0) {
                    this.wallSide = Side.RIGHT;
                } else if (this.xSpeed < 0) {
                    this.wallSide = Side.LEFT;
                }

                this.xSpeed = 0;
            }
        }
    }

    private void fall() {
        if (this.falling == true) {
            this.ySpeed += this.gravity;
        }
    }

    private void move() {
        this.xPos += this.xSpeed;
        this.yPos += this.ySpeed;
    }

    //register methods
    // -----------------------------------------------------------------------------------------------
    private void registerStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
        stateManager.setTargetPlayer(this);
    }

    // getters / setters
    // -----------------------------------------------------------------------------------------------
    public State getState() {
        return this.state;
    }

    public boolean isAccelerating() {
        return accelerating;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public boolean getCanRun() {
        return canRun;
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public boolean isSliding() {
        return sliding;
    }

    public void setSliding(boolean sliding) {
        this.sliding = sliding;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isDoubleJumping() {
        return doubleJumping;
    }

    public void setDoubleJumping(boolean doubleJumping) {
        this.doubleJumping = doubleJumping;
    }

    public boolean isWallJumping() {
        return wallJumping;
    }

    public void setWallJumping(boolean wallJumping) {
        this.wallJumping = wallJumping;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public boolean isAtWall() {
        return atWall;
    }

    public void setAtWall(boolean atWall) {
        this.atWall = atWall;
    }

    public boolean isWallPushing() {
        return wallPushing;
    }

    public void setWallPushing(boolean wallPushing) {
        this.wallPushing = wallPushing;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void setState(State state) {
        this.state = state;
    }
}