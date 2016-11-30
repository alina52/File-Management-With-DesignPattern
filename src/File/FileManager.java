package File;

import Util.Date;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class FileManager {
    private static int copied = -1;

    public static void modifyFileVolume(int id, int length) {
        File file = FileLoader.readFile(id);
        int oldVolume = file.getVolume();
        file.setVolume(length);
        FileLoader.save(id, file);

        modifyFolderVolume(id, length - oldVolume);
    }

    public static void modifyFolderVolume(int id, int offsert) {
        FileTreeItem fti = FileTree.getFileTree().get(id);
        while (fti.getParent() != -1) {
            File f = FileLoader.readFile(fti.getParent());
            f.setVolume(f.getVolume() + offsert);
            FileLoader.save(fti.getParent(), f);

            fti = FileTree.getFileTree().get(fti.getParent());
        }
        FileTree.setUsedVolumn(FileLoader.readFile(0).getVolume());
    }

    public static void createFile(String name) {
        ArrayList<String> location = getLocation();

        File file = new File();
        file.setName(checkName(name));
        file.setLocation(location);
        file.setTime(Date.getDate());
        file.setIsFile(true);
        file.setVolume(0);
        file.setBlock(DataManager.newFile());

        FileLoader.add(file);
        FileTree.add();

        for (String temp : file.getLocation()) {
            System.out.print("/" + temp);
        }
    }

    public static void createFolder(String name) {
        ArrayList<String> location = getLocation();

        File file = new File();
        file.setName(checkName(name));
        file.setLocation(location);
        file.setTime(Date.getDate());
        file.setIsFile(false);
        file.setVolume(0);

        FileLoader.add(file);
        FileTree.add();

        for (String temp : file.getLocation()) {
            System.out.print("/" + temp);
        }
    }

    private static String checkName(String name) {
        FileTreeItem parent = FileTree.getFileTree().get(FileTree.getCurrent());
        ArrayList<Integer> children = (ArrayList<Integer>) FileTree.findChildren(parent);

        while (true) {
            boolean flag = true;
            for (int child : children) {
                File file = FileLoader.readFile(child);
                if (file.getName().equals(name)) {
                    name += "_untitled";
                    flag = false;
                    break;
                }
            }
            if (!flag)
                continue;
            else
                break;
        }

        return name;
    }

    private static ArrayList<String> getLocation() {
        ArrayList<String> location = new ArrayList<>();
        File current = FileLoader.readFile(FileTree.getCurrent());
        location.addAll(current.getLocation());
        location.add(current.getName());
        return location;
    }

    public static void removeFile(int id) {
        FileLoader.remove(id);
    }

    public static int getCopied() { return copied; }

    public static void setCopied(int toCopy) {
        copied = toCopy;
    }

    public static void toPaste() {
        File file = FileLoader.readFile(copied);

        createFile(file.getName());

        int newFileId = FileTree.getFileTree().size() - 1;
        File newFile = FileLoader.readFile(newFileId);
        ArrayList<Integer> pointer = new ArrayList<>();
        pointer.add(newFile.getBlock());

        DataManager.modifyFile(newFileId, pointer, DataManager.getData(file.getBlock()));

        copied = -1;

        FileTree.setCurrent(FileTree.getCurrent());
    }
}
