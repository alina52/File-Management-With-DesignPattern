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

    private void copy() {
        FileManager.setCopied(selectedFile);
    }

    private void paste() {
        FileManager.toPaste();
    }


    private void createFile() {
        new Dialog();
        FileManager.createFile(name);
        Page.createList(FileTree.getCurrent());
    }

    private void createFolder() {
        new Dialog();
        FileManager.createFolder(name);
        Page.createList(FileTree.getCurrent());
    }

    private class Dialog extends JDialog {
        private JTextField textField;

        Dialog() {
            super();
            textField = new JTextField();
            add(textField);
            addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent e) {
                    name = textField.getText();
                    if (name.equals("")) {
                        name = "Untitled";
                    }
                }
            });
            setTitle("Input a name");

            setModal(true);
            pack();
            setBounds(300, 300, 100, 60);
            setResizable(false);
            setVisible(true);
        }
    }

    private void delete() {
        File file = FileLoader.readFile(selectedFile);
        if (file.getIsFile()) {
            delete(selectedFile, true);
        } else {
            deleteFolder(selectedFile);
        }
        Page.createList(FileTree.getCurrent());
    }

    private void deleteFolder(int id) {
        FileTreeItem parent = FileTree.getFileTree().get(id);
        ArrayList<Integer> children = (ArrayList<Integer>) FileTree.findChildren(parent);

        for (int temp : children) {
            File file = FileLoader.readFile(temp);
            System.out.println("File "+file.getName() + " to be deleted");
            if (file.getIsFile())
                delete(temp, true);
            else
                deleteFolder(temp);
        }
        delete(id, false);
    }

    private void delete(int id, boolean isFile) {
        if (isFile) {
            File file = FileLoader.readFile(id);
            int block = file.getBlock();
            DataManager.deleteFile(block);

            FileManager.modifyFolderVolume(id,-file.getVolume());
        }
        FileManager.removeFile(id);
        FileTree.remove(id);
    }

    private void hideItems() {
        if(selectedFile == -1){
            menuItems[0].setEnabled(false);
            menu[0].setEnabled(true);
            menuItems[2].setEnabled(false);
            if(FileManager.getCopied() != -1)
                menuItems[3].setEnabled(true);
            else
                menuItems[3].setEnabled(false);
            menuItems[4].setEnabled(false);
        } else {
            menuItems[0].setEnabled(true);
            menuItems[2].setEnabled(true);
            menuItems[3].setEnabled(false);
            menu[0].setEnabled(false);
            menuItems[4].setEnabled(true);
        }


    }
}
