import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by AZbest on 06.12.2015.
 */
public class HelpPanel extends JPanel {

    private Image img; // background image

    HelpPanel() throws IOException {
        BufferedImage bi = ImageIO.read(new File("src/main/resources/help_.png"));
        img = bi.getScaledInstance(462, 276, Image.SCALE_SMOOTH);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
