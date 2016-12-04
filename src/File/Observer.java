package File;

import FileManagement.Bottom;
import FileManagement.Header;
import FileManagement.Page;


public class Observer {
    static void toNotifyVolume() {
        Bottom.updateVolume(FileTree.getUsedVolumn(),FileTree.totalVolume);
    }

    static void toNotifyCurrent() {
        Page.createList(FileTree.getCurrent());

        Bottom.updateObjects(FileTree.getObjects());
        Header.showAddress();
    }
}