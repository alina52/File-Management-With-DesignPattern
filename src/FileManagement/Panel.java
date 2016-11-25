package FileManagement;

public class Panel {
	Tree tree;
    Page page;

    public Panel(){
        super();

        setDefaultSettings();

        loadItems();
    }

    private void setDefaultSettings(){
        this.setBounds(0, 40, 810, 490);
        this.setLayout(null);
    }

    private void loadItems(){
        JButton text=new JButton("Name                                           Time               " +
                "                            Type                                             Volume" +
                "                                           ");
        text.setBackground(Color.white);
        text.setBorder(BorderFactory.createEmptyBorder());
        text.setForeground(new Color(0, 0, 0));
        text.setEnabled(false);
        text.setBounds(151,-2,653,24);
        this.add(text);

        page = new Page();
        page.setBounds(151, 24, 653, 462);
        page.setBorder(BorderFactory.createEmptyBorder());
        this.add(page);

        tree = new Tree();
        this.add(tree);
    }
}
