package com.pisoft.mistborn_game.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.intents.*;

public class Board extends JPanel implements IntentDispatcher {

	private static final long serialVersionUID = -3991296469087660042L;

	private Player player = Game.getActiveLevel().getPlayerController().getPlayer();

	private final int WIDTH = 500;
	private final int HEIGHT = 500;

	private Painter painter = new Painter();
	
//	private boolean eventTest = false;

	public Board() {
		initBoard();
		initKeyBindings();
	}

	// initialization
	// ---------------------------------------------------------------------------------------------------
	private void initBoard() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		addIntentListener(Game.getActiveLevel().getPlayerController().getIntentParser());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
					dispatchIntent(new SteelPushIntent(e.getPoint()));
				}

				if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
					// ironPull here				
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != MouseEvent.BUTTON1_DOWN_MASK) {
					dispatchIntent(new StopSteelPushIntent());
				}

				if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != MouseEvent.BUTTON3_DOWN_MASK) {
					// stop ironPull here
				}
			}
		});
		
		/*this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					eventTest = true;
				}
			}
		});*/

		setFocusable(true);
	}

	private void initKeyBindings() {
		// TODO: figure out how to map shift explicitly
		// add bindings with shift mask
		getInputMap().put(KeyStroke.getKeyStroke("UP"), "Jump Intent");
		getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "Acc Left Intent");
		getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "Acc Right Intent");
		getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Crouch Intent");
		getInputMap().put(KeyStroke.getKeyStroke("Z"), "Prep Run Intent");
		// getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0), "Prep Run Intent");
		getInputMap().put(KeyStroke.getKeyStroke("released UP"), "Stop Jump Intent");
		getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "Stop Acc Left Intent");
		getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "Stop Acc Right Intent");
		getInputMap().put(KeyStroke.getKeyStroke("released DOWN"), "Stop Crouch Intent");
		getInputMap().put(KeyStroke.getKeyStroke("released Z"), "Stop Prep Run Intent");

		// start actions
		getActionMap().put("Jump Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new JumpIntent());
			}
		});

		getActionMap().put("Acc Left Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new AccelerateIntent(Side.LEFT));
			}
		});

		getActionMap().put("Acc Right Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new AccelerateIntent(Side.RIGHT));
				// eventTest = true;
			}
		});

		getActionMap().put("Crouch Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new CrouchIntent());
			}
		});

		getActionMap().put("Prep Run Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new PrepRunIntent());
			}
		});

		// stop actions
		getActionMap().put("Stop Jump Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new StopJumpIntent());
			}
		});

		getActionMap().put("Stop Acc Left Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new StopAccIntent(Side.LEFT));
			}
		});

		getActionMap().put("Stop Acc Right Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new StopAccIntent(Side.RIGHT));
			}
		});

		getActionMap().put("Stop Crouch Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new StopCrouchIntent());
			}
		});

		getActionMap().put("Stop Prep Run Intent", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispatchIntent(new StopPrepRunIntent());
			}
		});
	}

	// drawing
	// ---------------------------------------------------------------------------------------------------
	@Override
	protected void paintComponent(Graphics g) {
		// paint background
		super.paintComponent(g);

		painter.paintLevel(g, Game.getActiveLevel());

		Toolkit.getDefaultToolkit().sync();

		// for debug purposes
		// -----------------------------------------------------------------------------------------------
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
		dp.printMessage("Wants to Acc: " +  player.wantsToAccelerate());
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
			dp.printMessage("Target Metal: " + player.getTargetMetal().getxPos() + ", " + player.getTargetMetal().getyPos());
		} catch (NullPointerException e) {
			dp.printMessage("Target Metal: null");
		}
		
//		g.drawString("Queued Actions: ", 700, 10);
//		int vertOffset = 10;
//		for (PlayerAction action : Game.getActiveLevel().getPlayerController().getPlayerActionResolver().getQueuedActions()) {
//			g.drawString(action.getClass().getSimpleName(), 805, vertOffset);
//			vertOffset += 20;
//		}
//		Game.getActiveLevel().getPlayerController().getPlayerActionResolver().getQueuedActions().clear();
		
//		g.drawString("Event Test: " + eventTest, 900, 10);
//		eventTest = false;
		
		g.dispose();
	}
	
	// debug stuffs
	// ---------------------------------------------------------------------------------------------------
	/**
	 * A class to simplify the real time printing of debug messages to the screen.<p>
	 * Contains methods to easily format message placement on the screen.
	 * 
	 * @author gouldb
	 *
	 */
	private class DebugPrinter {
		
		private Graphics g;
		
		private int x, y, xOffset, yOffset;
		
		/**
		 * Simple constructor for the <code>DebugPrinter</code> class
		 * 
		 * 
		 * @param g The <code>Graphics</code>  object to be used to draw the messages
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
		 * Prints a message to the screen at the current message location,
		 * and increments the y coordinate of the message location to prevent 
		 * messages from being printed on top of each other.
		 * 
		 * @param message The message to print onto the screen
		 */
		private void printMessage(String message) {
			g.drawString(message, x + xOffset, y + yOffset);
			
			yOffset += 20;
		}
		
		/**
		 * Moves message location to the top of a new column, allowing for easy 
		 * grouping of debug messages by column.
		 * 
		 * @param offset The x coordinate distance between the old and new columns
		 * 
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

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public int getOne() {
		return 1;
	}
}