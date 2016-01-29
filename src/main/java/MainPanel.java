import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * Created by AZbest on 27.11.2015.
 */
public class MainPanel extends JPanel implements ActionListener, Runnable {

    private final JFrame owner;
    private Image img = new ImageIcon("src/main/resources/bgnd.jpg").getImage();
    private Image imageList[] = new Image[4];
    private Image imageArrowList[] = new Image[8];
    private final int delta = 15;
    private static ArrayList<Button> left = new ArrayList<Button>();
    private static ArrayList<Button> right = new ArrayList<Button>();
    Button b;
    private int X = 275, Y = 275, R = 250;
    private int rr = 2 * (int)(R * Math.sin( Math.PI / 21 )) - 4;
    private final java.util.List<Integer> listOperation = new LinkedList<Integer>();
    private boolean alive = false;
    private JPopupMenu popupMenu;
    private JButton helpButton;
    private int arrowWidth = 52;
    private int arrowHeight = 107;
    private int arrowLeftCorner = 410;
    private int arrowUpCorner = 205;
    private int activeArrow = 0;

//    private ArrowButton buttonUR, buttonUL, buttonDR, buttonDL;

    public void initButtons(boolean isOld) {
        if (!isOld) {
            int number = 0;
            for (int i = -2; i < 18; i++) {
                if (i == 8) number++;
                b = new Button();
                b.setParameters(
                        (int) (R * Math.cos(i * Math.PI * 2.0 / 21.0)) + X,
                        (int) (R * Math.sin(i * Math.PI * 2.0 / 21.0)) + Y,
                        rr,
                        number
                );
                left.add(b);
            }

            number++;
            for (int i = 8; i < 28; i++) {
                if (i == 18) number++;
                b = new Button();
                b.setParameters(
                        (int) (R * Math.cos(i * Math.PI * 2.0 / 21.0 + Math.PI / 21.0)) + X +
                                (int) (424 * Math.cos(5.0 * Math.PI / 21.0)),
                        (int) (R * Math.sin(i * Math.PI * 2.0 / 21.0 + Math.PI / 21.0)) + Y,
                        rr,
                        number
                );
                right.add(b);
            }
            left.add(right.get(5));
            right.add(left.get(5));
        } else {
            ArrayList<Button> tempList = new ArrayList<Button>();
            try {
                FileInputStream fis = new FileInputStream("temp.out");
                ObjectInputStream oin = new ObjectInputStream(fis);
                for (int i = 0; i < 21; i++) {
                    Button b = (Button) oin.readObject();
                    tempList.add(b);
                }
                MainPanel.setLeft(tempList);
                tempList  = new ArrayList<Button>();;
                for (int i = 0; i < 21; i++) {
                    Button b = (Button) oin.readObject();
                    tempList.add(b);
                }
                MainPanel.setRight(tempList);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    MainPanel(JFrame owner) throws InterruptedException, IOException {
        this.owner = owner;
        loadImages();

        File file = new File("temp.out");
        boolean isFileOldGameExist = (file.exists() && file.isFile());
        initButtons(isFileOldGameExist);

        addKeyListener(new MyKeyAdapter());
        addMouseWheelListener(new WheelListener());
        addMouseMotionListener(new MyMotionListener());

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File("src/main/resources/arrowUR_.png"));
            imageArrowList[0] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
            bi = ImageIO.read(new File("src/main/resources/arrowUR.png"));
            imageArrowList[1] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
            bi = ImageIO.read(new File("src/main/resources/arrowUL_.png"));
            imageArrowList[2] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
            bi = ImageIO.read(new File("src/main/resources/arrowUL.png"));
            imageArrowList[3] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
            bi = ImageIO.read(new File("src/main/resources/arrowDR_.png"));
            imageArrowList[6] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
            bi = ImageIO.read(new File("src/main/resources/arrowDR.png"));
            imageArrowList[7] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
            bi = ImageIO.read(new File("src/main/resources/arrowDL_.png"));
            imageArrowList[4] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
            bi = ImageIO.read(new File("src/main/resources/arrowDL.png"));
            imageArrowList[5] = bi.getScaledInstance(52, 107, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }


        helpButton = new JButton();

        helpButton.setIcon(new ImageIcon("src/main/resources/helpbm.png"));
        helpButton.setUI(new BasicButtonUI()
                       {
                           public void update(Graphics g, JComponent c)
                           {
                               if (c.isOpaque())
                               {
                                   Icon ic=((AbstractButton)c).getIcon();
                                   c.setSize(ic.getIconWidth(), ic.getIconHeight());
                                   c.setLocation(900, 588);
                                   g.setColor(Color.LIGHT_GRAY);
                                   g.fillRect(0, 0, c.getWidth(), c.getHeight());
                               }
                               paint(g, c);
                           }
                       }
        );

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HelpFormModal fm = new HelpFormModal(getOwner(), "Help", true);
                } catch (IOException evt) {
                    evt.printStackTrace();
                }
            }
        });

        helpButton.setVisible(true);
        add(helpButton);

        createPopupMenu();
        setFocusable(true);

        Thread t = new Thread(new Runnable() {
            public void run() {
                int operation;
                while (true) {
                    if (!listOperation.isEmpty() && !alive) {
                        alive = true;
                        operation = listOperation.get(0);
                        listOperation.remove(0);
                        switch (operation) {
                            case 1: rotateLeftCW(1);    break;
                            case 2: rotateLeftCCW(1);   break;
                            case 3: rotateRightCW(1);   break;
                            case 4: rotateRightCCW(1);  break;
                        }
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
        for (int i = 0 ; i < 21; i++) {
            g.drawImage(imageList[left.get(i).number], left.get(i).x, left.get(i).y, null);
            g.drawImage(imageList[right.get(i).number], right.get(i).x, right.get(i).y, null);
        }
        g.drawImage(imageArrowList[0], arrowLeftCorner, arrowUpCorner, null);
        g.drawImage(imageArrowList[2], arrowLeftCorner+arrowWidth, arrowUpCorner, null);
        g.drawImage(imageArrowList[6], arrowLeftCorner, arrowUpCorner+arrowHeight, null);
        g.drawImage(imageArrowList[4], arrowLeftCorner+arrowWidth, arrowUpCorner+arrowHeight, null);
        helpButton.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        //rotateLeftCW(1);
        //rotateRightCW(1);
    }

    public void rotateLeftCW(int step) {
        Button tmpButton;
        for (int i = 0; i < step; i++) {

            int j = 0;
            int XX[] = new int[21];
            int YY[] = new int[21];
            XX[20] = left.get(0).x;
            YY[20] = left.get(0).y;
            for (int k = 0; k < 20; k++) {
                XX[k] = left.get(k+1).x;
                YY[k] = left.get(k+1).y;
            }
//*
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int k = 0; k < 2*delta; k++)
                    {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int o = 0; o < 21; o++) {
                            left.get(o).x = (int) (R * Math.cos(((o - 3) * 2.0 * delta + k) * Math.PI / 21.0 / delta)) + X;
                            left.get(o).y = (int) (R * Math.sin(((o - 3) * 2.0 * delta + k) * Math.PI / 21.0 / delta)) + Y;
                        }
                        repaint();
                    }
                }
            });
            t.start();
            for (int k = 0; k < 21; k++) {
                left.get(k).x = XX[k];
                left.get(k).y = YY[k];
            }
            tmpButton = left.get(20);
            j = 20;
            for (; j > 0; j--) {
                left.set(j, left.get(j-1));
            }
            left.set(j, tmpButton);
            right.set(20, left.get(5));
            right.set(5, left.get(20));
        }
        alive = false;
    }

    public void rotateLeftCCW(int step) {
        Button tmpButton;
        for (int i = 0; i < step; i++) {

            int j = 20;
            int XX[] = new int[21];
            int YY[] = new int[21];
            XX[0] = left.get(20).x;
            YY[0] = left.get(20).y;
            for (int k = 20; k > 0; k--) {
                XX[k] = left.get(k - 1).x;
                YY[k] = left.get(k - 1).y;
            }
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int k = 0; k < 2*delta; k++)
                    {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int o = 0; o < 21; o++) {
                            left.get(o).x = (int) (R * Math.cos(((o - 1) * 2.0 * delta - k) * Math.PI / 21.0 / delta)) + X;
                            left.get(o).y = (int) (R * Math.sin(((o - 1) * 2.0 * delta - k) * Math.PI / 21.0 / delta)) + Y;
                        }
                        repaint();
                    }
                }
            });

            t.start();
            for (int k = 0; k < 21; k++) {
                left.get(k).x = XX[k];
                left.get(k).y = YY[k];
            }

            tmpButton = left.get(0);
            j = 0;
            for (; j < 20; j++) {
                left.set(j, left.get(j+1));
            }
            left.set(j, tmpButton);
            right.set(20, left.get(5));
            right.set(5, left.get(20));
        }
        alive = false;
    }

    public void rotateRightCW(int step) {
        Button tmpButton;
        for (int i = 0; i < step; i++) {

            int j = 0;
            int XX[] = new int[21];
            int YY[] = new int[21];
            XX[20] = right.get(0).x;
            YY[20] = right.get(0).y;
            for (int k = 0; k < 20; k++) {
                XX[k] = right.get(k+1).x;
                YY[k] = right.get(k+1).y;
            }
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int k = 0; k < 2*delta; k++)
                    {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int o = 0; o < 21; o++) {
                            right.get(o).x = (int) (R * Math.cos(((o + 7) * 2.0 * delta + k) * Math.PI / 21.0 / delta + Math.PI / 21.0)) + X +
                                    (int)(424 * Math.cos(5.0 * Math.PI / 21.0 ));
                            right.get(o).y = (int) (R * Math.sin(((o + 7) * 2.0 * delta + k) * Math.PI / 21.0 / delta + Math.PI / 21.0)) + Y;
                        }
                        repaint();
                    }
                }
            });
            t.start();
            for (int k = 0; k < 21; k++) {
                right.get(k).x = XX[k];
                right.get(k).y = YY[k];
            }

            tmpButton = right.get(20);
            j = 20;
            for (; j > 0; j--) {
                right.set(j, right.get(j-1));
            }
            right.set(j, tmpButton);
            left.set(20, right.get(5));
            left.set(5, right.get(20));
        }
        alive = false;
    }

    public void rotateRightCCW(int step) {
        Button tmpButton;
        for (int i = 0; i < step; i++) {

            int j = 20;
            int XX[] = new int[21];
            int YY[] = new int[21];
            XX[0] = right.get(20).x;
            YY[0] = right.get(20).y;
            for (int k = 20; k > 0; k--) {
                XX[k] = right.get(k-1).x;
                YY[k] = right.get(k-1).y;
            }
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int k = 0; k < 2*delta; k++)
                    {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int o = 0; o < 21; o++) {
                            right.get(o).x = (int) (R * Math.cos(((o + 9) * 2.0 * delta - k) * Math.PI / 21.0 / delta + Math.PI / 21.0)) + X +
                                    (int)(424 * Math.cos(5.0 * Math.PI / 21.0 ));
                            right.get(o).y = (int) (R * Math.sin(((o + 9) * 2.0 * delta - k) * Math.PI / 21.0 / delta + Math.PI / 21.0)) + Y;
                        }
                        repaint();
                    }
                }
            });
            t.start();
            for (int k = 0; k < 21; k++) {
                right.get(k).x = XX[k];
                right.get(k).y = YY[k];
            }

            tmpButton = right.get(0);
            j = 0;
            for (; j < 20; j++) {
                right.set(j, right.get(j+1));
            }
            right.set(j, tmpButton);
            left.set(20, right.get(5));
            left.set(5, right.get(20));
        }
        alive = false;
    }

    public static ArrayList<Button> getLeft() {
        return left;
    }

    public static ArrayList<Button> getRight() {
        return right;
    }

    public static void setLeft(ArrayList<Button> left) {
        MainPanel.left = left;
    }

    public static void setRight(ArrayList<Button> right) {
        MainPanel.right = right;
    }

    private void loadImages() throws IOException{
        BufferedImage bi = ImageIO.read(new File("src/main/resources/ball_blue_3.png"));
        imageList[0] = bi.getScaledInstance(rr, rr, Image.SCALE_SMOOTH);
        bi = ImageIO.read(new File("src/main/resources/ball_yellow_3.png"));
        imageList[1] = bi.getScaledInstance(rr, rr, Image.SCALE_SMOOTH);
        bi = ImageIO.read(new File("src/main/resources/ball_red_2.png"));
        imageList[2] = bi.getScaledInstance(rr, rr, Image.SCALE_SMOOTH);
        bi = ImageIO.read(new File("src/main/resources/ball_violet_2.png"));
        imageList[3] = bi.getScaledInstance(rr, rr, Image.SCALE_SMOOTH);
    }

    public void run() {
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_A: listOperation.add(2);   break;      //rotateLeftCCW(1); break;
                case KeyEvent.VK_D: listOperation.add(1);   break;      //rotateLeftCW(1); break;
                case KeyEvent.VK_J: listOperation.add(4);   break;      //rotateRightCCW(1); break;
                case KeyEvent.VK_L: listOperation.add(3);   break;      //rotateRightCW(1); break;
                default: break;
            }
        }
    }

    private int whereCursor(int x, int y) {
        if ( x > arrowLeftCorner && x < arrowLeftCorner + arrowWidth &&
             y > arrowUpCorner && y < arrowUpCorner + arrowHeight ) return 1;
        if ( x > arrowLeftCorner + arrowWidth && x < arrowLeftCorner + 2 * arrowWidth &&
             y > arrowUpCorner && y < arrowUpCorner + arrowHeight ) return 2;
        if ( x > arrowLeftCorner + arrowWidth && x < arrowLeftCorner + 2 * arrowWidth &&
             y > arrowUpCorner + arrowHeight && y < arrowUpCorner + 2 * arrowHeight ) return 3;
        if ( x > arrowLeftCorner && x < arrowLeftCorner + arrowWidth &&
             y > arrowUpCorner + arrowHeight && y < arrowUpCorner + 2 * arrowHeight ) return 4;
        return 0;
    }

    private class MyMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int wc = whereCursor( e.getX(), e.getY() );
            if ( activeArrow !=  wc)
                changeActiveArrow(wc);

        //System.out.println(whereCursor(e.getX(), e.getY()));
    }
}

    private void changeActiveArrow(int wc) {
        Image img;

        if (activeArrow != 0) {
            img = imageArrowList[(activeArrow - 1) * 2];
            imageArrowList[(activeArrow - 1) * 2] = imageArrowList[(activeArrow - 1) * 2 + 1];
            imageArrowList[(activeArrow - 1) * 2 + 1] = img;
        }

        if (wc != 0) {
            img = imageArrowList[(wc - 1) * 2];
            imageArrowList[(wc - 1) * 2] = imageArrowList[(wc - 1) * 2 + 1];
            imageArrowList[(wc - 1) * 2 + 1] = img;
        }

        activeArrow = wc;
        this.repaint();
    }

    private class WheelListener implements MouseWheelListener {
        public void mouseWheelMoved(MouseWheelEvent e) {

            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                if (e.getX() < 950 / 2) {
                    if (e.getWheelRotation() < 0) {
                        listOperation.add(2);
                    } else {
                        listOperation.add(1);
                    }
                } else {
                    if (e.getWheelRotation() < 0) {
                        listOperation.add(4);
                    } else {
                        listOperation.add(3);
                    }
                }
                /*
                System.out.println("Position(" + e.getX() + ";" + e.getY() + ") " +
                        "Count " + e.getUnitsToScroll() +
                        " Number " + e.getScrollAmount() +
                        " Spin " + e.getWheelRotation());
                */
            }
        }
    }

    public JFrame getOwner() {
        return owner;
    }

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();
        popupMenu.setBorder(new BevelBorder(BevelBorder.RAISED));

        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String s = event.getActionCommand();
                if (s.equals("Rotate Left CCW")) {
                    listOperation.add(2);
                } else if (s.equals("Rotate Left CW")) {
                    listOperation.add(1);
                } else if (s.equals("Rotate Right CCW")) {
                    listOperation.add(4);
                } else if (s.equals("Rotate Right CW")) {
                    listOperation.add(3);
                } else if (s.equals("Rotate New game CW")) {

                } else if (s.equals("Help")) {

                    try {
                        HelpFormModal fm = new HelpFormModal(getOwner(), "Help", true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*
                    if(fm.isOK()){
                        JOptionPane.showMessageDialog(getOwner(), "OK");
                    }
                    */

                } else if (s.equals("Exit")) {
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
                    System.exit(0);
                }
            }
        };

        JMenuItem rotLeftCCW = new JMenuItem("Rotate Left CCW");
        rotLeftCCW.addActionListener(menuListener);
        JMenuItem rotLeftCW = new JMenuItem("Rotate Left CW");
        rotLeftCW.addActionListener(menuListener);
        JMenuItem rotRightCCW = new JMenuItem("Rotate Right CCW");
        rotRightCCW.addActionListener(menuListener);
        JMenuItem rotRightCW = new JMenuItem("Rotate Right CW");
        rotRightCW.addActionListener(menuListener);
        JMenuItem newGame = new JMenuItem("New game");
        newGame.addActionListener(menuListener);
        newGame.setEnabled(false);
        JMenuItem hlp = new JMenuItem("Help");
        hlp.addActionListener(menuListener);
        JMenuItem evtExit = new JMenuItem("Exit");
        evtExit.addActionListener(menuListener);

        //rotLeftCCW.


        popupMenu.add(rotLeftCCW);
        popupMenu.add(rotLeftCW);
        popupMenu.add(rotRightCCW);
        popupMenu.add(rotRightCW);
        popupMenu.addSeparator();
        popupMenu.add(newGame);
        popupMenu.add(hlp);
        popupMenu.addSeparator();
        popupMenu.add(evtExit);

        this.setComponentPopupMenu(popupMenu);

    }

}