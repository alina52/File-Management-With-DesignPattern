import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by samsung on 2016/6/5.
 */
public class Bottom extends JPanel {
    static public JLabel jl1,jl2=new JLabel();
    public Bottom(){
        new JPanel();
        this.setBounds(0,530,810,40);
        this.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        this.setLayout(null);

        jl1=new JLabel("  0  个对象");
        jl1.setBounds(0,0,100,20);
        this.add(jl1);

        jl2.setBounds(680,0,150,20);
        try {
            showNumberOfChildren();
            showVolume();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.add(jl2);
    }

    public static void showNumberOfChildren(){
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

        int number=0;
        for(int i=0;i<FileMenu.fileTree.size();i++){
            if(FileMenu.fileTree.get(i).parent==parent){
                number++;
            }
        }
        jl1.setText("  "+String.valueOf(number)+"  个对象");
    }

    static public void showVolume() throws IOException {
        File f=new File();
        f.readFile(0);
        double d=(double)f.volume/1024;
        java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
        jl2.setText("共 30.0 MB  "+df.format(30.0-d)+" MB");
        System.out.print(df.format(32.0-d));
    }
}
