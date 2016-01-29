import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by AZbest on 28.11.2015.
 */
public class Button implements Serializable {

    public int x;
    public int y;
    public int size;
    public int number;

    public void setParameters(int x, int y, int size, int number) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.number = number;
    }

}