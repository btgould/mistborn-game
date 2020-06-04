package com.pisoft.mistborn_game.display;

//imports needed for swing
import javax.swing.JFrame;
import java.awt.EventQueue; //b/c swing not thread safe

//imports to keep track of screen size
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Display extends JFrame {

    private static final long serialVersionUID = 3129809478408754800L;
    
    private Dimension appSize = new Dimension();
    private Dimension screenSize = new Dimension();

    public double scale;

    public Display() {
        initUI();
    }

    //initialization
    //---------------------------------------------------------------------------------------------------
    private void initUI() {
        Board board = new Board();
        add(board);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setMinimumSize(new Dimension(800, 450));
        this.setVisible(true);

        setTitle("Mistborn Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.appSize = this.getContentPane().getSize();
        this.screenSize.setSize(this.appSize);

        this.addComponentListener(new ComponentAdapter() {
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

        // TODO: this feels sloppy
        if (this.appSize.getHeight() > this.screenSize.getHeight()
                && this.appSize.getWidth() > this.screenSize.getWidth()) {
            this.screenSize = this.appSize;
        }

        // 1366, 713
        this.scale = this.appSize.getHeight() / screenSize.getHeight();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Display window = new Display();
        });
    }
}