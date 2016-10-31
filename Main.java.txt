import javax.swing.*;
import javax.swing.plaf.MenuBarUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class Main {
    static MenuBar mb;
    static Menu m[];
    static MenuItem mi[];
    static JFrame jf;

    public static void main(String []args) throws IOException {
    /*    BufferedWriter fw=new BufferedWriter(new FileWriter("Data.txt"));
        String s="";


        for(int i=0;i<32;i++){
            for(int j=0;j<5;j++){
                char c='1';
                s+=c;
                fw.write(c);
            }
            for(int j=5;j<1024;j++){
                char c=0;
                s+=c;
                fw.write(c);
            }
        }
        fw.close();
        EditFile.all=s;*/

        EditFile.all="";
        FileReader fr=new FileReader("Data.txt");
        int ch=0;
        while((ch=fr.read())!=-1){
            EditFile.all+=(char)ch;
        }

        FileMenu fm=new FileMenu();

        jf=new JFrame("文件管理");
        jf.setBounds(150,150,810,600);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(null);

        createMenu();

        Header h=new Header();
        jf.add(h);
        MainPanel mp=new MainPanel();
        jf.add(mp);
        Bottom b=new Bottom();
        jf.add(b);

        jf.setMenuBar(mb);
        jf.setVisible(true);

    }

    public static void createMenu(){
        mb=new MenuBar();
        m=new Menu[2];
        mi=new MenuItem[8];

        m[0]=new Menu(" NiYiwei ");m[1]=new Menu(" 1454094 ");


        for(int i=0;i<2;i++)
            mb.add(m[i]);
    }


}