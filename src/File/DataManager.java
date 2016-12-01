package File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.*;

import java.lang.String;

//31表明为空，0表明这块已占用，非0或非31表明下一块的索引
public class DataManager {
    private static String allData = "";
    private final static int BlockSize = 1024, PointerSize = 5, BlockNumber = 32;


    public DataManager() {
        loadAllData();
    }

    private static void loadAllData() {
        FileReader fr = null;
        try {
            fr = new FileReader("Data.txt");
            int ch = 0;
            while ((ch = fr.read()) != -1) {
                allData += (char) ch;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<ArrayList<Integer>, String> loadFile(int block) {
        String text = "";
        ArrayList<Integer> pointers = getPointers(block);

        for (int index : pointers) {
            index = index * BlockSize + PointerSize;
            int k = index;
            while (allData.charAt(k) != 0) {
                text += allData.charAt(k);
                k++;
                if (k - index == (BlockSize - PointerSize)) {
                    break;
                }
            }
//            DataIterator dataIterator = new DataIterator(index);
//            text += dataIterator.getText();
        }
        Map ret = new HashMap<ArrayList<Integer>, String>();
        ret.put(pointers, text);
        return ret;
    }

    public static void modifyFile(int id, ArrayList<Integer> pointers, String data) {
        deleteData(pointers);

        saveData(data);

        FileManager.modifyFileVolume(id, data.length());
    }

    private static void deleteData(ArrayList<Integer> pointers) {
        String temp = "";
        for (int j = 0; j < PointerSize; j++) {
            temp += '1';
        }
        for (int j = 5; j < BlockSize; j++) {
            char c = 0;
            temp += c;
        }
        for (int tempPointer : pointers) {
            int index = tempPointer * BlockSize;
            String s1 = allData.substring(0, index);
            String s2 = temp;
            String s3 = allData.substring(index + BlockSize, allData.length());

            allData = s1 + s2 + s3;
        }
    }

    public static String getData(int block) {
        Map<ArrayList<Integer>,String> get = DataManager.loadFile(block);
        ArrayList<Integer> pointers = (ArrayList<Integer>) get.keySet().toArray()[0];
        return get.get(pointers);
    }

   

    private static List<Integer> getFreeBlockNumber(int number) {
        ArrayList<Integer> ret = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            DataIterator dataIterator = new DataIterator();
            for(;dataIterator.hasNext();) {
                if(dataIterator.getPointer() == 31) {
                    ret.add(dataIterator.getCurrent());
                    break;
                }
            }
        }
        return ret;
    }

    public static int saveData(String data) {
        int numbers = data.length() / (BlockSize - PointerSize) + 1;
        ArrayList<String> splittedData = new ArrayList<>();
        List<Integer> pointers = getFreeBlockNumber(numbers);

        for (int i = 0; i < numbers; i++) {
            int lastIndex;
            if(numbers == 1 || i == numbers - 1)
                lastIndex = data.length();
            else
                lastIndex = (i + 1) * (BlockSize - PointerSize);
            splittedData.add(data.substring(i * (BlockSize - PointerSize), lastIndex));
        }

        for (int i = 0; i < numbers; i++) {
            int next = (numbers == 1 || i == numbers -1 ? 0 : pointers.get(i + 1 ));

            insertSingleData(pointers.get(i),splittedData.get(i),next);
        }
        return pointers.get(0);
    }

    public static int newFile(){
        return saveData("");
    }

    private static void insertSingleData(int id, String data, int next) {
        String pre = Util.String.dec2bin(next);
        String insert = pre + data;

        if(next == 0 || next == BlockNumber - 1) {
            char ch = 0;
            while(insert.length() < BlockSize) {
                insert += ch;
            }
        }

        allData = allData.substring(0, id * BlockSize) + insert + allData.substring((id + 1) * BlockSize, allData.length());
    }

    public static void deleteFile(int block){
        ArrayList<Integer> pointers = getPointers(block);

        for(int temp:pointers){
            insertSingleData(temp,"",31);
        }
    }

    private static ArrayList<Integer> getPointers(int first) {
        ArrayList<Integer> ret = new ArrayList<>();
        ret.add(first);
        while (true) {
            int index = ret.get(ret.size() - 1) * BlockSize, block = 0;
            for (int i = index; i < index + PointerSize; i++) {
                if (allData.charAt(i) == '1') {
                    block += Math.pow(2, (index + PointerSize - i - 1));
                }
            }
            if (block == 0) {
                break;
            }
            ret.add(block);
        }

        return ret;
    }


}
