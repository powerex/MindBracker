import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by AZbest on 06.12.2015.
 */
public class HelpFormModal extends JDialog {

    private boolean resultDialog;

    public boolean isOK(){
        return resultDialog;
    }
    HelpFormModal(JFrame owner, String sdf, boolean b) throws IOException {
        super(owner);
        setResizable(false);
        setSize(468,305);
        setLocationRelativeTo(owner);
        setModalityType(ModalityType.TOOLKIT_MODAL);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        HelpPanel helpPanel = new HelpPanel();
        helpPanel.setName("Help");
        add(helpPanel);
        /*JButton button1 = new JButton("OK");
        setLayout(new FlowLayout());
        add(button1);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultDialog = true;
                HelpFormModal.this.setVisible(false);
            }
        });
        */
        setVisible(true);
    }

}
