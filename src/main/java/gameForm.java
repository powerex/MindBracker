import javax.swing.*;
import java.awt.*;

/**
 * Created by AZbest on 27.11.2015.
 */
public class gameForm {
    private JPanel gamePanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("gameForm");
        frame.setContentPane(new gameForm().gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("Game");

        Image img = new ImageIcon("resources/bgnd.jpg").getImage();
        frame.setIconImage(img);
        frame.repaint();

        frame.setLocation(200, 200);
        frame.setSize(800, 500);
        frame.setVisible(true);
    }
}
