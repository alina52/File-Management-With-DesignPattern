package File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import Util.Date;
import java.util.ArrayList;

public class FileLoader {
    private static File ret;
    private static FileReader fr;

    public static File readFile(int id) {
        ret = new File();

        try {
            init(id);

            setName();
            setLocation();
            setTime();
            setIsFile();
            setVolumn();
            setBlock();

            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private static String init(int id) throws IOException {
        fr = new FileReader("Files.txt");
        String all = "";

        for (int i = 0; i < id; i++) {
            int ch = 0;
            while ((char) (ch = fr.read()) != '.')
                all += (char) ch;
            all += '.';
        }
        return all;
    }

    public static void save(int id, File file) {
        try {
            String all = init(FileTree.getFileTree().size());

            String pre = init(id + 1);
            String post = all.substring(pre.length(), all.length());
            pre = init(id);

            String replacement = formatFile(file);
            all = pre + replacement + post;

            FileWriter fw = new FileWriter("Files.txt");
            fw.write(all);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(File file){
        try {
            String all = init(FileTree.getFileTree().size());
            String add = formatFile(file);

            FileWriter fw = new FileWriter("Files.txt");
            fw.write(all+add);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void remove(int id){
        try {
            String all = init(FileTree.getFileTree().size());
            String pre = init(id);
            String post = all.substring(init(id + 1).length(), all.length());

            FileWriter fw = new FileWriter("Files.txt");
            fw.write(pre + post);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatFile(File file) {
        String ret = "";
        ret += file.getName() + ",";
        for (String temp : file.getLocation()) {
            ret += temp + "/";
        }
        ret += ",";

        ret+= Date.getDate();

        if (file.getIsFile()) {
            ret += ",1,";
        } else {
            ret += ",0,";
        }
        if(file.getIsFile()) {
            ret += file.getVolume() + ",";
            ret += file.getBlock() + ".";
        } else {
            ret += file.getVolume() + ".";
        }

        return ret;
    }


    private static void setName() throws IOException {
        int ch;
        String tempName = "";
        while ((char) (ch = fr.read()) != ',') {
            tempName += (char) ch;
        }
        ret.setName(tempName);
    }

    private static void setLocation() throws IOException {
        int ch;
        String temp = "";

        ret.getLocation().clear();
        ArrayList<String> tempLocation = new ArrayList<>();
        while ((char) (ch = fr.read()) != ',') {
            temp += (char) ch;
            while ((char) (ch = fr.read()) != '/') {
                temp += (char) ch;
            }
            tempLocation.add(temp);
            temp = "";
        }
        ret.setLocation(tempLocation);
    }

    private static void setTime() throws IOException {
        int ch;
        String tempTime = "";
        while ((char) (ch = fr.read()) != ',') {
            tempTime += (char) ch;
            while ((char) (ch = fr.read()) != '/') {
                tempTime += (char) ch;
            }
            tempTime += '/';
        }
        ret.setTime(tempTime);
    }

    private static void setIsFile() throws IOException {
        int ch;
        boolean tempIsFile = false;
        ch = fr.read();
        if ((char) ch == '1') {
            tempIsFile = true;
        } else {
            tempIsFile = false;
        }
        ret.setIsFile(tempIsFile);
    }

    private static void setVolumn() throws IOException {
        int ch, tempVolume;
        String temp = "";
        fr.read();//吸入一个','
        if (!ret.getIsFile()) {
            while ((char) (ch = fr.read()) != '.') {
                temp += (char) ch;
            }
        } else {
            while ((char) (ch = fr.read()) != ',') {
                temp += (char) ch;
            }
        }
        tempVolume = Integer.valueOf(temp);
        ret.setVolume(tempVolume);
    }

    private static void setBlock() throws IOException {
        String tempId = "";
        int tempBlock = -1, ch;
        if (ret.getIsFile()) {
            /*for (int i = 0; i < 5; i++) {
                ch = fr.read();
                if ((char) ch == '0') {
                    tempId[i] = false;
                } else {
                    tempId[i] = true;
                }
            }*/
            while((char)(ch = fr.read()) != '.'){
                tempId += (char)ch;
            }
            tempBlock = Integer.valueOf(tempId);
        }
        ret.setBlock(tempBlock);
    }


}
