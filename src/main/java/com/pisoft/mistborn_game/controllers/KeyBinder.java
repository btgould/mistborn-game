package com.pisoft.mistborn_game.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Player;
import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.game_events.GameEvent;
import com.pisoft.mistborn_game.player.game_events.GameEventDispatcher;
import com.pisoft.mistborn_game.player.game_events.GameEventListener;
import com.pisoft.mistborn_game.player.intents.SteelPushIntent;
import com.pisoft.mistborn_game.player.intents.StopSteelPushIntent;

/**
 * This class stores key bindings in a <code>java.util.HashMap</code>. The keys
 * to this map are <code>Integer</code>s representing the key code of the
 * desired key. By default, actions are bound to key press events. To indicate
 * that an action should be performed on a key release, add the
 * <code>RELEASED</code> constant to the key code when you add the binding.
 * <p>
 * The values of the map are <code>PlayerAction</code> objects. The binding can
 * dispatch these actions, or <code>resolve()</code> them directly when the
 * corrosponding key event is received.
 * 
 * @author gouldb
 *
 */
// TODO: keys that do not autorepeat do not inturrupt the stream
public class KeyBinder implements GameEventDispatcher {

	/**
	 * Add this constant to the key code in a keyBinding entry to indicate that the
	 * action should be performed when the specified key is released.
	 * <p>
	 * VK stores 525 key codes, therefore this constant must be at minimum 526 to
	 * ensure no overlap between press and release key bindings.
	 */
	public static final int RELEASED = 526;
	private int lastKeyPressed = -1;

	private HashMap<Integer, PlayerAction> keyBindings = new HashMap<>();

	private JComponent targetComponent;
	private Player targetPlayer;
	private ArrayList<GameEventListener> listeners = new ArrayList<>();

	public KeyBinder(JComponent targetComponent) {
		setTargetComponent(targetComponent);

		addGameEventListener(Game.getPlayerActionManager());

		targetComponent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				if (keyCode != lastKeyPressed && keyCode != KeyEvent.VK_UNDEFINED) {
					try {
						dispatchEvent((GameEvent) getKeyBindings().get(keyCode).clone());
					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					
					if (!(keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_CONTROL)) {
						lastKeyPressed = keyCode;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				
				try {
					dispatchEvent((GameEvent) getKeyBindings().get(keyCode + KeyBinder.RELEASED).clone());
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				
				if (!(keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_CONTROL)) {
					lastKeyPressed = -1;
				}
			}
		});

		targetComponent.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
					dispatchEvent(new SteelPushIntent(e.getPoint()));
				}

				if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
					// ironPull here
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != MouseEvent.BUTTON1_DOWN_MASK) {
					dispatchEvent(new StopSteelPushIntent());
				}

				if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != MouseEvent.BUTTON3_DOWN_MASK) {
					// stop ironPull here
				}
			}
		});
	}

	// getters and setters
	// ---------------------------------------------------------------------------------------------------
	/**
	 * Gets the map of key bindings associated with this <code>KeyBinder</code>.
	 * 
	 * @return The map of key bindings.
	 */
	public HashMap<Integer, PlayerAction> getKeyBindings() {
		return keyBindings;
	}

	public void setKeyBindings(HashMap<Integer, PlayerAction> keyBindings) {
		this.keyBindings = keyBindings;
	}

	public JComponent getTargetComponent() {
		return targetComponent;
	}

	public void setTargetComponent(JComponent targetComponent) {
		this.targetComponent = targetComponent;
	}

	@Override
	public ArrayList<GameEventListener> getListeners() {
		return listeners;
	}

	@Override
	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}
}
