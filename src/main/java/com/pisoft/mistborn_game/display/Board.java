package com.pisoft.mistborn_game.display;

import javax.swing.JPanel;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.player.Player;

//event listening imports
import com.pisoft.mistborn_game.player.controllers.*;
import com.pisoft.mistborn_game.player.metalpushing.PushPullManager;

//drawing imports
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

//animation imports
import java.awt.Toolkit;

public class Board extends JPanel {

    private static final long serialVersionUID = -3991296469087660042L;

    private Player player = Game.getActiveLevel().getPlayer();

    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    //TODO: find a better place to initialize
    private Painter painter = new Painter();

    public Board() {
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
    }

    //drawing
    //---------------------------------------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        //paint background
        super.paintComponent(g);

        painter.paintLevel(g, Game.getActiveLevel());

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
        g.drawString("Can Run: " + player.getCanRun(), 200, 30);
        g.drawString("Sliding: " + player.isSliding(), 200, 50);
        
        g.drawString("Grounded: " + player.isGrounded(), 200, 70);
        g.drawString("Landing: " + player.isLanding(), 200, 90);
        g.drawString("Jumping: " + player.isJumping(), 200, 110);
        g.drawString("Double Jumping: " + player.isDoubleJumping(), 200, 130);
        g.drawString("Wall Jumping: " + player.isWallJumping(), 200, 150);
        g.drawString("Falling: " + player.isFalling(), 200, 170);
        
        g.drawString("Crouching: " + player.isCrouching(), 200, 190);
        g.drawString("At Wall: " + player.isAtWall(), 200, 210);
        g.drawString("Wall pushing: " + player.isWallPushing(), 200, 230);
        
        g.drawString("Mouse Position: " + MouseTracker.getMousePoint(), 400, 10);
        g.drawString("Mouse Buttons Pressed: " + MouseTracker.getButtonsPressed(), 400, 30);

        try {
            g.drawString("Target Metal xPos: " + PushPullManager.getTargetMetal().getxPos(), 800, 10);
            g.drawString("Target Metal yPos: " + PushPullManager.getTargetMetal().getyPos(), 800, 30);
        } catch (NullPointerException e) {
            g.drawString("Target Metal xPos: null", 800, 10);
            g.drawString("Target Metal yPos: null", 800, 30);
        };
    }

    //getters and setters
    //---------------------------------------------------------------------------------------------------
    public int getOne() {
        return 1;
    }
}