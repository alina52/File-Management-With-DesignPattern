package FileManagement;

import File.DataManager;
import File.FileManager;
import File.FileTree;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FileManagement extends JFrame {
    private static Header header;
    private static Panel panel;
    private static Bottom bottom;
    private static MenuBars menu;
    private static int init;

    public FileManagement () {
        super("文件管理");
        loadItems();
        setDefaultSettings();

        FileTree.setCurrent(FileTree.getCurrent());
    }

    private void loadItems(){
        header = new Header();
        panel = new Panel();
        menu = new MenuBars();
        bottom = new Bottom();

        this.add(header);
        this.add(panel);
        this.add(bottom);

        this.setMenuBar(menu);
    }

    private void setDefaultSettings(){
        this.setBounds(150,150,810,600);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataManager.save2disk();
                super.windowClosing(e);
                System.exit(0);
            }
        });
        this.setLayout(null);

        this.setVisible(true);

        FileManager.modifyFolderVolume(0,0);
    }
}
