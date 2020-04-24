package display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import levels.*;
import player.Player;

//TODO: paint only objects onscreen
public class Painter {

    public void paintLevel(Graphics g, Level level) {
        //paint player
        paintPlayer(g, level.getPlayer());
        
        //paint platforms
        for (int i = 0; i < level.getPlatforms().size(); i++) {
            paintPlatform(g, level.getPlatforms().get(i));
        }

        //paint metals
        for (int i = 0; i < level.getMetals().size(); i++) {
            paintMetal(g, level.getMetals().get(i));
        }
    }

    private void paintPlayer(Graphics g, Player player) {

        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.red);

        AffineTransform at = AffineTransform.getTranslateInstance(player.getxPos(), player.getyPos());

        switch (player.getState()) {
            case IDLE:
                Rectangle2D idle = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(idle));
            break;

            case WALKING:
                Rectangle2D walking = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(walking));
            break;

            case RUNNING:
                Rectangle2D running = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(running));
            break;

            case SLIDING:
                Rectangle2D sliding = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(sliding));
            break;

            case JUMPING:
                Rectangle2D jumping = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(jumping));
            break;

            case DOUBLE_JUMPING:
                Rectangle2D doubleJumping = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(doubleJumping));
            break;

            case FALLING:
                Rectangle2D falling = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(falling));
            break;

            case WALL_FALLING:
                Rectangle2D wallFalling = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(wallFalling));
            break;

            case WALL_JUMPING:
                Rectangle2D wallJumping = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(wallJumping));
            break;

            case LANDING:
                Rectangle2D landing = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(landing));
            break;

            case CROUCHING:
                Rectangle2D crouch = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight() / 2);

                at.translate(0, player.getHeight() / 2);

                g2d.draw(at.createTransformedShape(crouch));
            break;

            case AT_WALL:
                Rectangle2D atWall = new Rectangle2D.Double(0D, 0D, player.getWidth(), player.getHeight());

                g2d.draw(at.createTransformedShape(atWall));
            break;
        }
    }

    private void paintPlatform(Graphics g, Platform platform) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.black);

        AffineTransform at = AffineTransform.getTranslateInstance(platform.getxPos(), platform.getyPos());

        Rectangle2D rect = new Rectangle2D.Double(0, 0, platform.getWidth(), platform.getHeight());

        g2d.draw(at.createTransformedShape(rect));
    }

    private void paintMetal(Graphics g, Metal metal) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.gray);

        AffineTransform at = AffineTransform.getTranslateInstance(metal.getxPos(), metal.getyPos());

        Rectangle2D rect = new Rectangle2D.Double(0, 0, 5, 5);

        g2d.draw(at.createTransformedShape(rect));
    }

}