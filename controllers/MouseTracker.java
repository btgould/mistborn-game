package controllers;

import java.util.HashSet;

import java.awt.Point;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseTracker extends MouseAdapter {

    //TODO: do I really want these to be static? 
    //If not, how do I access them?
    private static Point mousePoint;
    private static HashSet<Integer> buttonsPressed = new HashSet<Integer>();

    @Override
    public void mouseMoved(MouseEvent e) {
        MouseTracker.mousePoint = new Point(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttonsPressed.add(e.getModifiers());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttonsPressed.remove(e.getModifiers());
    }

    //getters and setters
    //---------------------------------------------------------------------------------------------------
    public static Point getMousePoint() {
        return mousePoint;
    }

    public static void setMousePoint(Point mousePoint) {
        MouseTracker.mousePoint = mousePoint;
    }

    public static HashSet<Integer> getButtonsPressed() {
        return buttonsPressed;
    }

    public static void setButtonsPressed(HashSet<Integer> buttonsPressed) {
        MouseTracker.buttonsPressed = buttonsPressed;
    }
}