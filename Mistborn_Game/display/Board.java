package display;

import javax.swing.JPanel;

import platforms.Platform;

//drawing imports
import java.awt.Dimension;
import java.awt.Graphics;

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
    private final int INTERVAL_DELAY = 1000 / 30;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //TODO: for some reason, certain sets of three keys cant be registered at the same time
        //holding down any two keys ignores any key directly above either
        //works the same way in a word document, may just be how my computer works
        addKeyListener(new KeyTracker());
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
        for (int i = 0; i < Platform.platforms.size(); i++) {
            Platform.platforms.get(i).drawShape(g);
        }

        Toolkit.getDefaultToolkit().sync();

        //for debug purposes
        //-----------------------------------------------------------------------------------------------
        g.drawString("Keys Pressed: " + Player.setToString(Player.keysPressed), 10, 150);
        g.drawString("Player State: " + player.getState(), 10, 170);

        g.drawString("xPos: " + player.getXPos(), 10, 190);
        g.drawString("yPos: " + player.getYPos(), 10, 210);
        g.drawString("xSpeed: " + player.getXSpeed(), 10, 230);
        g.drawString("ySpeed: " + player.getYSpeed(), 10, 250);

    }
}