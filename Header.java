import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
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
public class Header extends JPanel {
    static JButton back,forward;
    static TextField address,find;
    static JButton findButton;


    public Header(){
        new JPanel();
        this.setBounds(-1,0,811,40);
        this.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        this.setLayout(null);

        JLabel jl=new JLabel("Address:");
        jl.setFont(new Font("·½ÕýÆ·ÉÐºÚ", Font.PLAIN, 13));
        jl.setBounds(20,9,70,20);
        this.add(jl);

        address=new TextField();
        address.setBounds(90,10,530,20);
        this.add(address);
        address.setText(" root / ");

        find=new TextField();
        find.setBounds(635,10,160,20);
        this.add(find);

        findButton=new JButton(new ImageIcon(Main.class.getResource("find.png")));
        findButton.setBounds(775,10,20,20);
        findButton.addActionListener(new find());
        this.add(findButton);
        setComponentZOrder(findButton,0);
    }

    public static void showAddress(int id) throws IOException {
        File f=new File();
        f.readFile(id);
        String all=" ";
        for(int i=0;i<f.location.size();i++){
            all+=f.location.get(i)+" / ";
        }
        all+=f.name+" / ";
        address.setText(all);
    }

    private class find implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String name=find.getText();
            ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();

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

            int parent=-1;
            for(int i=0;i<FileMenu.fileTree.size();i++){
                boolean flag=true;
                File f=new File();
                try {
                    f.readFile(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(f.location.size()==(path.size()-1)){
                    for(int j=0;j<f.location.size();j++){
                          if(!f.location.get(j).equals(path.get(path.size()-j-1).toString())){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        parent=i;
                        break;
                    }
                }
            }
            for(int i=0;i<FileMenu.fileTree.size();i++){
                File f=new File();
                try {
                    f.readFile(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(FileMenu.fileTree.get(i).parent == parent && f.name.equals(name)){
                    String insert = "";
                    insert = f.name;
                    MainPanel.list.addElement(insert);
                    insert = "";

                    for (int j = 0; j < f.time.length() - 1; j++) {
                        insert += f.time.charAt(j);
                    }
                    MainPanel.list.addElement(insert);
                    insert = "";

                    if (f.isFile) {
                        insert = "file";
                    } else {
                        insert = "folder";
                    }
                    MainPanel.list.addElement(insert);

                    insert = String.valueOf(f.volume);
                    insert += " Bytes";
                    MainPanel.list.addElement(insert);

                    break;
                }
            }

            find.setText("");
        }
    }

}
