package platforms;

//graphics imports
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;

//import to scale drawings
//import display.Display;

public class Platform {

    public double xPos;
    public double yPos;

    public double width;
    public double height;

    public static ArrayList<Platform> platforms = new ArrayList<Platform>();

    public Platform(double xPos, double yPos, double width, double height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        platforms.add(this);
    }

    public void drawShape(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.black);

        AffineTransform at = AffineTransform.getTranslateInstance(this.xPos, this.yPos);

        Rectangle2D rect = new Rectangle2D.Double(0, 0, this.width, this.height);

        g2d.draw(at.createTransformedShape(rect));
    }
    
}