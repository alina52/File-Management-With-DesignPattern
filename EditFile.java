import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by samsung on 2016/6/13.
 */
public class EditFile extends JFrame {
    ArrayList<Integer> pointers=new ArrayList<Integer>();
    JTextArea ja;
    String text;
    static String all;
    int id;

    public EditFile(File f,int id) throws IOException {
        this.id=id;
        new JFrame("ÎÄ¼þ±à¼­");
        setBounds(200,200,610,450);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int ret = JOptionPane.showConfirmDialog(null, "Save the change£¿","Caution",JOptionPane.OK_OPTION);
                if(ret==JOptionPane.OK_OPTION){
                    try {
                        clearData();
                        saveData();
                        Bottom.showVolume();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    super.windowClosing(windowEvent);
                } else{
                    super.windowClosing(windowEvent);
                }

            }
        });

        ja=new JTextArea();
        ja.setLineWrap(true);

        JScrollPane jp=new JScrollPane();
        jp.setViewportView(ja);
        this.add(jp);


        loadData(f);

        setVisible(true);
    }


    public void loadData(File f){
        text="";

        pointers.add(f.block);
        while(true){
            int indexT=pointers.get(pointers.size()-1)*1024;
            int block=0;
            for(int i=indexT;i<indexT+5;i++){
                if(all.charAt(i)=='1'){
                    block+=Math.pow(2,(indexT+5-i-1));
                }
            }
            if(block == 0){
                break;
            }

            pointers.add(block);
        }

        for(int i=0;i<pointers.size();i++){
            int index=pointers.get(i)*1024+5;
            int k=index;
            while(all.charAt(k)!=0){
                text+=all.charAt(k);
                k++;
                if(k-index==1019){
                    break;
                }
            }
        }



        ja.setText(text);
    }

    public void clearData() throws IOException {
        String temp="";
        for(int j=0;j<5;j++){
            temp+='1';
        }
        for(int j=5;j<1024;j++){
            char c=0;
            temp+=c;
        }
        for(int i=0;i<pointers.size();i++){
            int index=pointers.get(i)*1024;
            String s1=all.substring(0,index);
            String s2=temp;
            String s3=all.substring(index+1024,all.length());

            all=s1+s2+s3;
        }
        FileWriter fw=new FileWriter("Data.txt");
        fw.write(all.substring(0,16384));
        fw.write(all.substring(16384,all.length()));
        fw.close();
    }

    public void saveData() throws IOException {
        text=ja.getText();
        int number=text.length()/(1024-5)+1;
        ArrayList<String> texts=new ArrayList<>();
        ArrayList<Integer> blocks=new ArrayList<>();

        for(int i=1;i<31;i++) {
            int block=0;
            for (int index = i * 1024; index < i * 1024 + 5; index++) {
                if(all.charAt(index)=='1'){
                    block+=Math.pow(2,(i*1024+5)-index-1);
                }
            }
            if(block!=31){
                if(i==30){
                    System.out.print("Full!");
                }
                continue;
            }
            blocks.add(i);
            if(blocks.size()==number){
                break;
            }
        }

        for(int i=0;i<number-1;i++){
            String temp="";

            String c=Integer.toBinaryString(blocks.get(i+1));
            String pre="";
            for(int j=0;j<5-c.length();j++){
                pre+="0";
            }
            c=pre+c;

            temp+=c;
            temp+=text.substring(i*1019,(i+1)*1019);
            texts.add(temp);
        }
        String temp="";
        temp+="00000";
        temp+=text.substring((number-1)*1019,text.length());
        int length=temp.length();
        for(int i=0;i<(1024-length);i++){
            char c=0;
            temp+=c;
        }
        texts.add(temp);

        FileWriter fw = null;
        try {
            fw=new FileWriter("Data.txt");
            for(int i=0;i<number;i++){
                String s1=all.substring(0,blocks.get(i)*1024);
                String s3=all.substring((blocks.get(i)+1)*1024,all.length());

                all=s1+texts.get(i)+s3;
            }
            fw.write(all);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        saveFile(blocks.get(0));
    }

    public void saveFile(int block) throws IOException {
        FileReader fr=new FileReader("Files.txt");
        String temp="";
        int ch=0;
        while((ch=fr.read())!=-1){
            temp+=(char)ch;
        }
        int index=0;
        for(int i=0;i<id;i++){
            while(temp.charAt(index)!='.'){
                index++;
            }
            index++;
        }
        int index2=index;
        while(temp.charAt(index2)!='.')
            index2++;
        index2++;

        File f=new File();
        f.readFile(id);

        String main=f.name+',';
        for(int i=0;i<f.location.size();i++){
            main+=f.location.get(i)+'/';
        }
        main+=','+f.time+',';
        if(f.isFile){
            main+="1,";
        }else{
            main+="0,";
        }
        main+=String.valueOf(ja.getText().length())+',';
        String c=Integer.toBinaryString(block);
        String pre="";
        for(int j=0;j<5-c.length();j++){
            pre+="0";
        }
        c=pre+c;
        main+=c+'.';

        FileWriter fw=new FileWriter("Files.txt");
        fw.write(temp.substring(0,index)+main+temp.substring(index2,temp.length()));
        fw.close();

        ((DefaultListModel) MainPanel.jl.getModel()).removeAllElements();
        MainPanel.createList(FileMenu.fileTree.get(id).parent);

        editFolderVolumn(FileMenu.fileTree.get(id).parent);
    }

    void editFolderVolumn(int parent) throws IOException {
        FileReader fr=null;
        fr=new FileReader("Files.txt");
        String allFiles="";
        int ch=0;
        while((ch=fr.read())!=-1){
            allFiles+=(char)ch;
        }
        fr.close();

        int total=0;
        FileTreeItem ftt=FileMenu.fileTree.get(FileMenu.fileTree.get(parent).child);
        while(ftt.sibling!=-1){
            File f=new File();
            f.readFile(ftt.self);
            total+=f.volume;

            ftt=FileMenu.fileTree.get(ftt.sibling);
        }
        File f=new File();
        f.readFile(ftt.self);
        total+=f.volume;

        int index=0,index2;
        for(int i=0;i<parent;i++){
            while(allFiles.charAt(index)!='.')
                index++;
            index++;
        }
        index2=index;
        while(allFiles.charAt(index2)!='.')
            index2++;
        index2++;

        f=new File();
        f.readFile(parent);
        String s1=allFiles.substring(0,index);
        String s2=f.name+',';
        for(int i=0;i<f.location.size();i++){
            s2+=f.location.get(i)+'/';
        }
        s2+=','+f.time+",0,"+String.valueOf(total)+'.';
        String s3=allFiles.substring(index2,allFiles.length());

        allFiles=s1+s2+s3;

        FileWriter fw=new FileWriter("Files.txt");
        fw.write(allFiles);
        fw.close();

        if(FileMenu.fileTree.get(parent).parent!=-1)
            editFolderVolumn(FileMenu.fileTree.get(parent).parent);
    }

}
