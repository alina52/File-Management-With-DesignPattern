import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by samsung on 2016/6/12.
 */
public class Tree extends JScrollPane {
    static DefaultMutableTreeNode top;
    static JTree jt;
    static TreeNode cur;

    public Tree() throws IOException {
        new JScrollPane();
        setBounds(5, -2, 144, 492);
        setBorder(BorderFactory.createEmptyBorder());


        top=createTree(0);
        cur=(TreeNode)top;

        jt = new JTree(top);
        this.setViewportView(jt);

        jt.addTreeSelectionListener(new treeSelectionListener());
    //    jt.setCellRenderer(new DemoRenderer());
    }


    static DefaultMutableTreeNode createTree(int id) throws IOException {
        FileTreeItem temp = new FileTreeItem();
        for (int i = 0; i < FileMenu.fileTree.size(); i++) {
            if (FileMenu.fileTree.get(i).self == id) {
                temp = FileMenu.fileTree.get(i);
                break;
            }
        }

        File f=new File();
        f.readFile(temp.self);


        DefaultMutableTreeNode ret = null;
        if(!f.isFile) {
            ret = new DefaultMutableTreeNode(f.name);

            if (temp.child != -1) {
                FileTreeItem child = new FileTreeItem();
                int j;
                for (j = 0; j < FileMenu.fileTree.size(); j++) {
                    if (FileMenu.fileTree.get(j).self == temp.child) {
                        child = FileMenu.fileTree.get(j);
                        break;
                    }
                }
                DefaultMutableTreeNode childNode;
                if (j != FileMenu.fileTree.size()) {
                    childNode = createTree(child.self);
                    if(childNode != null)
                        ret.add(childNode);
                } else {
                    System.out.println("Error!");
                }


                if (child.sibling != -1) {
                    do {
                        int i;
                        for (i = 0; i < FileMenu.fileTree.size(); i++) {
                            if (FileMenu.fileTree.get(i).self == child.sibling) {
                                child = FileMenu.fileTree.get(i);
                                break;
                            }
                        }
                        if (i != FileMenu.fileTree.size()) {
                            childNode = createTree(child.self);
                            if(childNode != null)
                                ret.add(childNode);
                        } else {
                            System.out.println("Error!");
                        }

                    } while (child.sibling != -1);
                }
            }

            return ret;
        } else {
            return null;
        }
    }

    static class treeSelectionListener implements TreeSelectionListener{
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath tp=e.getNewLeadSelectionPath();
            cur=(TreeNode) tp.getLastPathComponent();
            String n=cur.toString();

            TreeNode temp=cur;
            ArrayList<TreeNode> path=new ArrayList<TreeNode>();
            while(temp.getParent()!=null){
                path.add(temp.getParent());
                temp=temp.getParent();
            }

            for(int i=0;i<FileMenu.fileTree.size();i++) {
                boolean flag=true;
                File f = new File();
                try {
                    f.readFile(i);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (f.name.equals(n)) {
                    if (f.location.size() == path.size()) {
                        if (path.size() == 0) {
                            ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
                            try {
                                MainPanel.createList(0);
                                Header.showAddress(0);
                                Bottom.showNumberOfChildren();

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        } else {
                            for (int j = 0; j < path.size(); j++) {
                                if (!path.get(j).toString().equals(f.location.get(path.size() - 1 - j))) {
                                    flag=false;
                                    break;
                                }
                            }
                            if(flag){
                                try {
                                    Header.showAddress(i);
                                    Bottom.showNumberOfChildren();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                                ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
                                try {
                                    MainPanel.createList(i);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }

                }
            }
        }
    }

}
