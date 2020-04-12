package display;

import javax.swing.JFrame;

import platforms.Platform;

import java.awt.EventQueue;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.GraphicsEnvironment;


public class Display extends JFrame {

    private static final long serialVersionUID = 3129809478408754800L;

    //private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //private Dimension screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
    private Component contentPane = this.getContentPane();
    
    private Dimension appSize = new Dimension();
    private Dimension screenSize = new Dimension();

    public static double xScale;
    public static double yScale;

    private Display() {
        initPlatforms();
        initUI();
    }

    private void initPlatforms() {
        // new Platform(xPos, yPos, width, height);
        // new Platform(0, 300, 500, 50);
        new Platform(250, 100, 50, 500);
        new Platform(450, 100, 50, 500);
        // new Platform(0, 100, 500, 50);
        new Platform(0, 500, 1500, 50);
    }

    private void initUI() {
        Board board = new Board();
        add(board);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        pack();

        //TODO: wait until screen is maximized
        this.appSize = this.getContentPane().getSize();
        this.screenSize.setSize(this.appSize);

        setTitle("Mistborn Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Display c = (Display) e.getSource();

                c.updateScale();
            }
        });

        
    }

    // things to detect when app size changes
    // -----------------------------------------------------------------------------------------------
    private void updateScale() {
        appSize = this.getContentPane().getSize();

        if (this.appSize.getHeight() > this.screenSize.getHeight() && this.appSize.getWidth() > this.screenSize.getWidth()) {
            this.screenSize = this.appSize;
        }

        //1366, 713
        this.xScale = this.appSize.getWidth() / screenSize.getWidth();
        this.yScale = this.appSize.getHeight() / screenSize.getHeight();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Display window = new Display();
            window.setVisible(true);//sets window to visible
        });
    }
}