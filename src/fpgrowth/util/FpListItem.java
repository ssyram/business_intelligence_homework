package fpgrowth.util;

public class FpListItem {
    private int key;

    private FpTreeNode first = null;
    private FpTreeNode last = null;

    public FpListItem(int key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }
}
