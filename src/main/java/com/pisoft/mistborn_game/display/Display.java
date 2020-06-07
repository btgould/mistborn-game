package com.pisoft.mistborn_game.display;

//imports needed for swing
import javax.swing.JFrame;

//imports to keep track of screen size
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Display extends JFrame {

    private static final long serialVersionUID = 3129809478408754800L;
    
    private JFrame frame;
    private Board board;
    
    private Dimension appSize = new Dimension();
    private Dimension screenSize = new Dimension();

    public double scale;

    public Display() {
        initUI();
    }

    //initialization
    //---------------------------------------------------------------------------------------------------
    private void initUI() {
    	frame = new JFrame();
        board = new Board();
        frame.add(board);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 450));
        frame.setVisible(true);

        frame.setTitle("Mistborn Game");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.appSize = frame.getContentPane().getSize();
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
        appSize = frame.getContentPane().getSize();

        // TODO: this feels sloppy
        if (this.appSize.getHeight() > this.screenSize.getHeight()
                && this.appSize.getWidth() > this.screenSize.getWidth()) {
            this.screenSize = this.appSize;
        }

        // 1366, 713
        this.scale = this.appSize.getHeight() / screenSize.getHeight();
    }

    //getters and setters
    // -----------------------------------------------------------------------------------------------
    public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
