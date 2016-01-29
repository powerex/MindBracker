import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by AZbest on 04.12.2015.
 */
public class MyWindowListener implements WindowListener {
    public void windowOpened(WindowEvent e) {
        /*ArrayList<Button> tempList = new ArrayList<Button>();
        try {
            FileInputStream fis = new FileInputStream("temp.out");
            ObjectInputStream oin = new ObjectInputStream(fis);
            for (int i = 0; i < 21; i++) {
                Button b = (Button) oin.readObject();
                tempList.add(b);
            }
            MainPanel.setLeft(tempList);
            tempList.clear();
            for (int i = 0; i < 21; i++) {
                Button b = (Button) oin.readObject();
                tempList.add(b);
            }
            MainPanel.setRight(tempList);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }*/
    }

    public void windowClosing(WindowEvent e) {
        System.out.print("Exiting");
        FileOutputStream fos = null;
        ArrayList<Button> tempList;
        try {
            fos = new FileOutputStream("temp.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            tempList = TestForm.mp.getLeft();
            for (int i=0; i<21; i++)
                oos.writeObject(tempList.get(i));
            tempList = TestForm.mp.getRight();
            for (int i=0; i<21; i++)
                oos.writeObject(tempList.get(i));
            oos.flush();
            oos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
}
