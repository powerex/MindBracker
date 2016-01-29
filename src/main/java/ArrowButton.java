import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by AZbest on 10.12.2015.
 */
public class ArrowButton extends JPanel {

    private int number;
    private Image img;

    ArrowButton(int number) {
        this.number = number;
        setSize(new Dimension(52, 107));
        BufferedImage bi = null;
        String s = "src/main/resources/";
        switch (number) {
            case 1: s = s + "arrowUR.png"; break;
            case 2: s = s + "arrowUL.png"; break;
            case 3: s = s + "arrowDR.png"; break;
            case 4: s = s + "arrowDL.png"; break;
        }
        try {
            bi = ImageIO.read(new File(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 300, 500, null);
    }

}
