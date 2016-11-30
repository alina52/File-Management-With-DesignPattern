package FileManagement;

import java.awt.*;

public class MenuBars extends MenuBar {
    private static Menu menu[];
    private static MenuItem menuItems[];

    public MenuBars() {
        super();

        menu = new Menu[2];
        menuItems = new MenuItem[8];

        menu[0] = new Menu(" NiYiwei ");
        menu[1] = new Menu(" 1454094 ");


        for (int i = 0; i < 2; i++)
            this.add(menu[i]);
    }
}
