package display;

import javax.swing.JPanel;

import levels.Metal;
import levels.Platform;
//event listening imports
import player.controllers.*;
import player.metalpushing.PusherPuller;

//drawing imports
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
//animation imports
import java.awt.Toolkit;

//imports to make a "level"
import levels.*;

import player.Player;

public class Board extends JPanel {

    private static final long serialVersionUID = -3991296469087660042L;

    private Player player = new Player();

    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    //for debugging: increase delay between frames to read stat menu
    private final int INTERVAL_DELAY = 1000 / 30;

    //TODO: find a better place to initialize
    //make level non-static
    private static Level level;
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private ArrayList<Metal> metals = new ArrayList<Metal>();
    private Painter painter = new Painter();


    public Board() {
        initPlatforms();
        initMetals();
        level = new Level(this.player, this.platforms, this.metals);

        initBoard();
    }

    //initialization
    //---------------------------------------------------------------------------------------------------
    private void initBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        KeyTracker keyTracker = new KeyTracker();
        MouseTracker mouseTracker = new MouseTracker();

        addKeyListener(keyTracker);
        addMouseListener(mouseTracker);
        addMouseMotionListener(mouseTracker);

        setFocusable(true);

        LevelThread thread = new LevelThread();
        thread.start();
    }

    private void initPlatforms() {
        // new Platform(xPos, yPos, width, height);
        // new Platform(0, 300, 500, 50);
        //this.platforms.add(new Platform(250, 100, 50, 500));
        //this.platforms.add(new Platform(450, 100, 50, 500));
        // new Platform(0, 100, 500, 50);
        this.platforms.add(new Platform(0, 500, 1500, 50));
    }

    private void initMetals() {
        //new Metal(xPos, yPos);
        this.metals.add(new Metal(350, 525));
    }

    //animation timing
    //---------------------------------------------------------------------------------------------------
    private class LevelThread extends Thread {
        @Override
        public void run() {
    
            long beforeTime, timeDiff, sleep;
    
            beforeTime = System.currentTimeMillis();
    
            while (true) {
                player.tick();
                repaint();
    
                timeDiff = System.currentTimeMillis() - beforeTime;
                sleep = INTERVAL_DELAY - timeDiff;
    
                if (sleep < 0) {
                    sleep = 2;
                }
    
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {};
    
                beforeTime = System.currentTimeMillis();
            }
        }
    }

    //drawing
    //---------------------------------------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        //paint background
        super.paintComponent(g);

        painter.paintLevel(g, level);

        Toolkit.getDefaultToolkit().sync();

        //for debug purposes
        //-----------------------------------------------------------------------------------------------
        g.setColor(Color.black);
        g.drawString("Keys Pressed: " + KeyTracker.getKeysPressed(), 10, 10);
        g.drawString("Player State: " + player.getState(), 10, 30);

        g.drawString("xPos: " + player.getxPos(), 10, 50);
        g.drawString("yPos: " + player.getyPos(), 10, 70);
        g.drawString("xSpeed: " + player.getxSpeed(), 10, 90);
        g.drawString("ySpeed: " + player.getySpeed(), 10, 110);

        g.drawString("Accelerating: " + player.isAccelerating(), 200, 10);
        g.drawString("Sliding: " + player.isSliding(), 200, 30);
        g.drawString("Grounded: " + player.isGrounded(), 200, 50);
        g.drawString("Jumping: " + player.isJumping(), 200, 70);
        g.drawString("Falling: " + player.isFalling(), 200, 90);
        g.drawString("Crouching: " + player.isCrouching(), 200, 110);
        g.drawString("At Wall: " + player.isAtWall(), 200, 130);
        g.drawString("Wall pushing: " + player.isWallPushing(), 200, 150);

        g.drawString("Mouse Position: " + MouseTracker.getMousePoint(), 400, 10);
        g.drawString("Mouse Buttons Pressed: " + MouseTracker.getButtonsPressed(), 400, 30);

        try {
            g.drawString("Target Metal xPos: " + PusherPuller.getTargetMetal().getxPos(), 800, 10);
            g.drawString("Target Metal yPos: " + PusherPuller.getTargetMetal().getyPos(), 800, 30);
        } catch (NullPointerException e) {
            g.drawString("Target Metal xPos: null", 800, 10);
            g.drawString("Target Metal yPos: null", 800, 30);
        };
    }

    //getters and setters
    //---------------------------------------------------------------------------------------------------
    public static Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        Board.level = level;
    }

    public int getOne() {
        return 1;
    }
}