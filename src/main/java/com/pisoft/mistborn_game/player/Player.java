package com.pisoft.mistborn_game.player;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.levels.Metal;
import com.pisoft.mistborn_game.levels.Platform;
import com.pisoft.mistborn_game.player.constants.*;
import com.pisoft.mistborn_game.player.game_events.*;
import com.pisoft.mistborn_game.player.state.State;
import com.pisoft.mistborn_game.player.state.StateManager;

public class Player implements GameEventDispatcher {

	private double width = 50;
	private double height = 100;

	// used to communicate with other objects
	// -----------------------------------------------------------------------------------------------
	private StateManager stateManager;

	// describe player state
	// -----------------------------------------------------------------------------------------------
	private State state;
	private double xPos = 350;
	private double yPos = 190;
	private double xSpeed = 0;
	private double ySpeed = 0;
	private double xAcc = 0;
	private double yAcc = 0;
	private double xPushAmount = 0;
	private double yPushAmount = 0;

	private Side facingSide;
	private boolean wantsToAccelerate;
	private boolean accelerating;
	private boolean walking;
	private boolean running;
	private boolean canRun;
	private boolean sliding;
	private boolean crouching;

	private boolean grounded;
	private boolean falling;
	private boolean landing;
	private boolean canJump;
	private boolean jumping;
	private boolean canDoubleJump;
	private boolean doubleJumping;
	private Side lastWallJumpSide;
	private boolean wallJumping;
	private boolean jumpReleased = true;

	private boolean atWall;
	private Side wallSide;
	private boolean wallPushing;

	private Metal targetMetal;
	private boolean steelPushing;

	// -----------------------------------------------------------------------------------------------
	public Player() {
		this.state = State.IDLE;

		registerStateManager(new StateManager());
	}

	public void tick() {
		fall();
		move();
		setState(stateManager.getNextState(getState()));
	}

	// movement methods
	// -----------------------------------------------------------------------------------------------
	private void checkIfFalling() {
		yPos++;

		if (!collided() && !isFalling()) {
			dispatchEvent(new StartFallEvent());
		}

		yPos--;
	}

	private void checkIfAtWall() {
		if (getWallSide() != Side.NONE) {
			Side sideChecked = Side.NONE;

			if (this.getWallSide() == Side.RIGHT) {
				this.xPos++;
				sideChecked = Side.RIGHT;
			} else if (this.getWallSide() == Side.LEFT) {
				this.xPos--;
				sideChecked = Side.LEFT;
			}

			if (!collided()) {
				dispatchEvent(new LeavingWallEvent(sideChecked));
			}

			if (sideChecked == Side.RIGHT) {
				this.xPos--;
			} else if (sideChecked == Side.LEFT) {
				this.xPos++;
			}
		}
	}

	// returns true if overlapping with a platform
	private boolean collided() {
		// check every platform for collision
		for (Platform platform : Game.getActiveLevel().getPlatforms()) {
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

	private void handleCollision() {
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
					dispatchEvent(new HitFloorEvent());
				} else {
					dispatchEvent(new HitCeilingEvent());
				}
			}

			if (causedByX) {
				// player hit a wall
				Side direction = (getxSpeed() > 0) ? Side.RIGHT : Side.LEFT;
				dispatchEvent(new HitWallEvent(direction));
			}
		}
	}

	private void fall() {
		if (this.falling == true) {
			this.ySpeed += PlatformingConstants.getGravity();
		}
	}

	private void move() {
		accelerate();

		doMetalMovement();
		xSpeed += xPushAmount;
		ySpeed -= yPushAmount;

		xPos += xSpeed;
		yPos += ySpeed;

		handleCollision();
		checkIfFalling();
		checkIfAtWall();

		if (isSliding()) {
			dispatchEvent(new SlidingEvent());
		} else if (Math.abs(getxSpeed()) > PlatformingConstants.getMaxWalkSpeed() && !getCanRun() && isGrounded()) {
			// moving faster than maxWalkSpeed, can't run, and grounded --> slow to walk
			Side direction = (getxSpeed() > 0) ? Side.RIGHT : Side.LEFT;

			dispatchEvent(new SlowToWalkEvent(direction));
		}
	}

	private void accelerate() {
		if (isGrounded()) {
			if (isWalking()) {
				// ensure player does not exceed max walk speed
				if (Math.abs(getxSpeed() + getxAcc()) > PlatformingConstants.getMaxWalkSpeed()
						&& ((getxAcc() > 0 && getxSpeed() > 0) || (getxAcc() < 0 && getxSpeed() < 0))) {
					dispatchEvent(new MaxWalkSpeedReachedEvent(getFacingSide()));
				}
			} else if (isRunning()) {
				// ensure player does not exceed max run speed
				if (Math.abs(getxSpeed() + getxAcc()) > PlatformingConstants.getMaxRunSpeed()
						&& ((getxAcc() > 0 && getxSpeed() > 0) || (getxAcc() < 0 && getxSpeed() < 0))) {
					dispatchEvent(new MaxRunSpeedReachedEvent(getFacingSide()));
				}
			}
		} else {
			// ensure player does not exceed max air speed
			if (Math.abs(getxSpeed() + getxAcc()) > PlatformingConstants.getMaxAirSpeed()
					&& ((getxAcc() > 0 && getxSpeed() > 0) || (getxAcc() < 0 && getxSpeed() < 0))) {
				dispatchEvent(new MaxAirSpeedReachedEvent(getFacingSide()));
			}
		}

		setxSpeed(getxSpeed() + getxAcc());
	}

	private void doMetalMovement() {
		if (getTargetMetal() != null && isSteelPushing() == true) {
			// set xPush and yPush based off of relative location to metal
			double direction = 0;
			double magnitude = 0;

			double x1 = getxPos() + (getWidth() / 2);
			double x2 = targetMetal.getxPos() + 2.5;
			double y1 = getyPos() + (getHeight() / 2);
			double y2 = targetMetal.getyPos() + 2.5;

			magnitude = MetalPushingConstants.getPushStrength() * Math.pow(Math.hypot(x1 - x2, y1 - y2), -2);
			direction = (x1 - x2 > 0) ? Math.atan((y1 - y2) / (x1 - x2)) : Math.atan((y1 - y2) / (x1 - x2)) + Math.PI;

			setxPushAmount(Math.abs(magnitude * Math.cos(direction)) < 0.3 ? 0 : magnitude * Math.cos(direction));
			setyPushAmount(Math.abs(magnitude * Math.sin(direction)) < 0.3 ? 0 : -magnitude * Math.sin(direction));

			if (Math.abs(magnitude * Math.cos(direction)) < 0.3 && Math.abs(magnitude * Math.sin(direction)) < 0.3
					&& isGrounded() && !isAccelerating() && getxSpeed() != 0) {
				setSliding(true);
			}
		} else {
			// no metal is targeted, or player is not pressing button for push -> zero push
			setxPushAmount(0);
			setyPushAmount(0);
		}
	}

	// register methods
	// -----------------------------------------------------------------------------------------------
	private void registerStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
		stateManager.setTargetPlayer(this);
	}

	// getters / setters
	// -----------------------------------------------------------------------------------------------
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
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

	public double getxAcc() {
		return xAcc;
	}

	public void setxAcc(double xAcc) {
		this.xAcc = xAcc;
	}

	public double getyAcc() {
		return yAcc;
	}

	public void setyAcc(double yAcc) {
		this.yAcc = yAcc;
	}

	public double getxPushAmount() {
		return xPushAmount;
	}

	public void setxPushAmount(double xPushAcc) {
		this.xPushAmount = xPushAcc;
	}

	public double getyPushAmount() {
		return yPushAmount;
	}

	public void setyPushAmount(double yPushAcc) {
		this.yPushAmount = yPushAcc;
	}

	public Side getFacingSide() {
		return facingSide;
	}

	public void setFacingSide(Side facingSide) {
		this.facingSide = facingSide;
	}

	public boolean wantsToAccelerate() {
		return wantsToAccelerate;
	}

	public void setWantsToAccelerate(boolean wantsToAccelerate) {
		this.wantsToAccelerate = wantsToAccelerate;
	}

	public boolean isAccelerating() {
		return accelerating;
	}

	public void setAccelerating(boolean accelerating) {
		this.accelerating = accelerating;
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
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

	public boolean isCrouching() {
		return crouching;
	}

	public void setCrouching(boolean crouching) {
		this.crouching = crouching;
	}

	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public boolean isLanding() {
		return landing;
	}

	public void setLanding(boolean landing) {
		this.landing = landing;
	}

	public boolean canJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean canDoubleJump() {
		return canDoubleJump;
	}

	public void setCanDoubleJump(boolean canDoubleJump) {
		this.canDoubleJump = canDoubleJump;
	}

	public boolean isDoubleJumping() {
		return doubleJumping;
	}

	public void setDoubleJumping(boolean doubleJumping) {
		this.doubleJumping = doubleJumping;
	}

	public Side getLastWallJumpSide() {
		return lastWallJumpSide;
	}

	public void setLastWallJumpSide(Side lastWallJumpSide) {
		this.lastWallJumpSide = lastWallJumpSide;
	}

	public boolean isWallJumping() {
		return wallJumping;
	}

	public void setWallJumping(boolean wallJumping) {
		this.wallJumping = wallJumping;
	}

	public boolean isJumpReleased() {
		return jumpReleased;
	}

	public void setJumpReleased(boolean jumpReleased) {
		this.jumpReleased = jumpReleased;
	}

	public boolean isAtWall() {
		return atWall;
	}

	public void setAtWall(boolean atWall) {
		this.atWall = atWall;
	}

	public Side getWallSide() {
		return wallSide;
	}

	public void setWallSide(Side wallSide) {
		this.wallSide = wallSide;
	}

	public boolean isWallPushing() {
		return wallPushing;
	}

	public void setWallPushing(boolean wallPushing) {
		this.wallPushing = wallPushing;
	}

	public Metal getTargetMetal() {
		return targetMetal;
	}

	public void setTargetMetal(Metal targetMetal) {
		this.targetMetal = targetMetal;
	}

	public boolean isSteelPushing() {
		return steelPushing;
	}

	public void setSteelPushing(boolean steelPushing) {
		this.steelPushing = steelPushing;
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
}
