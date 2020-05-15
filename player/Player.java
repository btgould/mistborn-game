package player;

import levels.Metal;
import levels.Platform;

import java.awt.event.KeyEvent;

import player.state.*;
import player.platforming.*;

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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
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
                this.state = stateManager.getNextState(this.state);
                break;
        }
    }

    // movement methods
    // -----------------------------------------------------------------------------------------------
    private void checkForWalkAcc() {
        this.wallPushing = false;

        // speed up to the left
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * PlatformingConstants.getMaxWalkSpeed()
                && !this.crouching) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= PlatformingConstants.getWalkAcc();
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_RIGHT) && this.xSpeed < PlatformingConstants.getMaxWalkSpeed()
                && !this.crouching) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += PlatformingConstants.getWalkAcc();
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        if (Math.abs(this.xSpeed) > PlatformingConstants.getMaxWalkSpeed()) {
            if (this.xSpeed > 0) {
                this.xSpeed--;
            } else {
                this.xSpeed++;
            }

            if (Math.abs(this.xSpeed) < PlatformingConstants.getMaxWalkSpeed()) {
                if (this.xSpeed > 0) {
                    this.xSpeed = PlatformingConstants.getMaxWalkSpeed();
                } else {
                    this.xSpeed = -1 * PlatformingConstants.getMaxWalkSpeed();
                }
            }
        }
    }

    private void checkForAirAcc() {
        this.wallPushing = false;

        if (this.canRun) {
            PlatformingConstants.setMaxAirSpeed(PlatformingConstants.getMaxRunSpeed());
        } else {
            PlatformingConstants.setMaxAirSpeed(PlatformingConstants.getMaxWalkSpeed());
        }

        // speed up to the left
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * PlatformingConstants
                .getMaxAirSpeed()) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= PlatformingConstants.getAirAcc();
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_RIGHT) && this.xSpeed < PlatformingConstants
                .getMaxAirSpeed()) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += PlatformingConstants.getAirAcc();
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

    }

    private void checkForRunAcc() {
        this.wallPushing = false;

        // speed up to the left
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * PlatformingConstants
                .getMaxRunSpeed()
                && !this.crouching) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= PlatformingConstants.getRunAcc();
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (controllers.KeyTracker.getKeysPressed().contains(KeyEvent.VK_RIGHT) && this.xSpeed < PlatformingConstants
                .getMaxRunSpeed()
                && !this.crouching) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += PlatformingConstants.getRunAcc();
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
            this.xSpeed *= PlatformingConstants.getFriction();

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
            this.ySpeed = PlatformingConstants.getFullJumpSpeed();

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
            this.ySpeed = PlatformingConstants.getDoubleJumpSpeed();

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
            this.ySpeed = PlatformingConstants.getWallJumpYSpeed();
            this.jumpReleased = false;

            if (this.wallSide == Side.RIGHT) {
                this.xSpeed = -1 * PlatformingConstants.getWallJumpXSpeed();
            } else if (this.wallSide == Side.LEFT) {
                this.xSpeed = PlatformingConstants.getWallJumpXSpeed();
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
            this.ySpeed += PlatformingConstants.getGravity();
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