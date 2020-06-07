package com.pisoft.mistborn_game.player.platforming;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.levels.Platform;
import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.state.State;

public class ConditionManager {
	private Player targetPlayer;

	private double playerxPos;
	private double playeryPos;

	// Player conditions are:
	// accelerating; canRun; sliding; grounded; landing; jumping; doubleJumping;
	// wallJumping; falling; crouching; atWall; wallPushing; wallSide;
	// lastWallJumpSide; facingSide; canJump; canDoubleJump; jumpReleased;

	// button press events
	// ---------------------------------------------------------------------------------------------------
	public void jumpRequest() {
		if ((targetPlayer.getState() == State.IDLE || targetPlayer.getState() == State.WALKING
				|| targetPlayer.getState() == State.RUNNING || targetPlayer.getState() == State.SLIDING)
				&& targetPlayer.canJump()) {
			// if canJump and in a state where jumping is legal --> jump
			targetPlayer.setSliding(false);
			targetPlayer.setGrounded(false);
			targetPlayer.setLanding(false);
			targetPlayer.setJumping(true);
			targetPlayer.setDoubleJumping(false);
			targetPlayer.setWallJumping(false);
			targetPlayer.setFalling(true);
			targetPlayer.setCrouching(false);

			targetPlayer.setCanJump(false);
			targetPlayer.setJumpReleased(false);

			targetPlayer.setySpeed(PlatformingConstants.getFullJumpSpeed());
		}

		if (targetPlayer.getState() == State.FALLING && targetPlayer.canDoubleJump() && targetPlayer.isJumpReleased()) {
			// if canDoubleJump and falling --> double jump
			targetPlayer.setSliding(false);
			targetPlayer.setGrounded(false);
			targetPlayer.setLanding(false);
			targetPlayer.setJumping(false);
			targetPlayer.setDoubleJumping(true);
			targetPlayer.setWallJumping(false);
			targetPlayer.setFalling(true);
			targetPlayer.setCrouching(false);

			targetPlayer.setCanDoubleJump(false);
			targetPlayer.setJumpReleased(false);

			targetPlayer.setySpeed(PlatformingConstants.getDoubleJumpSpeed());
		}

		if (targetPlayer.getState() == State.WALL_FALLING && targetPlayer.isJumpReleased()) {
			if (targetPlayer.getWallSide() != targetPlayer.getLastWallJumpSide()) {
				// if in WALL_FALLING state and on other side since previous wall jump --> wall
				// jump
				targetPlayer.setSliding(false);
				targetPlayer.setGrounded(false);
				targetPlayer.setLanding(false);
				targetPlayer.setJumping(false);
				targetPlayer.setDoubleJumping(false);
				targetPlayer.setWallJumping(true);
				targetPlayer.setFalling(true);
				targetPlayer.setCrouching(false);

				targetPlayer.setLastWallJumpSide(targetPlayer.getWallSide());
				targetPlayer.setJumpReleased(false);

				targetPlayer.setxSpeed((targetPlayer.getWallSide() == Side.LEFT) ? PlatformingConstants.getWallJumpXSpeed()
								: -PlatformingConstants.getWallJumpXSpeed());
				targetPlayer.setySpeed(PlatformingConstants.getWallJumpYSpeed());
			} else if (targetPlayer.canDoubleJump()) {
				// if in WALL_FALLING state, on same side as previous wall jump, and
				// canDoubleJump --> double jump
				targetPlayer.setSliding(false);
				targetPlayer.setGrounded(false);
				targetPlayer.setLanding(false);
				targetPlayer.setJumping(false);
				targetPlayer.setDoubleJumping(true);
				targetPlayer.setWallJumping(false);
				targetPlayer.setFalling(true);
				targetPlayer.setCrouching(false);

				targetPlayer.setCanDoubleJump(false);
				targetPlayer.setJumpReleased(false);

				targetPlayer.setySpeed(PlatformingConstants.getDoubleJumpSpeed());
			}
		}
	}

	public void crouchRequest() {
		if (targetPlayer.getState() == State.IDLE || targetPlayer.getState() == State.WALKING
				|| targetPlayer.getState() == State.RUNNING || targetPlayer.getState() == State.SLIDING
				|| targetPlayer.getState() == State.AT_WALL) {
			// if in a state where crouching is legal --> crouch
			targetPlayer.setAccelerating(false);
			targetPlayer.setSliding(true);
			targetPlayer.setGrounded(true);
			targetPlayer.setLanding(false);
			targetPlayer.setJumping(false);
			targetPlayer.setDoubleJumping(false);
			targetPlayer.setWallJumping(false);
			targetPlayer.setFalling(false);
			targetPlayer.setCrouching(true);
		}
	}

	public void canRunRequest() {
		// when shift is pressed --> canRun
		targetPlayer.setCanRun(true);
	}

	public void leftRequest() {
		targetPlayer.setFacingSide(Side.LEFT);
		if (targetPlayer.getWallSide() == Side.LEFT) {
			// trying to move left with a wall to the left --> wallPush
			targetPlayer.setAccelerating(false);
			targetPlayer.setSliding(false);
			targetPlayer.setAtWall(true);
			targetPlayer.setWallPushing(true);
		} else if (targetPlayer.getState() != State.CROUCHING) {
			// trying to move left with no wall in the way --> move left
			targetPlayer.setAccelerating(true);
			targetPlayer.setSliding(false);
			targetPlayer.setAccelerating(true);
			targetPlayer.setAtWall(false);
			targetPlayer.setWallPushing(false);
			targetPlayer.setWallSide(Side.NONE);
		}
	}

	public void rightRequest() {
		targetPlayer.setFacingSide(Side.RIGHT);
		if (targetPlayer.getWallSide() == Side.RIGHT) {
			// trying to move right with a wall to the right --> wallPush
			targetPlayer.setAccelerating(false);
			targetPlayer.setSliding(false);
			targetPlayer.setAtWall(true);
			targetPlayer.setWallPushing(true);
		} else if (targetPlayer.getState() != State.CROUCHING) {
			// trying to move right with no wall in the way --> move right
			targetPlayer.setAccelerating(true);
			targetPlayer.setSliding(false);
			targetPlayer.setAccelerating(true);
			targetPlayer.setAtWall(false);
			targetPlayer.setWallPushing(false);
			targetPlayer.setWallSide(Side.NONE);
		}
	}

	// button release events
	// ---------------------------------------------------------------------------------------------------
	public void jumpRelease() {
		// jump released --> no longer jumping
		targetPlayer.setJumping(false);
		targetPlayer.setJumpReleased(true);
		
		if (targetPlayer.getySpeed() >= PlatformingConstants.getShortJumpSpeed()) {
			targetPlayer.setySpeed(PlatformingConstants.getShortJumpSpeed());
		}
	}

	public void crouchRelease() {
		// crouch released --> no longer crouching
		targetPlayer.setCrouching(false);
	}

	public void canRunRelease() {
		// canRun released --> can't run
		targetPlayer.setCanRun(false);
	}

	public void leftReleased() {
		// TODO: figure out how to handle when left and right are pressed at the same
		// time
		// leftReleased --> stop moving
		targetPlayer.setAccelerating(false);
		targetPlayer.setWallPushing(false);
		if (Math.abs(targetPlayer.getxSpeed()) > 0) {
			targetPlayer.setSliding(true);
		}
	}

	public void rightReleased() {
		// rightReleased --> stop moving
		targetPlayer.setAccelerating(false);
		targetPlayer.setWallPushing(false);
		if (Math.abs(targetPlayer.getxSpeed()) > 0) {
			targetPlayer.setSliding(true);
		}
	}

	// gameplay triggered events
	// ---------------------------------------------------------------------------------------------------

	// triggers player to fall if there is not a platform immediately below it
	public void checkIfFalling() {
		if (!isCollided(targetPlayer.getxPos(), targetPlayer.getyPos() + 1)) {
			targetPlayer.setFalling(true);
			targetPlayer.setCanJump(false);
			targetPlayer.setGrounded(false);
		}
	}

	// triggers player to be able to move horizonatally if there is no longer a wall
	// on targetPlayer.WallSide
	public void checkIfAtWall() {
		if (targetPlayer.getWallSide() != Side.NONE) {
			double sideOffset = (targetPlayer.getWallSide() == Side.RIGHT) ? 1 : -1;

			if (!isCollided(targetPlayer.getxPos() + sideOffset, targetPlayer.getyPos())) {
				targetPlayer.setAtWall(false);
				targetPlayer.setWallPushing(false);
				targetPlayer.setWallSide(Side.NONE);
			}
		}
	}

	// moves player so that they are no longer collided, and changes player
	// conditions accordingly
	public void handleCollision() {
		// TODO: replace xSpeed / ySpeed with total movement (i.e. include metalPushing)
		if (isCollided(targetPlayer.getxPos(), targetPlayer.getyPos())) {
			double slope = Math.abs(targetPlayer.getySpeed() / targetPlayer.getxSpeed());

			// how far has the player moved in each direction to not be collided
			double xOffset = 0;
			double yOffset = 0;

			// which way should the player move to get out of collision
			int xTickAmount;
			int yTickAmount;
			if (targetPlayer.getxSpeed() != 0) {
				xTickAmount = (targetPlayer.getxSpeed() > 0) ? -1 : 1;
			} else {
				xTickAmount = 0;
			}
			if (targetPlayer.getySpeed() != 0) {
				yTickAmount = (targetPlayer.getySpeed() > 0) ? -1 : 1;
			} else {
				yTickAmount = 0;
			}

			// which movement axis caused the collision
			boolean causedByX = false;
			boolean causedByY = false;
			if (isCollided(targetPlayer.getxPos() - targetPlayer.getxSpeed(), targetPlayer.getyPos())) {
				// collision caused by y movement
				causedByY = true;
			}
			if (isCollided(targetPlayer.getxPos(), targetPlayer.getyPos() - targetPlayer.getySpeed())) {
				// collision caused by x movement
				causedByX = true;
			}

			// set tracking variables to determine when you are no longer collided
			this.playerxPos = targetPlayer.getxPos();
			this.playeryPos = targetPlayer.getyPos();
			if (causedByX) {
				this.playerxPos = Math.round(targetPlayer.getxPos());
			}
			if (causedByY) {
				this.playeryPos = Math.round(targetPlayer.getyPos());
			}

			// determine closest position in which player would no longer be collided along
			// movement path
			while (isCollided(this.playerxPos, this.playeryPos)) {
				if (xOffset == 0 && yOffset == 0) {
					if (slope >= 1) {
						// tick y
						this.playeryPos += yTickAmount;
						yOffset++;
					} else {
						// works b/c both cannot be 0, or player would never collide
						// tick x
						this.playerxPos += xTickAmount;
						xOffset++;
					}
				} else if (yOffset / xOffset >= slope) {
					// tick x
					this.playerxPos += xTickAmount;
					xOffset++;
				} else {
					// tick y
					this.playeryPos += yTickAmount;
					yOffset++;
				}
			}

			// set player position to new non-collided position
			targetPlayer.setxPos(this.playerxPos);
			targetPlayer.setyPos(this.playeryPos);

			if (causedByY) {
				// player hit a floor or a ceiling
				if (targetPlayer.getySpeed() > 0) {
					targetPlayer.setGrounded(true);
					targetPlayer.setLanding(true);
					targetPlayer.setCanJump(true);
					targetPlayer.setCanDoubleJump(true);
					targetPlayer.setLastWallJumpSide(Side.NONE);
					targetPlayer.setFalling(false);
				}

				targetPlayer.setySpeed(0);
			}

			if (causedByX) {
				// player hit a wall
				targetPlayer.setAtWall(true);
				targetPlayer.setWallPushing(true);

				if (targetPlayer.getxSpeed() > 0) {
					targetPlayer.setWallSide(Side.RIGHT);
				} else if (targetPlayer.getxSpeed() < 0) {
					targetPlayer.setWallSide(Side.LEFT);
				}

				targetPlayer.setxSpeed(0);
			}
		}
	}

	// returns true / false based on whether or not player would be
	// collided at input xPos / yPos
	public boolean isCollided(double xPos, double yPos) {
		// check every platform for collision
		for (Platform platform : Game.getActiveLevel().getPlatforms()) {
			boolean onSameX = false;
			boolean onSameY = false;

			// TODO: change "2" into stroke width
			if (xPos + targetPlayer.getWidth() + 2 > platform.getxPos()
					&& platform.getxPos() + platform.getWidth() + 2 > xPos) {
				onSameX = true;
			}

			if (yPos + targetPlayer.getHeight() + 2 > platform.getyPos()
					&& platform.getyPos() + platform.getHeight() + 2 > yPos) {
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

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}
}
