package display;

import javax.swing.JFrame;

import platforms.Platform;

import java.awt.EventQueue;

//imports for scaling drawings (future plans)
/*import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import java.awt.Toolkit;*/

public class Display extends JFrame {

    private static final long serialVersionUID = 3129809478408754800L;

    //private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //private Dimension appSize;

    //public double xScale = appSize.getWidth() / screenSize.getWidth();
    //public double yScale = appSize.getWidth() / screenSize.getWidth();

    private Display() {
        initPlatforms();
        initUI();
    }

    private void initPlatforms() {
        //new Platform(xPos, yPos, width, height);
        //new Platform(0, 300, 500, 50);
        new Platform(250, 200, 50, 500);
        //new Platform(0, 100, 500, 50);
        new Platform(0, 500, 1500, 50);
    }

    private void initUI() {
        Board board = new Board();
        add(board);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //appSize = this.getContentPane().getSize();

        pack();

        setTitle("Mistborn Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                //appSize = .getContentPane().getSize();

                xScale = appSize.getWidth() / screenSize.getWidth();
                yScale = appSize.getWidth() / screenSize.getWidth();

                board.repaint();
            }
        });*/
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Display window = new Display();
            window.setVisible(true);//sets window to visible
        });
    }
}