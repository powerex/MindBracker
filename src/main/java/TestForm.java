import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by AZbest on 27.11.2015.
 */
public class TestForm {

    static MainPanel mp;

    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame mainFrame = new JFrame("Game Crossing circles");
        mainFrame.addWindowListener(new MyWindowListener());
        mp = new MainPanel(mainFrame);
        mainFrame.add(mp);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(940, 650);
        mainFrame.setLocation(200, 200);
        mainFrame.setResizable(false);
        mainFrame.setMaximumSize(new Dimension(940, 650));
        mainFrame.setVisible(true);
    }

}
