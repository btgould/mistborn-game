package com.pisoft.mistborn_game.display;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.controllers.KeyBinder;
import com.pisoft.mistborn_game.player.Side;
import com.pisoft.mistborn_game.player.intents.AccelerateIntent;
import com.pisoft.mistborn_game.player.intents.CrouchIntent;
import com.pisoft.mistborn_game.player.intents.JumpIntent;
import com.pisoft.mistborn_game.player.intents.PrepRunIntent;
import com.pisoft.mistborn_game.player.intents.StopAccIntent;
import com.pisoft.mistborn_game.player.intents.StopCrouchIntent;
import com.pisoft.mistborn_game.player.intents.StopJumpIntent;
import com.pisoft.mistborn_game.player.intents.StopPrepRunIntent;

public class Board extends JPanel {

	private static final long serialVersionUID = -3991296469087660042L;

	private final int WIDTH = 500;
	private final int HEIGHT = 500;

	private Painter painter = new Painter();

	private KeyBinder keyBinder;

	public Board() {
		initBoard();
		initKeyBindings();
	}

	// initialization
	// ---------------------------------------------------------------------------------------------------
	private void initBoard() {
		setKeyBinder(new KeyBinder(this));

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
	}

	private void initKeyBindings() {
		keyBinder.getKeyBindings().put(KeyEvent.VK_RIGHT, new AccelerateIntent(Side.RIGHT));
		keyBinder.getKeyBindings().put(KeyEvent.VK_LEFT, new AccelerateIntent(Side.LEFT));
		keyBinder.getKeyBindings().put(KeyEvent.VK_UP, new JumpIntent());
		keyBinder.getKeyBindings().put(KeyEvent.VK_DOWN, new CrouchIntent());
		keyBinder.getKeyBindings().put(KeyEvent.VK_SHIFT, new PrepRunIntent());

		keyBinder.getKeyBindings().put(KeyEvent.VK_RIGHT + KeyBinder.RELEASED, new StopAccIntent(Side.RIGHT));
		keyBinder.getKeyBindings().put(KeyEvent.VK_LEFT + KeyBinder.RELEASED, new StopAccIntent(Side.LEFT));
		keyBinder.getKeyBindings().put(KeyEvent.VK_UP + KeyBinder.RELEASED, new StopJumpIntent());
		keyBinder.getKeyBindings().put(KeyEvent.VK_DOWN + KeyBinder.RELEASED, new StopCrouchIntent());
		keyBinder.getKeyBindings().put(KeyEvent.VK_SHIFT + KeyBinder.RELEASED, new StopPrepRunIntent());
	}

	// drawing
	// ---------------------------------------------------------------------------------------------------
	@Override
	protected void paintComponent(Graphics g) {
		// paint background
		super.paintComponent(g);

		// paint active level
		if (Game.getActiveLevel() != null) {
			painter.paintLevel(g, Game.getActiveLevel());
		}

		// prevent excessive buffering of graphics events
		Toolkit.getDefaultToolkit().sync();
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	public int getOne() {
		return 1;
	}

	public KeyBinder getKeyBinder() {
		return keyBinder;
	}

	public void setKeyBinder(KeyBinder keyBinder) {
		this.keyBinder = keyBinder;
	}
}
