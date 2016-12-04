package FileManagement;

import File.FileTree;

import javax.swing.*;
import java.awt.*;

public class Bottom extends JPanel {
    private static JLabel object, volume;

    public Bottom() {
        super();

        setDefaultSettings();

        loadItems();
    }

    private void setDefaultSettings() {
        new JPanel();
        this.setBounds(0, 530, 810, 40);
        this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        this.setLayout(null);
    }

    private void loadItems() {
        object.setBounds(0, 0, 100, 20);
        this.add(object);

        if(volume == null)
            volume = new JLabel("test");
        volume.setBounds(640, 0, 200, 20);
        this.add(volume);
    }

    public static void updateObjects(int objects) {
        if(object == null)
            object = new JLabel("  "+String.valueOf(objects)+"  个对象");
        else
            object.setText("  "+String.valueOf(objects)+"  个对象");
    }

    public static void updateVolume(int usedVolume,int totalVolume) {
        double volumeValue = (double) (totalVolume - usedVolume) / 1024;
        java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
        volume.setText("共 30.00 MB  剩余 "+df.format(volumeValue)+" MB");
    }

}
