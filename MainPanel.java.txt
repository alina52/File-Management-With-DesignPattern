
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by samsung on 2016/6/5.
 */
public class MainPanel extends JPanel {
    static Tree tree;
    JScrollPane page;
    static DefaultListModel list = new DefaultListModel();

    JPopupMenu jp;
    JMenuItem main[];
    JMenu jm[];
    static JList jl;
    static String selected = "";
    static String name = "";
    static JTextField nameField;
    static int copied = -1;

    public MainPanel() throws IOException {
        new JPanel();
        this.setBounds(0, 40, 810, 490);
        this.setLayout(null);

        createPanel();
        createList(0);
        createJpopupMenu();

        page.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    jp.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        });
    }


    void createJpopupMenu() {
        jp = new JPopupMenu();
        jp.setFont(new Font("Dialog", Font.BOLD, 13));
        jp.setPopupSize(140, 190);

        main = new JMenuItem[5];
        jm = new JMenu[1];

        main[0] = new JMenuItem("  Open", new ImageIcon(Main.class.getResource("open.png")));
        main[0].setFont(new Font("Caslon", Font.PLAIN, 13));
        main[1] = new JMenuItem("  Refresh");
        main[1].setFont(new Font("Caslon", Font.PLAIN, 13));
        main[2] = new JMenuItem("  Copy", new ImageIcon(Main.class.getResource("copy.png")));
        main[2].setFont(new Font("Caslon", Font.PLAIN, 13));
        main[3] = new JMenuItem("  Paste", new ImageIcon(Main.class.getResource("paste.png")));
        main[3].setFont(new Font("Caslon", Font.PLAIN, 13));
        main[4] = new JMenuItem("  Delete", new ImageIcon(Main.class.getResource("delete.png")));
        main[4].setFont(new Font("Caslon", Font.PLAIN, 13));


        jm[0] = new JMenu("  New");
        jm[0].setFont(new Font("Caslon", Font.PLAIN, 13));

        JMenuItem[] newF = new JMenuItem[2];
        newF[0] = new JMenuItem("      File              ");
        newF[0].setPreferredSize(new Dimension(120,25));
        newF[0].setFont(new Font("Caslon", Font.PLAIN, 13));
        newF[1] = new JMenuItem("      Folder              ");
        newF[1].setPreferredSize(new Dimension(120,25));
        newF[1].setFont(new Font("Caslon", Font.PLAIN, 13));
        jm[0].add(newF[0]);
        jm[0].add(newF[1]);

        do {
            jp.add(main[0]);
            jp.add(main[1]);
            jp.addSeparator();
            jp.add(main[2]);
            jp.add(main[3]);
            jp.addSeparator();
            jp.add(jm[0]);
            jp.add(main[4]);
        } while (false);

        main[0].addActionListener(new toOpen());
        main[4].addActionListener(new toDelete());
        newF[0].addActionListener(new toNewFile());
        newF[1].addActionListener(new toNewFolder());
        main[2].addActionListener(new toCopy());
        main[3].addActionListener(new toPaste());
    }

    void createPanel() throws IOException {
        JButton text=new JButton("Name                                           Time               " +
                "                            Type                                             Volume" +
                "                                           ");
        text.setBackground(Color.white);
        text.setBorder(BorderFactory.createEmptyBorder());
        text.setForeground(new Color(0, 0, 0));
        text.setEnabled(false);
        text.setBounds(151,-2,653,24);
        this.add(text);

        page = new JScrollPane();
        page.setBounds(151, 24, 653, 462);
        page.setBorder(BorderFactory.createEmptyBorder());
        this.add(page);

        jl = new JList() {
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
        jl.setVisibleRowCount(list.getSize() / 4);
        jl.setFixedCellWidth(160);
        jl.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jl.setFont(new Font("Caslon", Font.PLAIN, 14));

        jl.setModel(list);
        jl.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int indexT = jl.locationToIndex(e.getPoint()) / 4;
                int indicesT[] = {indexT * 4, indexT * 4 + 1, indexT * 4 + 2, indexT * 4 + 3};
                jl.setSelectedIndices(indicesT);
                if (jl.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()) {
                    jl.clearSelection();
                }

                if (e.getClickCount() == 2) {
                    if (jl.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()) {
                        return;
                    }

                    String name = jl.getModel().getElementAt(indexT * 4).toString();

                    TreeNode temp = Tree.cur;
                    ArrayList<TreeNode> path = new ArrayList<TreeNode>();
                    path.add(temp);
                    if (temp == null) {
                        System.out.println("Nullptr Error!");
                    }
                    while (temp.getParent() != null) {
                        path.add(temp.getParent());
                        temp = temp.getParent();
                    }

                    for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                        File f = new File();
                        try {
                            f.readFile(i);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (f.name.equals(name)) {
                            if (f.location.size() == path.size()) {
                                boolean flag = true;
                                for (int j = 0; j < path.size(); j++) {
                                    if (!path.get(j).toString().equals(f.location.get(path.size() - 1 - j))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    if (f.isFile) {
                                        try {
                                            EditFile ef = new EditFile(f, i);
                                            return;
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            Header.showAddress(i);
                                            Bottom.showNumberOfChildren();

                                            ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
                                            createList(i);

                                            TreeModel t = Tree.jt.getModel();
                                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) t.getRoot();

                                            if (path.size() == 1) {
                                                for (int kt = 0; kt < node.getChildCount(); kt++) {
                                                    if (node.getChildAt(kt).toString().equals(name)) {
                                                        node = (DefaultMutableTreeNode) node.getChildAt(kt);
                                                        TreeNode[] treeNodes = node.getPath();
                                                        TreePath treePath = new TreePath(treeNodes);
                                                        Tree.jt.setSelectionPath(treePath);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                for (int kt = path.size() - 2; kt >= 0; kt--) {
                                                    for (int ktt = 0; ktt < node.getChildCount(); ktt++) {
                                                        if (path.get(kt).toString().equals(node.getChildAt(ktt).toString())) {
                                                            node = (DefaultMutableTreeNode) node.getChildAt(ktt);
                                                            break;
                                                        }
                                                    }
                                                }
                                                for (int kt = 0; kt < node.getChildCount(); kt++) {
                                                    if (node.getChildAt(kt).toString().equals(name)) {
                                                        node = (DefaultMutableTreeNode) node.getChildAt(kt);
                                                    }
                                                }
                                                TreeNode[] treeNodes = node.getPath();
                                                TreePath treePath = new TreePath(treeNodes);
                                                Tree.jt.setSelectionPath(treePath);
                                            }
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int indexT = jl.locationToIndex(e.getPoint()) / 4;
                int indicesT[] = {indexT * 4, indexT * 4 + 1, indexT * 4 + 2, indexT * 4 + 3};
                jl.setSelectedIndices(indicesT);
                if (jl.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()) {
                    jl.clearSelection();
                }

                if (e.isPopupTrigger()) {
                    jp.show(e.getComponent(), e.getX(), e.getY());

                    if (jl.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()) {
                        do {
                            main[0].setEnabled(false);
                            main[2].setEnabled(false);
                            main[4].setEnabled(false);

                            main[1].setEnabled(true);
                            if (copied == -1) {
                                main[3].setEnabled(false);
                            } else {
                                main[3].setEnabled(true);
                            }

                        } while (false);

                        jl.clearSelection();
                        return;
                    }
                    do {
                        main[0].setEnabled(true);
                        main[2].setEnabled(true);
                        main[4].setEnabled(true);
                        jm[0].setEnabled(true);

                        main[1].setEnabled(false);
                        main[3].setEnabled(false);
                    } while (false);

                    int index = jl.locationToIndex(e.getPoint()) / 4;
                    int indices[] = {index * 4, index * 4 + 1, index * 4 + 2, index * 4 + 3};
                    jl.setSelectedIndices(indices);

                    selected = jl.getModel().getElementAt(index * 4).toString();

                }

            }
        });
        page.setViewportView(jl);

        tree = new Tree();
        this.add(tree);
    }

    public static void createList(int id) throws IOException {
        FileTreeItem temp = new FileTreeItem();
        for (int i = 0; i < FileMenu.fileTree.size(); i++) {
            if (FileMenu.fileTree.get(i).self == id) {
                temp = FileMenu.fileTree.get(i);
                break;
            }
        }
        ArrayList<Integer> children = new ArrayList<>();
        if (temp.child != -1) {
            children.add(temp.child);
            for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                if (FileMenu.fileTree.get(i).self == temp.child) {
                    temp = FileMenu.fileTree.get(i);
                    break;
                }
            }
            if (temp.sibling != -1) {
                do {
                    children.add(temp.sibling);
                    for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                        if (FileMenu.fileTree.get(i).self == temp.sibling) {
                            temp = FileMenu.fileTree.get(i);
                            break;
                        }
                    }
                } while (temp.sibling != -1);
            }
        }

        for (int i = 0; i < children.size(); i++) {
            File f = new File(true, null, null);
            f.readFile(children.get(i));

            String insert = "";
            insert = f.name;
            list.addElement(insert);
            insert = "";

            for (int j = 0; j < f.time.length() - 1; j++) {
                insert += f.time.charAt(j);
            }
            list.addElement(insert);
            insert = "";

            if (f.isFile) {
                insert = "file";
            } else {
                insert = "folder";
            }
            list.addElement(insert);

            insert = String.valueOf(f.volume);
            insert += " Bytes";
            list.addElement(insert);
        }
    }

    private class toDelete implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = selected;

            TreeNode temp = Tree.cur;
            ArrayList<TreeNode> path = new ArrayList<TreeNode>();
            path.add(temp);
            if (temp == null) {
                System.out.println("Nullptr Error!");
            }
            while (temp.getParent() != null) {
                path.add(temp.getParent());
                temp = temp.getParent();
            }

            File f = new File();
            for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                boolean flag = true;
                try {
                    f.readFile(i);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (f.name.equals(name)) {
                    if (f.location.size() == path.size()) {
                        for (int j = 0; j < path.size(); j++) {
                            if (!path.get(j).toString().equals(f.location.get(path.size() - 1 - j))) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            if (f.isFile) {
                                deleteFile(f, i);
                            } else {
                                deleteFolder(f, i);
                            }
                        }
                    }
                }
            }
        }
    }

    private void deleteFile(File f, int id) {
        ArrayList<Integer> pointers = new ArrayList<>();

        pointers.add(f.block);
        while (true) {
            int indexT = pointers.get(pointers.size() - 1) * 1024;
            int block = 0;
            for (int i = indexT; i < indexT + 5; i++) {
                if (EditFile.all.charAt(i) == '1') {
                    block += Math.pow(2, (indexT + 5 - i - 1));
                }
            }
            if (block == 0) {
                break;
            }
            pointers.add(block);
        }
        String temp = "";
        for (int j = 0; j < 5; j++) {
            temp += '1';
        }
        for (int j = 5; j < 1024; j++) {
            char c = 0;
            temp += c;
        }
        for (int i = 0; i < pointers.size(); i++) {
            int index = pointers.get(i) * 1024;
            String s1 = EditFile.all.substring(0, index);
            String s2 = temp;
            String s3 = EditFile.all.substring(index + 1024, EditFile.all.length());

            EditFile.all = s1 + s2 + s3;
        }

        String allFiles = "";
        int Begin = 0, End = 0;
        try {
            int ch = 0;
            FileReader fr = new FileReader("Files.txt");
            while ((ch = fr.read()) != -1) {
                allFiles += (char) ch;
            }
            fr.close();
            for (int i = 0; i < id; i++) {
                while (allFiles.charAt(Begin) != '.')
                    Begin++;
                Begin++;
            }
            End = Begin;
            while (allFiles.charAt(End) != '.')
                End++;
            End++;
            String s1 = allFiles.substring(0, Begin);
            String s2 = allFiles.substring(End, allFiles.length());

            allFiles = s1 + s2;

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < FileMenu.fileTree.size(); i++) {
            FileTreeItem file = FileMenu.fileTree.get(i);
            if (file.child == id) {
                FileMenu.fileTree.get(i).child = FileMenu.fileTree.get(id).sibling;
            }
            if (file.sibling == id) {
                FileMenu.fileTree.get(i).sibling = FileMenu.fileTree.get(id).sibling;
            }
        }
        for (int i = 0; i < FileMenu.fileTree.size(); i++) {
            FileTreeItem file = FileMenu.fileTree.get(i);
            if (file.child > id)
                file.child--;
            if (file.self > id)
                file.self--;
            if (file.sibling > id)
                file.sibling--;
            if (file.parent > id)
                file.parent--;
        }

        int parent = -1;
        String fileTree = "";
        for (int i = 0; i < FileMenu.fileTree.size(); i++) {
            FileTreeItem ftt = FileMenu.fileTree.get(i);
            if (i == id) {
                parent = ftt.parent;
                continue;
            }
            fileTree += String.valueOf(ftt.self) + ',' + String.valueOf(ftt.child) + ',' + String.valueOf(ftt.sibling) + ',' + String.valueOf(ftt.parent) + '.';
        }

        try {
            FileWriter fw = new FileWriter("Data.txt");
            fw.write(EditFile.all);
            fw.close();

            FileWriter fw2 = new FileWriter("Files.txt");
            fw2.write(allFiles);
            fw2.close();

            FileWriter fw3 = new FileWriter("FileTree.txt");
            fw3.write(fileTree);
            fw3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileMenu fm = new FileMenu();
            Tree.top = Tree.createTree(0);
            Tree.jt = new JTree(Tree.top);
            MainPanel.tree.setViewportView(Tree.jt);
            Tree.jt.addTreeSelectionListener(new Tree.treeSelectionListener());

            ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
            MainPanel.createList(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFolder(File f, int id) {

        FileTreeItem ftt = FileMenu.fileTree.get(id);
        while (ftt.child != -1) {
            File child = new File();
            try {
                child.readFile(ftt.child);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (child.isFile) {
                deleteFile(child, ftt.child);
            } else {
                deleteFolder(child, ftt.child);
            }
            ftt = FileMenu.fileTree.get(id);
        }
        int parent = ftt.parent;
        if (FileMenu.fileTree.get(parent).child != ftt.self) {
            FileTreeItem child = FileMenu.fileTree.get(FileMenu.fileTree.get(parent).child);
            while (child.sibling != id) {
                child = FileMenu.fileTree.get(child.sibling);
            }
            FileMenu.fileTree.get(child.self).sibling = FileMenu.fileTree.get(id).sibling;
        } else {
            FileMenu.fileTree.get(parent).child = ftt.sibling;
        }


        String fileTree = "";
        for (int i = 0; i < FileMenu.fileTree.size(); i++) {
            if (FileMenu.fileTree.get(i).self == id) {
                continue;
            }

            FileTreeItem temp = FileMenu.fileTree.get(i);
            if (temp.self > id)
                FileMenu.fileTree.get(i).self--;
            if (temp.child > id)
                FileMenu.fileTree.get(i).child--;
            if (temp.sibling > id)
                FileMenu.fileTree.get(i).sibling--;
            if (temp.parent > id)
                FileMenu.fileTree.get(i).parent--;

            fileTree += String.valueOf(temp.self) + ',' + String.valueOf(temp.child) + ',' + String.valueOf(temp.sibling) + ',' + String.valueOf(temp.parent) + '.';
        }

        String allFiles = "";
        int Begin = 0, End = 0;
        try {
            int ch = 0;
            FileReader fr = new FileReader("Files.txt");
            while ((ch = fr.read()) != -1) {
                allFiles += (char) ch;
            }
            fr.close();
            for (int i = 0; i < id; i++) {
                while (allFiles.charAt(Begin) != '.')
                    Begin++;
                Begin++;
            }
            End = Begin;
            while (allFiles.charAt(End) != '.')
                End++;
            End++;
            String s1 = allFiles.substring(0, Begin);
            String s2 = allFiles.substring(End, allFiles.length());

            allFiles = s1 + s2;

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter("Files.txt");
            fw.write(allFiles);
            fw.close();

            FileWriter fw2 = new FileWriter("FileTree.txt");
            fw2.write(fileTree);
            fw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileMenu fm = new FileMenu();
            Tree.top = Tree.createTree(0);
            Tree.jt = new JTree(Tree.top);
            MainPanel.tree.setViewportView(Tree.jt);
            Tree.jt.addTreeSelectionListener(new Tree.treeSelectionListener());

            ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
            MainPanel.createList(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class toOpen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = selected;

            TreeNode temp = Tree.cur;
            ArrayList<TreeNode> path = new ArrayList<TreeNode>();
            path.add(temp);
            if (temp == null) {
                System.out.println("Nullptr Error!");
            }
            while (temp.getParent() != null) {
                path.add(temp.getParent());
                temp = temp.getParent();
            }

            for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                File f = new File();
                try {
                    f.readFile(i);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (f.name.equals(name)) {
                    if (f.location.size() == path.size()) {
                        boolean flag = true;
                        for (int j = 0; j < path.size(); j++) {
                            if (!path.get(j).toString().equals(f.location.get(path.size() - 1 - j))) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            if (f.isFile) {
                                try {
                                    EditFile ef = new EditFile(f, i);
                                    return;
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                try {
                                    Header.showAddress(i);

                                    ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
                                    createList(i);

                                    TreeModel t = Tree.jt.getModel();
                                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) t.getRoot();

                                    if (path.size() == 1) {
                                        for (int kt = 0; kt < node.getChildCount(); kt++) {
                                            if (node.getChildAt(kt).toString().equals(name)) {
                                                node = (DefaultMutableTreeNode) node.getChildAt(kt);
                                                TreeNode[] treeNodes = node.getPath();
                                                TreePath treePath = new TreePath(treeNodes);
                                                Tree.jt.setSelectionPath(treePath);
                                                break;
                                            }
                                        }
                                    } else {
                                        for (int kt = path.size() - 2; kt >= 0; kt--) {
                                            for (int ktt = 0; ktt < node.getChildCount(); ktt++) {
                                                if (path.get(kt).toString().equals(node.getChildAt(ktt).toString())) {
                                                    node = (DefaultMutableTreeNode) node.getChildAt(ktt);
                                                    break;
                                                }
                                            }
                                        }
                                        for (int kt = 0; kt < node.getChildCount(); kt++) {
                                            if (node.getChildAt(kt).toString().equals(name)) {
                                                node = (DefaultMutableTreeNode) node.getChildAt(kt);
                                            }
                                        }
                                        TreeNode[] treeNodes = node.getPath();
                                        TreePath treePath = new TreePath(treeNodes);
                                        Tree.jt.setSelectionPath(treePath);
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private class toNewFile implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            TreeNode temp = Tree.cur;
            ArrayList<TreeNode> path = new ArrayList<TreeNode>();
            path.add(temp);
            if (temp == null) {
                System.out.println("Nullptr Error!");
            }
            while (temp.getParent() != null) {
                path.add(temp.getParent());
                temp = temp.getParent();
            }

            File f = new File();

            int parent = 0;
            for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                try {
                    f.readFile(i);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (f.name.equals(Tree.cur.toString())) {
                    if ((f.location.size() + 1) == path.size()) {
                        boolean flag = true;
                        for (int j = 0; j < f.location.size(); j++) {
                            if (!f.location.get(j).equals(path.get(path.size() - 1 - j).toString())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            parent = i;
                        }
                    }
                }
            }

            Dialog d = new Dialog(parent);

            int index = 0;
            String all = "";
            int indexOfS = -1;
            try {
                int ch = 0;
                FileReader fr = new FileReader("FileTree.txt");
                while ((ch = fr.read()) != -1) {
                    all += (char) ch;
                }
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileTreeItem ftt = FileMenu.fileTree.get(parent);
            if (ftt.child != -1) {
                ftt = FileMenu.fileTree.get(ftt.child);
                while (ftt.sibling != -1) {
                    ftt = FileMenu.fileTree.get(ftt.sibling);
                }
                indexOfS = FileMenu.fileTree.indexOf(ftt);

                for (int i = 0; i < indexOfS; i++) {
                    while (all.charAt(index) != '.')
                        index++;
                    index++;
                }
                int index2 = index;

                while (all.charAt(index2) != '.') {
                    index2++;
                }
                index2++;

                String s1 = all.substring(0, index);
                String s3 = all.substring(index2, all.length());
                String s2 = String.valueOf(ftt.self) + ',' + String.valueOf(ftt.child) + ',' +
                        String.valueOf(FileMenu.fileTree.size()) + ',' + String.valueOf(ftt.parent) + '.';

                all = s1 + s2 + s3 + String.valueOf(FileMenu.fileTree.size()) + ",-1,-1," + String.valueOf(parent) + '.';
            } else {
                all = "";

                ftt.child = FileMenu.fileTree.size();

                for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                    FileTreeItem fttt = FileMenu.fileTree.get(i);
                    all += String.valueOf(fttt.self) + ',' + String.valueOf(fttt.child) + ',' +
                            String.valueOf(fttt.sibling) + ',' + String.valueOf(fttt.parent) + '.';
                }
                all += String.valueOf(FileMenu.fileTree.size()) + ",-1,-1," + String.valueOf(parent) + '.';
            }


            String Files = "";
            File pa = new File();
            try {
                FileReader fr = new FileReader("Files.txt");
                int ch = 0;
                while ((ch = fr.read()) != -1) {
                    Files += (char) ch;
                }
                fr.close();
                pa.readFile(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Files += name + ',';
            for (int i = 0; i < pa.location.size(); i++) {
                Files += pa.location.get(i) + '/';
            }
            Files += pa.name + "/,";
            Calendar c = Calendar.getInstance();

            int block = 0;
            int indexOfData = 0;
            for (int i = 1; i < 30; i++) {
                block = 0;
                for (indexOfData = i * 1024; indexOfData < i * 1024 + 5; indexOfData++) {
                    if (EditFile.all.charAt(indexOfData) == '1') {
                        block += Math.pow(2, (i * 1024 + 5) - indexOfData - 1);
                    }
                }
                if (block != 31) {
                    if (i == 30) {
                        System.out.print("Full!");
                    }
                    continue;
                }
                block = i;
                break;
            }

            String cblock = Integer.toBinaryString(block);
            String pre = "";
            for (int j = 0; j < 5 - cblock.length(); j++) {
                pre += "0";
            }
            cblock = pre + cblock;

            Files += String.valueOf(c.get(Calendar.YEAR)) + "/" + String.valueOf(c.get(Calendar.MONTH)) + "/"
                    + String.valueOf(+c.get(Calendar.DATE)) + "/" + String.valueOf(c.get(Calendar.HOUR)) + "/" +
                    String.valueOf(c.get(Calendar.MINUTE)) + "/,1,0," + cblock + '.';

            try {
                FileReader fr = new FileReader("Data.txt");
                String allData = "";
                int ch = 0;
                while ((ch = fr.read()) != -1) {
                    allData += (char) ch;
                }
                String data1 = allData.substring(0, block * 1024);
                String data3 = allData.substring(((block + 1) * 1024), allData.length());

                String data2 = "00000";
                char chara = 0;
                for (int k = 5; k < 1024; k++) {
                    data2 += chara;
                }
                EditFile.all = data1 + data2 + data3;
                FileWriter fw = new FileWriter("Data.txt");
                fw.write(EditFile.all);
                fw.close();
            } catch (IOException e) {
            }


            try {
                FileWriter fw = new FileWriter("Files.txt");
                fw.write(Files);
                fw.close();

                FileWriter fw2 = new FileWriter("FileTree.txt");
                fw2.write(all);
                fw2.close();

                FileMenu fm = new FileMenu();
                Tree.top = Tree.createTree(0);
                Tree.jt = new JTree(Tree.top);
                MainPanel.tree.setViewportView(Tree.jt);
                Tree.jt.addTreeSelectionListener(new Tree.treeSelectionListener());

                ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
                MainPanel.createList(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class toNewFolder implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            TreeNode temp = Tree.cur;
            ArrayList<TreeNode> path = new ArrayList<TreeNode>();
            path.add(temp);
            if (temp == null) {
                System.out.println("Nullptr Error!");
            }
            while (temp.getParent() != null) {
                path.add(temp.getParent());
                temp = temp.getParent();
            }

            File f = new File();
            int parent = 0;
            for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                boolean flag = true;
                try {
                    f.readFile(i);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (f.name.equals(Tree.cur.toString())) {
                    if ((f.location.size() + 1) == path.size()) {
                        for (int j = 0; j < f.location.size(); j++) {
                            if (!f.location.get(j).equals(path.get(path.size() - 1 - j).toString())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            parent = i;
                            flag = true;
                        }
                    }
                }
            }

            Dialog d = new Dialog(parent);

            int index = 0;
            String all = "";
            int indexOfS = -1;
            try {
                int ch = 0;
                FileReader fr = new FileReader("FileTree.txt");
                while ((ch = fr.read()) != -1) {
                    all += (char) ch;
                }
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileTreeItem ftt = FileMenu.fileTree.get(parent);
            if (ftt.child != -1) {
                ftt = FileMenu.fileTree.get(ftt.child);
                while (ftt.sibling != -1) {
                    ftt = FileMenu.fileTree.get(ftt.sibling);
                }
                indexOfS = FileMenu.fileTree.indexOf(ftt);

                for (int i = 0; i < indexOfS; i++) {
                    while (all.charAt(index) != '.')
                        index++;
                    index++;
                }
                int index2 = index;

                while (all.charAt(index2) != '.') {
                    index2++;
                }
                index2++;

                String s1 = all.substring(0, index);
                String s3 = all.substring(index2, all.length());
                String s2 = String.valueOf(ftt.self) + ',' + String.valueOf(ftt.child) + ',' +
                        String.valueOf(FileMenu.fileTree.size()) + ',' + String.valueOf(ftt.parent) + '.';

                all = s1 + s2 + s3 + String.valueOf(FileMenu.fileTree.size()) + ",-1,-1," + String.valueOf(parent) + '.';
            } else {
                all = "";

                ftt.child = FileMenu.fileTree.size();

                for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                    FileTreeItem fttt = FileMenu.fileTree.get(i);
                    all += String.valueOf(fttt.self) + ',' + String.valueOf(fttt.child) + ',' +
                            String.valueOf(fttt.sibling) + ',' + String.valueOf(fttt.parent) + '.';
                }
                all += String.valueOf(FileMenu.fileTree.size()) + ",-1,-1," + String.valueOf(parent) + '.';
            }

            ///writeFiles;
            String Files = "";
            File pa = new File();
            try {
                FileReader fr = new FileReader("Files.txt");
                int ch = 0;
                while ((ch = fr.read()) != -1) {
                    Files += (char) ch;
                }
                fr.close();
                pa.readFile(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Files += name + ',';
            for (int i = 0; i < pa.location.size(); i++) {
                Files += pa.location.get(i) + '/';
            }
            Files += pa.name + "/,";
            Calendar c = Calendar.getInstance();
            Files += String.valueOf(c.get(Calendar.YEAR)) + "/" + String.valueOf(c.get(Calendar.MONTH)) + "/"
                    + String.valueOf(+c.get(Calendar.DATE)) + "/" + String.valueOf(c.get(Calendar.HOUR)) + "/" +
                    String.valueOf(c.get(Calendar.MINUTE)) + "/,0,0.";


            try {
                FileWriter fw = new FileWriter("Files.txt");
                fw.write(Files);
                fw.close();

                FileWriter fw2 = new FileWriter("FileTree.txt");
                fw2.write(all);
                fw2.close();

                FileMenu fm = new FileMenu();
                Tree.top = Tree.createTree(0);
                Tree.jt = new JTree(Tree.top);
                MainPanel.tree.setViewportView(Tree.jt);
                Tree.jt.addTreeSelectionListener(new Tree.treeSelectionListener());

                ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
                MainPanel.createList(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class Dialog extends JDialog {
        public Dialog(int parent) {
            new JDialog();
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    name = nameField.getText();
                    if (name.equals("")) {
                        name = "Untitled";
                    }
                    FileTreeItem p = FileMenu.fileTree.get(parent);
                    if (p.child != -1) {
                        p = FileMenu.fileTree.get(p.child);
                        int append = 0;
                        while (p.sibling != -1) {
                            File f = new File();
                            try {
                                f.readFile(p.self);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (append == 0) {
                                if (f.name.equals(name))
                                    append++;
                            } else {
                                if (f.name.equals(name + String.valueOf(append)))
                                    append++;
                            }
                            p = FileMenu.fileTree.get(p.sibling);
                        }
                        File f = new File();
                        try {
                            f.readFile(p.self);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (append == 0) {
                            if (f.name.equals(name))
                                append++;
                        } else {
                            if (f.name.equals(name + String.valueOf(append)))
                                append++;
                        }

                        if (append != 0)
                            name += String.valueOf(append);
                    }
                }
            });
            setTitle("ÇëÊäÈëÃû³Æ");
            nameField = new JTextField();
            add(nameField);
            setModal(true);
            pack();
            setBounds(300, 300, 100, 60);
            setResizable(false);
            setVisible(true);
        }
    }

    private class toCopy implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            TreeNode temp = Tree.cur;
            ArrayList<TreeNode> path = new ArrayList<TreeNode>();
            path.add(temp);
            if (temp == null) {
                System.out.println("Nullptr Error!");
            }
            while (temp.getParent() != null) {
                path.add(temp.getParent());
                temp = temp.getParent();
            }

            File f = new File();
            for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                boolean flag = true;
                try {
                    f.readFile(i);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (f.name.equals(selected)) {
                    if (f.location.size() == path.size()) {
                        for (int j = 0; j < path.size(); j++) {
                            if (!path.get(j).toString().equals(f.location.get(path.size() - 1 - j))) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            if(f.isFile) {
                                copied = f.block;
                            } else {
                                System.out.println("Cannot copy a folder!");
                            }
                        }
                    }
                }
            }
        }
    }

    private class toPaste implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int id = copied;

            TreeNode temp = Tree.cur;
            ArrayList<TreeNode> path = new ArrayList<TreeNode>();
            path.add(temp);
            if (temp == null) {
                System.out.println("Nullptr Error!");
            }
            while (temp.getParent() != null) {
                path.add(temp.getParent());
                temp = temp.getParent();
            }

            File f = new File();

            int parent = 0;
            for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                try {
                    f.readFile(i);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (f.name.equals(Tree.cur.toString())) {
                    if ((f.location.size() + 1) == path.size()) {
                        boolean flag = true;
                        for (int j = 0; j < f.location.size(); j++) {
                            if (!f.location.get(j).equals(path.get(path.size() - 1 - j).toString())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            parent = i;
                        }
                    }
                }
            }

            File file = new File();
            try {
                file.readFile(id);
                name = file.name + "_backup";

                FileTreeItem p = FileMenu.fileTree.get(parent);
                if (p.child != -1) {
                    p = FileMenu.fileTree.get(p.child);
                    int append = 0;
                    while (p.sibling != -1) {
                        File ft = new File();
                        try {
                            ft.readFile(p.self);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (append == 0) {
                            if (ft.name.equals(name))
                                append++;
                        } else {
                            if (ft.name.equals(name + String.valueOf(append)))
                                append++;
                        }
                        p = FileMenu.fileTree.get(p.sibling);
                    }
                    File ft = new File();
                    try {
                        ft.readFile(p.self);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (append == 0) {
                        if (f.name.equals(name))
                            append++;
                    } else {
                        if (ft.name.equals(name + String.valueOf(append)))
                            append++;
                    }

                    if (append != 0)
                        name += String.valueOf(append);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            int index = 0;
            String all = "";
            int indexOfS = -1;
            try {
                int ch = 0;
                FileReader fr = new FileReader("FileTree.txt");
                while ((ch = fr.read()) != -1) {
                    all += (char) ch;
                }
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileTreeItem ftt = FileMenu.fileTree.get(parent);
            if (ftt.child != -1) {
                ftt = FileMenu.fileTree.get(ftt.child);
                while (ftt.sibling != -1) {
                    ftt = FileMenu.fileTree.get(ftt.sibling);
                }
                indexOfS = FileMenu.fileTree.indexOf(ftt);

                for (int i = 0; i < indexOfS; i++) {
                    while (all.charAt(index) != '.')
                        index++;
                    index++;
                }
                int index2 = index;

                while (all.charAt(index2) != '.') {
                    index2++;
                }
                index2++;

                String s1 = all.substring(0, index);
                String s3 = all.substring(index2, all.length());
                String s2 = String.valueOf(ftt.self) + ',' + String.valueOf(ftt.child) + ',' +
                        String.valueOf(FileMenu.fileTree.size()) + ',' + String.valueOf(ftt.parent) + '.';

                all = s1 + s2 + s3 + String.valueOf(FileMenu.fileTree.size()) + ",-1,-1," + String.valueOf(parent) + '.';
            } else {
                all = "";

                ftt.child = FileMenu.fileTree.size();

                for (int i = 0; i < FileMenu.fileTree.size(); i++) {
                    FileTreeItem fttt = FileMenu.fileTree.get(i);
                    all += String.valueOf(fttt.self) + ',' + String.valueOf(fttt.child) + ',' +
                            String.valueOf(fttt.sibling) + ',' + String.valueOf(fttt.parent) + '.';
                }
                all += String.valueOf(FileMenu.fileTree.size()) + ",-1,-1," + String.valueOf(parent) + '.';
            }


            String Files = "";
            File pa = new File();
            try {
                FileReader fr = new FileReader("Files.txt");
                int ch = 0;
                while ((ch = fr.read()) != -1) {
                    Files += (char) ch;
                }
                fr.close();
                pa.readFile(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Files += name + ',';
            for (int i = 0; i < pa.location.size(); i++) {
                Files += pa.location.get(i) + '/';
            }
            Files += pa.name + "/,";
            Calendar c = Calendar.getInstance();

            int block = 0;
            int indexOfData = 0;
            for (int i = 1; i < 30; i++) {
                block = 0;
                for (indexOfData = i * 1024; indexOfData < i * 1024 + 5; indexOfData++) {
                    if (EditFile.all.charAt(indexOfData) == '1') {
                        block += Math.pow(2, (i * 1024 + 5) - indexOfData - 1);
                    }
                }
                if (block != 31) {
                    if (i == 30) {
                        System.out.print("Full!");
                    }
                    continue;
                }
                block = i;
                break;
            }

            String cblock = Integer.toBinaryString(block);
            String pre = "";
            for (int j = 0; j < 5 - cblock.length(); j++) {
                pre += "0";
            }
            cblock = pre + cblock;

            Files += String.valueOf(c.get(Calendar.YEAR)) + "/" + String.valueOf(c.get(Calendar.MONTH)) + "/"
                    + String.valueOf(+c.get(Calendar.DATE)) + "/" + String.valueOf(c.get(Calendar.HOUR)) + "/" +
                    String.valueOf(c.get(Calendar.MINUTE)) + "/,1,0," + cblock + '.';

            try {
                saveData(copied,block);

                FileWriter fw = new FileWriter("Files.txt");
                fw.write(Files);
                fw.close();

                FileWriter fw2 = new FileWriter("FileTree.txt");
                fw2.write(all);
                fw2.close();

                FileMenu fm = new FileMenu();
                Tree.top = Tree.createTree(0);
                Tree.jt = new JTree(Tree.top);
                MainPanel.tree.setViewportView(Tree.jt);
                Tree.jt.addTreeSelectionListener(new Tree.treeSelectionListener());

                ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
                MainPanel.createList(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }

            copied=-1;
        }
    }

    void saveData(int from,int to) throws IOException {
        String text = "";
        ArrayList<Integer> froms=new ArrayList<>();
        froms.add(from);
        while(true){
            int indexT=froms.get(froms.size()-1)*1024;
            int block=0;
            for(int i=indexT;i<indexT+5;i++){
                if(EditFile.all.charAt(i)=='1'){
                    block+=Math.pow(2,(indexT+5-i-1));
                }
            }
            if(block == 0){
                break;
            }
            froms.add(block);
        }

        for(int i=0;i<froms.size();i++){
            int index=froms.get(i)*1024+5;
            int k=index;
            while(EditFile.all.charAt(k)!=0){
                text+=EditFile.all.charAt(k);
                k++;
                if(k-index==1019){
                    break;
                }
            }
        }

        int number = text.length() / (1024 - 5) + 1;
        ArrayList<String> texts = new ArrayList<>();
        ArrayList<Integer> blocks = new ArrayList<>();

        for (int i = to; i < 31; i++) {
            int block = 0;
            for (int index = i * 1024; index < i * 1024 + 5; index++) {
                if (EditFile.all.charAt(index) == '1') {
                    block += Math.pow(2, (i * 1024 + 5) - index - 1);
                }
            }
            if (block != 31) {
                if (i == 30) {
                    System.out.print("Full!");
                }
                continue;
            }
            blocks.add(i);
            if (blocks.size() == number) {
                break;
            }
        }

        for (int i = 0; i < number - 1; i++) {
            String temp = "";

            String c = Integer.toBinaryString(blocks.get(i + 1));
            String pre = "";
            for (int j = 0; j < 5 - c.length(); j++) {
                pre += "0";
            }
            c = pre + c;

            temp += c;
            temp += text.substring(i * 1019, (i + 1) * 1019);
            texts.add(temp);
        }
        String temp = "";
        temp += "00000";
        temp += text.substring((number - 1) * 1019, text.length());
        int length = temp.length();
        for (int i = 0; i < (1024 - length); i++) {
            char c = 0;
            temp += c;
        }
        texts.add(temp);

        FileWriter fw = null;
        try {
            fw = new FileWriter("Data.txt");
            for (int i = 0; i < number; i++) {
                String s1 = EditFile.all.substring(0, blocks.get(i) * 1024);
                String s3 = EditFile.all.substring((blocks.get(i) + 1) * 1024, EditFile.all.length());

                EditFile.all = s1 + texts.get(i) + s3;
            }
            fw.write(EditFile.all);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
