package FileManagement;

import javax.swing.*;
import java.awt.*;
import File.*;

public class Header extends JPanel {
    private static JButton back, forward;
    private static TextField address, find;
    private static JButton findButton;

    public Header() {
        super();

        setDefaultSettings();

        loadItems();
    }

    private void setDefaultSettings() {
        this.setBounds(-1, 0, 811, 40);
        this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        this.setLayout(null);
    }

    private void loadItems() {
        JLabel jl = new JLabel("Address:");
        jl.setFont(new Font("方正品尚黑", Font.PLAIN, 13));
        jl.setBounds(20, 9, 70, 20);
        this.add(jl);

        address = new TextField();
        address.setBounds(90, 10, 530, 20);
        address.setText(" root / ");
        this.add(address);

        find = new TextField();
        find.setBounds(635, 10, 160, 20);
        this.add(find);

        //findButton=new JButton(new ImageIcon(Main.class.getResource("find.png")));
        findButton = new JButton("find");
        findButton.setBounds(775, 10, 20, 20);
        //findButton.addActionListener(new find());
        setComponentZOrder(findButton, 0);
        this.add(findButton);
    }

    
}
