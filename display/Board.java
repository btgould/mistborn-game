package display;

import javax.swing.JPanel;

import metals.Metal;
import platforms.Platform;

//event listening imports
import player.KeyTracker;
import player.MouseTracker;



//drawing imports
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

//animation imports
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;

import player.Player;

public class Board extends JPanel {

    private static final long serialVersionUID = -3991296469087660042L;

    private Player player = new Player();

    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    private final int INITIAL_DELAY = 100;
    //for debugging: increase delay between frames to read stat menu
    private final int INTERVAL_DELAY = 1000 / 30;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        KeyTracker keyTracker = new KeyTracker();
        MouseTracker mouseTracker = new MouseTracker();

        //TODO: for some reason, certain sets of three keys cant be registered at the same time
        //holding down any two keys ignores any key directly above either
        //works the same way in a word document, may just be how my computer works
        addKeyListener(keyTracker);
        addMouseListener(mouseTracker);
        addMouseMotionListener(mouseTracker);

        setFocusable(true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new tickAnimation(), INITIAL_DELAY, INTERVAL_DELAY);
    }

    private class tickAnimation extends TimerTask {
        @Override
        public void run() {
            player.tick();

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //paint background
        super.paintComponent(g);

        //paint character
        player.drawShape(g);

        //paint platforms
        for (int i = 0; i < Platform.getPlatforms().size(); i++) {
            Platform.getPlatforms().get(i).drawShape(g);
        }

        //paint metals
        for (int i = 0; i < Metal.getMetals().size(); i++) {
            Metal.getMetals().get(i).drawShape(g);
        }

        Toolkit.getDefaultToolkit().sync();

        //for debug purposes
        //-----------------------------------------------------------------------------------------------
        g.setColor(Color.black);
        g.drawString("Keys Pressed: " + KeyTracker.getKeysPressed(), 10, 10);
        g.drawString("Player State: " + player.getState(), 10, 30);

        g.drawString("xPos: " + player.getXPos(), 10, 50);
        g.drawString("yPos: " + player.getYPos(), 10, 70);
        g.drawString("xSpeed: " + player.getXSpeed(), 10, 90);
        g.drawString("ySpeed: " + player.getYSpeed(), 10, 110);

        g.drawString("Collided: " + player.collided(), 10, 130);

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
    }
}