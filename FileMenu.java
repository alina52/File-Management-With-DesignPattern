import java.io.*;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by samsung on 2016/6/11.
 */
public class FileMenu {
    static File root;
    static ArrayList<FileTreeItem> fileTree;

    public FileMenu() throws IOException {
        root=new File(true,null,null);
        root.name="Root";

        createFileMenu();
    }

    void createFileMenu() throws IOException {
        FileReader fr=new FileReader("FileTree.txt");
        fileTree=new ArrayList<FileTreeItem>();

        int ch;
        String temp=new String();
        StringBuffer sb=new StringBuffer();

        while((ch=fr.read())!=-1) {
            FileTreeItem ftTemp = new FileTreeItem();
            temp += (char) ch;
            while ((char) (ch = fr.read()) != ',') {
                temp += (char) ch;
            }
            ftTemp.self = Integer.valueOf(temp);
            temp = new String();
            while ((char) (ch = fr.read()) != ',') {
                temp += (char) ch;
            }
            ftTemp.child = Integer.valueOf(temp);
            temp = "";
            while ((char) (ch = fr.read()) != ',') {
                temp += (char) ch;
            }
            ftTemp.sibling = Integer.valueOf(temp);
            temp = "";
            while ((char) (ch = fr.read()) != '.') {
                temp += (char) ch;
            }
            ftTemp.parent = Integer.valueOf(temp);
            temp="";

            fileTree.add(ftTemp);
        }
    }
}
