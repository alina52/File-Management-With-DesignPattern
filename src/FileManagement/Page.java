package FileManagement;

import File.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Page extends JScrollPane {
    private static int initial = 0;

    private static DefaultListModel listModel;
    private static JList list;

    public Page() {
        super();

        createPage();

        createList(initial);
    }

    private void createPage() {
        if(listModel == null)
            listModel = new DefaultListModel();

        list = new JList() {
            @Override
            public int locationToIndex(Point location) {
                int index = super.locationToIndex(location);
                if (index != -1 && !getCellBounds(index, index).contains(location)) {
                    return -1;
                } else {
                    return index;
                }
            }
        };
        list.addMouseListener(new ListMouseAdapter());
        list.setVisibleRowCount(listModel.getSize() / 4);
        list.setFixedCellWidth(160);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setFont(new Font("Caslon", Font.PLAIN, 14));

        list.setModel(listModel);

        this.setViewportView(list);
    }

    public static void createList(int id) {
        removeList();

        FileTreeItem parent = FileTree.findFileTreeItem(id);
        ArrayList<Integer> children = (ArrayList<Integer>) FileTree.findChildren(parent);

        int number = 0;
        for (int child : children) {
            File f = FileLoader.readFile(child);
            insertList(f);

            if(f.getIsFile())
                number ++;
        }
        if(list != null)
            list.setVisibleRowCount(listModel.getSize() / 4);
        FileTree.setObjects(number);
    }

    private static void removeList(){
        if(list != null){
            ((DefaultListModel) list.getModel()).removeAllElements();
        }
    }

    private static void insertList(File f) {
        if(listModel == null)
            listModel = new DefaultListModel();

        String insert = f.getName();
        listModel.addElement(insert);

        insert = f.getTime();
        listModel.addElement(insert);

        insert = f.getIsFile() ? "file" : "folder";
        listModel.addElement(insert);

        insert = String.valueOf(f.getVolume());
        insert += " Bytes";
        listModel.addElement(insert);
    }

    private class ListMouseAdapter extends MouseAdapter {

    }


}
