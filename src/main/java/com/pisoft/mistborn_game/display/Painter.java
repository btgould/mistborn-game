package com.pisoft.mistborn_game.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.pisoft.mistborn_game.levels.*;
import com.pisoft.mistborn_game.player.Player;

public class Painter {

    public void paintLevel(Graphics g, Level level) {
        //paint player
        paintPlayer(g, level.getPlayer());
        
        //paint platforms
        for (int i = 0; i < level.getPlatforms().size(); i++) {
            paintPlatform(g, level.getPlatforms().get(i));
        }

        //paint metals
        for (int i = 0; i < level.getMetals().size(); i++) {
            paintMetal(g, level.getMetals().get(i));
        }
        
        printDebug(g, level.getPlayer());
        
        g.dispose();
    }

    private void paintPlayer(Graphics g, Player player) {

        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.red);

        AffineTransform at = AffineTransform.getTranslateInstance(player.getxPos(), player.getyPos());

        switch (player.getState()) {
            case IDLE:
                Rectangle2D idle = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(idle));
            break;

            case WALKING:
                Rectangle2D walking = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(walking));
            break;

            case RUNNING:
                Rectangle2D running = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(running));
            break;

            case SLIDING:
                Rectangle2D sliding = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(sliding));
            break;

            case JUMPING:
                Rectangle2D jumping = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(jumping));
            break;

            case DOUBLE_JUMPING:
                Rectangle2D doubleJumping = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(doubleJumping));
            break;

            case FALLING:
                Rectangle2D falling = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(falling));
            break;

            case WALL_FALLING:
                Rectangle2D wallFalling = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(wallFalling));
            break;

            case WALL_JUMPING:
                Rectangle2D wallJumping = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(wallJumping));
            break;

            case LANDING:
                Rectangle2D landing = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(landing));
            break;

            case CROUCHING:
                Rectangle2D crouch = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight() / 2);

                at.translate(0, player.getHeight() / 2);

                g2d.draw(at.createTransformedShape(crouch));
            break;

            case AT_WALL:
                Rectangle2D atWall = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(atWall));
            break;
        }
    }

    private void paintPlatform(Graphics g, Platform platform) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.black);

        AffineTransform at = AffineTransform.getTranslateInstance(platform.getxPos(), platform.getyPos());

        Rectangle2D rect = new Rectangle2D.Double(0, 0, platform.getWidth(), platform.getHeight());

        g2d.draw(at.createTransformedShape(rect));
    }

    private void paintMetal(Graphics g, Metal metal) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.gray);

        AffineTransform at = AffineTransform.getTranslateInstance(metal.getxPos(), metal.getyPos());

        Rectangle2D rect = new Rectangle2D.Double(0, 0, 5, 5);

        g2d.draw(at.createTransformedShape(rect));
    }
    
    // debug stuff
    // ---------------------------------------------------------------------------------------------------
    private void printDebug(Graphics g, Player player) {
    	g.setColor(Color.black);

		DebugPrinter dp = new DebugPrinter(g, 10, 10);

		dp.printMessage("Player State: " + player.getState());
		dp.printMessage("xPos: " + player.getxPos());
		dp.printMessage("yPos: " + player.getyPos());
		dp.printMessage("xSpeed: " + player.getxSpeed());
		dp.printMessage("ySpeed: " + player.getySpeed());
		dp.printMessage("xAcc: " + player.getxAcc());
		dp.printMessage("yAcc: " + player.getyAcc());
		dp.printMessage("xPushAmount: " + player.getxPushAmount());
		dp.printMessage("yPushAmount: " + player.getyPushAmount());

		dp.nextColumn(250);

		dp.printMessage("Facing Side: " + player.getFacingSide());
		dp.printMessage("Wants to Acc: " + player.wantsToAccelerate());
		dp.printMessage("Accelerating: " + player.isAccelerating());
		dp.printMessage("Walking: " + player.isWalking());
		dp.printMessage("Running: " + player.isRunning());
		dp.printMessage("Can Run: " + player.getCanRun());
		dp.printMessage("Sliding: " + player.isSliding());
		dp.printMessage("Crouching: " + player.isCrouching());

		dp.nextColumn(150);

		dp.printMessage("Grounded: " + player.isGrounded());
		dp.printMessage("Falling: " + player.isFalling());
		dp.printMessage("Landing: " + player.isLanding());
		dp.blankLine(5);
		dp.printMessage("Can Jump: " + player.canJump());
		dp.printMessage("Jumping: " + player.isJumping());
		dp.blankLine(5);
		dp.printMessage("Can Double Jump: " + player.canDoubleJump());
		dp.printMessage("Double Jumping: " + player.isDoubleJumping());
		dp.blankLine(5);
		dp.printMessage("Last Wall Jump Side: " + player.getLastWallJumpSide());
		dp.printMessage("Wall Jumping: " + player.isWallJumping());
		dp.blankLine(5);
		dp.printMessage("Jump Released: " + player.isJumpReleased());

		dp.nextColumn(150);

		dp.printMessage("At Wall: " + player.isAtWall());
		dp.printMessage("Wall Side: " + player.getWallSide());
		dp.printMessage("Wall pushing: " + player.isWallPushing());

		dp.nextColumn(150);

		dp.printMessage("Steel Pushing: " + player.isSteelPushing());
		try {
			dp.printMessage(
					"Target Metal: " + player.getTargetMetal().getxPos() + ", " + player.getTargetMetal().getyPos());
		} catch (NullPointerException e) {
			dp.printMessage("Target Metal: null");
		}
    }
    
    /**
	 * A class to simplify the real time printing of debug messages to the screen.
	 * <p>
	 * Contains methods to easily format message placement on the screen.
	 * 
	 * @author gouldb
	 */
	private class DebugPrinter {

		private Graphics g;

		private int x, y, xOffset, yOffset;

		/**
		 * Simple constructor for the <code>DebugPrinter</code> class
		 * 
		 * 
		 * @param g The <code>Graphics</code> object to be used to draw the messages
		 * @param x Starting x coordinate for message location
		 * @param y Starting y coordinate for message location
		 */
		private DebugPrinter(Graphics g, int x, int y) {
			this.g = g;
			this.x = x;
			this.y = y;

			xOffset = 0;
			yOffset = 0;
		}

		/**
		 * Prints a message to the screen at the current message location, and
		 * increments the y coordinate of the message location to prevent messages from
		 * being printed on top of each other.
		 * 
		 * @param message The message to print onto the screen
		 */
		private void printMessage(String message) {
			g.drawString(message, x + xOffset, y + yOffset);

			yOffset += 20;
		}

		/**
		 * Moves message location to the top of a new column, allowing for easy grouping
		 * of debug messages by column.
		 * 
		 * @param offset The x coordinate distance between the old and new columns
		 */
		private void nextColumn(int offset) {
			xOffset += offset;
			yOffset = 0;
		}

		/**
		 * Prints a blank line, allowing for easy subdivision of messages in the same
		 * column.
		 * 
		 * @param offset The y coordinate size of the blank line
		 */
		private void blankLine(int offset) {
			yOffset += offset;
		}
	}

}