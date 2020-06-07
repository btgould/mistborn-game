package com.pisoft.mistborn_game.player.platforming;

import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.Side;

public class PlatformingManager {
	private Player targetPlayer;

	private double xAcc;
	private double yAcc;

	public void fall() {
		if (targetPlayer.isFalling()) {
			yAcc = PlatformingConstants.getGravity();
		}
	}

	public void accelerate() {
		if (targetPlayer.isAccelerating()) {
			// initialize direction
			double direction = 0;
			if (targetPlayer.getFacingSide() == Side.RIGHT) {
				direction = 1;
			} else if (targetPlayer.getFacingSide() == Side.LEFT) {
				direction = -1;
			}

			// set xAcc based on player condition
			if (targetPlayer.isFalling()) {
				// set falling xAcc
				xAcc = PlatformingConstants.getAirAcc() * direction;

				// ensure player does not exceed max air speed
				if (Math.abs(xAcc) + Math.abs(targetPlayer.getxSpeed()) > PlatformingConstants.getMaxAirSpeed()) {
					xAcc = (Math.abs(PlatformingConstants.getMaxAirSpeed()) - Math.abs(targetPlayer.getxSpeed()))
							* direction;
				}
			} else if (Math.abs(targetPlayer.getxSpeed()) <= PlatformingConstants.getMaxWalkSpeed()
					&& !targetPlayer.isCrouching()) {
				// set walking xAcc
				xAcc = PlatformingConstants.getWalkAcc() * direction;

				// ensure player does not exceed max walk speed
				if (Math.abs(xAcc) + Math.abs(targetPlayer.getxSpeed()) > PlatformingConstants.getMaxWalkSpeed()) {
					xAcc = (Math.abs(PlatformingConstants.getMaxWalkSpeed()) - Math.abs(targetPlayer.getxSpeed()))
							* direction;
				}
			} else if (Math.abs(targetPlayer.getxSpeed()) > PlatformingConstants.getMaxWalkSpeed()) {
				// player is running
				if (targetPlayer.getCanRun()) {
					// set xAcc
					xAcc = PlatformingConstants.getRunAcc() * direction;

					// ensure player does not exceed max run speed
					if (Math.abs(targetPlayer.getxSpeed()) + Math.abs(xAcc) > PlatformingConstants.getMaxRunSpeed()) {
						xAcc = (Math.abs(PlatformingConstants.getMaxRunSpeed()) - Math.abs(targetPlayer.getxSpeed()))
								* direction;
					}
				} else {
					// player is not running, and needs to slow down
					xAcc = (targetPlayer.getxSpeed() > 0) ? -1 : 1;

					// ensure slowing down does not force xSpeed lower than maxWalkSpeed
					if (Math.abs(targetPlayer.getxSpeed()) - Math.abs(xAcc) < PlatformingConstants.getMaxWalkSpeed()) {
						xAcc = (targetPlayer.getxSpeed() > 0)
								? PlatformingConstants.getMaxWalkSpeed() - targetPlayer.getxSpeed()
								: targetPlayer.getxSpeed() - PlatformingConstants.getMaxWalkSpeed();
					}
				}
			}
		} else if (targetPlayer.isGrounded() && Math.abs(targetPlayer.getxSpeed()) > 0) {
			// player is not accelerating, and needs to slow down according to friction
			if (targetPlayer.getxSpeed() <= 1) {
				xAcc = targetPlayer.getxSpeed() * -1;
			} else {
				xAcc = targetPlayer.getxSpeed() * -1 * (1 - PlatformingConstants.getFriction());
			}
		}
	}

	// getters and setters
	// -----------------------------------------------------------------------------------------------
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
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
}
