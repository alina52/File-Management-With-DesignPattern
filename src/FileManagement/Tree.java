package FileManagement;

import File.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tree extends JScrollPane {
    private static DefaultMutableTreeNode top;
    private static JTree jtree;
    private static TreeNode current;
    private static final int initial = 0;

    public Tree() {
        super();

        this.setBounds(5, -2, 144, 492);
        this.setBorder(BorderFactory.createEmptyBorder());

        try {
            top = createTree(initial);
            current = (TreeNode) top;

            jtree = new JTree(top);
            this.setViewportView(jtree);

            jtree.addTreeSelectionListener(new treeSelectionListener());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DefaultMutableTreeNode createTree(int id) throws IOException {
        FileTreeItem fti = FileTree.findFileTreeItem(id);

        File file = FileLoader.readFile(fti.getSelf());

        DefaultMutableTreeNode ret = null;
        if (!file.getIsFile()) {
            ret = new DefaultMutableTreeNode(file.getName());

            if (fti.getChild() != -1) {
                List<Integer> children = FileTree.findChildren(fti);

                for (int child : children) {
                    FileTreeItem temp = FileTree.findFileTreeItem(child);

                    DefaultMutableTreeNode childNode = createTree(temp.getSelf());
                    if (childNode != null) {
                        ret.add(childNode);
                    }
                }
            }

            return ret;
        } else {
            return null;
        }
    }

    public static List<TreeNode> getPath() {
        TreeNode temp = current;
        ArrayList<TreeNode> path = new ArrayList<TreeNode>();
        path.add(temp);
        if (temp == null) {
            System.out.println("Nullptr Error!");
        }
        while (temp.getParent() != null) {
            path.add(temp.getParent());
            temp = temp.getParent();
        }
        return path;
    }


    static class treeSelectionListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath tp = e.getNewLeadSelectionPath();
            current = (TreeNode) tp.getLastPathComponent();
            String name = current.toString();

            TreeNode temp = current;
            ArrayList<TreeNode> pathTemp = new ArrayList<>();
            ArrayList<String> path = new ArrayList<>();
            while (temp.getParent() != null) {
                pathTemp.add(temp.getParent());
                temp = temp.getParent();
            }

            for(int i = pathTemp.size()-1; i >= 0; i-- ) {
                path.add(pathTemp.get(i).toString());
            }

            int id = FileTree.findByNameNPath(name,path);
            Page.createList(id);
            FileTree.setCurrent(id);
        }
    }

}
