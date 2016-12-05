package FileManagement;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

import File.*;


public class EditFile extends JFrame{
    private JTextArea textArea;
    private String context;
    private int id;
    private ArrayList<Integer> pointers;

	public EditFile(File file, int id) {
        super("文件编辑");
        setDefaultSettings();

        this.id = id;
        loadItems();

        this.addWindowListener(new windowsAdapter());
        this.setVisible(true);


        Map<ArrayList<Integer>,String> get = DataManager.loadFile(file.getBlock());
        pointers = (ArrayList<Integer>) get.keySet().toArray()[0];
        String text = get.get(pointers);
        setContext(text);
    }

    private void loadItems() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(textArea);
        this.add(jScrollPane);
    }

    private void setDefaultSettings() {
        setBounds(200, 200, 610, 450);
        setResizable(false);
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
        textArea.setText(context);
    }

    private class windowsAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            DataManager.modifyFile(id,pointers,textArea.getText());
            FileTree.setCurrent(FileTree.getCurrent());
            super.windowClosing(e);
        }
    }
}
