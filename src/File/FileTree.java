package File;

import FileManagement.Bottom;
import FileManagement.Header;
import FileManagement.Page;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTree extends IFileTree {
    private final static int totalVolume = 30*1024;
    private static File root;
    private static int current, objects, usedVolume;
    private static ArrayList<FileTreeItem> fileTree;

    public FileTree(int initial) {
        root = FileLoader.readFile(0);

        try {
            createFileTree();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setCurrent(initial);
        setDefaultSettings();
    }

    private void setDefaultSettings() { usedVolume = 0; }

    private void createFileTree() throws IOException {
        FileReader fr = new FileReader("FileTree.txt");
        fileTree = new ArrayList<FileTreeItem>();

        int ch;
        while ((ch = fr.read()) != -1) {

            FileTreeItem ftiTemp = new FileTreeItem();

            ftiTemp.setSelf(helper(fr, ',', ch));
            ftiTemp.setChild(helper(fr, ','));
            ftiTemp.setSibling(helper(fr, ','));
            ftiTemp.setParent(helper(fr, '.'));

            fileTree.add(ftiTemp);
        }
    }

    private int helper(FileReader fr, char end, int... c) throws IOException {
        int ch;
        String temp = "";
        if (c.length == 1) {
            temp += (char) c[0];
        }
        while ((char) (ch = fr.read()) != end) {
            temp += (char) ch;
        }
        if (temp != "")
            return Integer.valueOf(temp);
        else
            return -1;
    }

    public static void add() {
        FileTreeItem parent = FileTree.findFileTreeItem(current), sibling = null;
        int id = fileTree.size();
        if (parent.getChild() == -1) {
            parent.setChild(id);
        } else {
            sibling = findFileTreeItem(parent.getChild());
            if (sibling.getSibling() != -1) {
                do {
                    sibling = findFileTreeItem(sibling.getSibling());
                } while (sibling.getSibling() != -1);
            }
            sibling.setSibling(id);
        }

        FileTreeItem fti = new FileTreeItem();

        fti.setSelf(id);
        fti.setChild(-1);
        fti.setSibling(-1);
        fti.setParent(parent.getSelf());

        fileTree.add(fti);

        write();
    }

    public static void remove(int id){
        int toDelete = fileTree.get(id).getSibling();
        for(FileTreeItem temp: fileTree){
            if(temp.getChild() == id){
                temp.setChild(toDelete);
            }
            if(temp.getSibling() == id){
                temp.setSibling(toDelete);
            }

            if(temp.getSelf() > id)
                temp.setSelf(temp.getSelf() -1);
            if(temp.getChild()>id)
                temp.setChild(temp.getChild()-1);
            if(temp.getSibling()>id)
                temp.setSibling(temp.getSibling()-1);
            if(temp.getParent()>id)
                temp.setParent(temp.getParent()-1);
        }
        fileTree.remove(id);

        write();
    }

    private static String formatFileTree(){
        String ret = "";

        for(FileTreeItem temp : fileTree) {
            ret += "" + temp.getSelf() + ',';
            ret += "" + temp.getChild() + ',';
            ret += "" + temp.getSibling() + ',';
            ret += "" + temp.getParent() + '.';
        }
        return ret;
    }

    private static void write(){
        try {
            FileWriter fw = new FileWriter("FileTree.txt");
            fw.write(formatFileTree());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getUsedVolumn() { return usedVolume; }
    public static void setUsedVolumn(int usedVolumn) {
        FileTree.usedVolume = usedVolumn;

        new observer().toNotifyVolume();
    }

    public static int getCurrent() { return current; }
    public static void setCurrent(int current) {
        FileTree.current = current;

        new observer().toNotifyCurrent();
    }

    public static List<FileTreeItem> getFileTree() {
        return fileTree;
    }

    public static int getObjects() { return objects; }
    public static void setObjects(int objects) {
        FileTree.objects = objects;
    }

}
