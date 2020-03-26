package display;

import javax.swing.JFrame;

import platforms.Platform;

import java.awt.EventQueue;

public class Display extends JFrame {

    private static final long serialVersionUID = 3129809478408754800L;

    private Display() {
        initPlatforms();
        initUI();
    }

    private void initPlatforms() {
        //new Platform(xPos, yPos, width, height);
        new Platform(0, 300, 500, 50);
        new Platform(250, 250, 50, 50);
    }

    private void initUI() {
        add(new Board());

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        pack();

        setTitle("Mistborn Game");
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Display window = new Display();
            window.setVisible(true);//sets window to visible
        });
    }
}