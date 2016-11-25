package File;

import Util.Date;
import com.sun.prism.shader.FillEllipse_Color_AlphaTest_Loader;

import java.util.ArrayList;

public class File {
    private int block, volume;
    private String name, time;
    private ArrayList<String> location;
    private boolean isFile;

    public File(){
        location = new ArrayList<>();
    }

    public File(String name, int block) {
        location = new ArrayList<>();

        File current = FileLoader.readFile(FileTree.getCurrent());
        location.addAll(current.getLocation());
        location.add(current.getName());

        String time = Date.getDate();

        this.setName(name);
        this.setTime(time);
        this.setLocation(location);
        this.setIsFile(true);
        this.setVolume(0);
        this.setBlock(block);
    }



    public void setVolume(int volume) { this.volume = volume; }
    public int getVolume() { return volume; }

    public void setBlock(int block) { this.block = block; }
    public int getBlock() { return block; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setTime(String time) { this.time = time; }
    public String getTime() { return time; }

    public void setLocation(ArrayList<String> location) { this.location = location; }
    public ArrayList<String> getLocation() { return location; }

    public void setIsFile(boolean isFile) { this.isFile = isFile; }
    public boolean getIsFile() { return isFile; }

    public String getFullPath() {
        String ret = " ";
        for (String temp : location) {
            ret += temp + " / ";
        }
        ret += name + " / ";
        return ret;
    }


}