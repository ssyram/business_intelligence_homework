package fpgrowth.util;

public class FpListItem {
    private int key;
    private int count;
    private int listCount = 0;

    private FpTreeNode first = null;
    private FpTreeNode last = null;

    public FpListItem(int key, int count) {
        this.key = key;
        this.count = count;
    }

    public Integer getKey() {
        return key;
    }

    public Integer getCount() {
        return count;
    }

    public int getListCount() {
        return listCount;
    }
}
