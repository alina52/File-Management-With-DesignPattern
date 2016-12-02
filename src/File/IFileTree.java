package File;

import java.util.ArrayList;
import java.util.List;

public abstract class IFileTree {
    public static FileTreeItem findFileTreeItem(int id) {
        FileTreeItem ret = new FileTreeItem();
        for (FileTreeItem temp : FileTree.getFileTree()) {
            if (temp.getSelf() == id)
                ret = temp;
        }

        return ret;
    }

    public static List<Integer> findChildren(FileTreeItem parent) {
        ArrayList<Integer> ret = new ArrayList<>();

        if (parent.getChild() != -1) {
            ret.add(parent.getChild());

            FileTreeItem children = findFileTreeItem(parent.getChild());
            if (children.getSibling() != -1) {
                do {
                    ret.add(children.getSibling());
                    children = findFileTreeItem(children.getSibling());
                } while (children.getSibling() != -1);
            }

        }

        return ret;
    }

    public static int findByNameNPath(String name, ArrayList<String> path) {

        for (int i = 0; i < FileTree.getFileTree().size(); i++) {
            File file = FileLoader.readFile(i);

            if (file.getName().equals(name) && file.getLocation().size() == path.size()) {
                boolean flag = true;
                for (int j = 0; j < path.size(); j++) {
                    if(!path.get(j).equals(file.getLocation().get(j))){
                        flag = false;
                        break;
                    }
                }
                if(flag)
                    return i;
            }
        }

        return -1;
    }
}
