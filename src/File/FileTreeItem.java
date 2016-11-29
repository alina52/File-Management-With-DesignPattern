package File;

public class FileTreeItem {
    private int self, child, sibling, parent;

    public FileTreeItem() {
        self = child = sibling = parent = -1;
    }

    public int getSelf() { return this.self; }
    public void setSelf(int self) { this.self = self; }

    public int getChild() { return this.child; }
    public void setChild(int child) { this.child = child; }

    public int getSibling() { return this.sibling; }
    public void setSibling(int sibling) { this.sibling = sibling; }

    public int getParent() { return this.parent; }
    public void setParent(int parent) { this.parent = parent; }
}
