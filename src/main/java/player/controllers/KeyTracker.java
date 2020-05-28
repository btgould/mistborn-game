package player.controllers;

import java.util.HashSet;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyTracker extends KeyAdapter {

    //TODO: do I really want this to be static?
    //If not, how do I access it?
    private static HashSet<Integer> keysPressed = new HashSet<>();

    //input methods
    //---------------------------------------------------------------------------------------------------
    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
    }

    //note: must use Integer type b/c e.getKeyCode returns type int
    //remove() is overloaded, and thinks that an argument of type int indicates the index of element to remove
    //while an argument of type Integer makes it actually find an element of that value
    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(new Integer(e.getKeyCode()));
    }

    //getters and setters
    //---------------------------------------------------------------------------------------------------
    public static HashSet<Integer> getKeysPressed() {
        return keysPressed;
    }

    public static void setKeysPressed(HashSet<Integer> keysPressed) {
        KeyTracker.keysPressed = keysPressed;
    }
}