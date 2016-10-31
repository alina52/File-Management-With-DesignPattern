import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by samsung on 2016/6/11.
 */
public class File {
    public boolean[] Id;
    public int block;
    public String name;
    public ArrayList<String> location=new ArrayList<String>();
    public String time;
    public boolean isFile;
    public File child,sibling,parent;
    public int volume;

    public File(){
        Id=new boolean[10];
        block=-1;
        name="untitled";
        child=sibling=parent=null;
        volume=0;
    }

    public File(boolean iF,ArrayList<String> l,String p){
        Id=new boolean[10];
        block=-1;
        name="untitled";
        isFile=iF;
        child=sibling=parent=null;
        volume=0;

        if(l!=null) {
            for (int i = 0; i < l.size(); i++) {
                location.add(l.get(i));
            }
        }

        if(p!=null) {
            location.add(p);
        }

    }

    public void readFile(int id) throws IOException {
        FileReader fr=new FileReader("Files.txt");
        String temp="";

        for(int i=0;i<id;i++){
            int ch=0;
            while((char)(ch=fr.read())!='.');
        }

        int ch=0;
        name="";
        while((char)(ch=fr.read())!=','){
            name+=(char)ch;
        }
        location.clear();
        while((char)(ch=fr.read())!=',') {
            temp+=(char)ch;
            while ((char) (ch = fr.read()) != '/') {
                temp +=(char)ch;
            }
            location.add(temp);
            temp="";
        }
        time="";
        while((char)(ch=fr.read())!=','){
            time+=(char)ch;
            while ((char) (ch = fr.read()) != '/') {
                time+=(char)ch;
            }
            time+='/';
        }
        isFile=false;
        ch=fr.read();
        if((char)ch=='1'){
            isFile=true;
        } else {
            isFile=false;
        }
        volume=0;
        temp="";
        fr.read();//ÎüÈëÒ»¸ö','
        if(!isFile) {
            while ((char) (ch = fr.read()) != '.') {
                temp += (char) ch;
            }
        } else {
            while ((char) (ch = fr.read()) != ',') {
                temp += (char) ch;
            }
        }
        volume=Integer.valueOf(temp);
        if(isFile){
            for(int i=0;i<5;i++){
                ch=fr.read();
                if((char)ch=='0'){
                    Id[i]=false;
                } else {
                    Id[i]=true;
                }
            }
            block=0;
            for(int j=0;j<5;j++){
                if(Id[j]){
                    block+=Math.pow(2,(4-j));
                }else{
                }
            }
            fr.read();

        }
        fr.close();
    }
}
