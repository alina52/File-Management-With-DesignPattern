package FileManagement;

import File.*;
import jdk.nashorn.internal.scripts.JD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class PopupMenu extends JPopupMenu {
    private JMenuItem menuItems[];
    private JMenu menu[];
    private int selectedFile;
    private JMenuItem[] newF = new JMenuItem[2];

    private static String name;

    public PopupMenu(int selectedFile) {
        super();

        this.selectedFile = selectedFile;

        createPopupMenu();

        hideItems();
    }

    private void createPopupMenu() {
        this.setFont(new Font("Dialog", Font.BOLD, 13));
        this.setPopupSize(140, 190);

        menuItems = new JMenuItem[5];
        menu = new JMenu[1];

        menuItems[0] = new JMenuItem("  Open");
        menuItems[0].setFont(new Font("Caslon", Font.PLAIN, 13));
        menuItems[1] = new JMenuItem("  Refresh");
        menuItems[1].setFont(new Font("Caslon", Font.PLAIN, 13));
        menuItems[2] = new JMenuItem("  Copy");
        menuItems[2].setFont(new Font("Caslon", Font.PLAIN, 13));
        menuItems[3] = new JMenuItem("  Paste");
        menuItems[3].setFont(new Font("Caslon", Font.PLAIN, 13));
        menuItems[4] = new JMenuItem("  Delete");
        menuItems[4].setFont(new Font("Caslon", Font.PLAIN, 13));


        menu[0] = new JMenu("  New");
        menu[0].setFont(new Font("Caslon", Font.PLAIN, 13));


        newF[0] = new JMenuItem("      File              ");
        newF[0].setPreferredSize(new Dimension(120, 25));
        newF[0].setFont(new Font("Caslon", Font.PLAIN, 13));
        newF[1] = new JMenuItem("      Folder              ");
        newF[1].setPreferredSize(new Dimension(120, 25));
        newF[1].setFont(new Font("Caslon", Font.PLAIN, 13));
        menu[0].add(newF[0]);
        menu[0].add(newF[1]);

        do {
            this.add(menuItems[0]);
            this.add(menuItems[1]);
            this.addSeparator();
            this.add(menuItems[2]);
            this.add(menuItems[3]);
            this.addSeparator();
            this.add(menu[0]);
            this.add(menuItems[4]);
        } while (false);

        addActionListener();
    }

    private void addActionListener() {
//        menuItems[0].addActionListener(new toOpen());
        menuItems[4].addActionListener(e -> delete());

        newF[0].addActionListener(e -> createFile());
        newF[1].addActionListener(e -> createFolder());

        menuItems[2].addActionListener(e -> copy());
        menuItems[3].addActionListener(e -> paste());
    }


    private void createFile() {
        new Dialog();
        FileManager.createFile(name);
        Page.createList(FileTree.getCurrent());
    }
}
