package display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import player.Player;

public class KeyTracker extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        Player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Player.keyReleased(e);
    }
}