package fpalgorithm.fsetgenerator.fpgrowth.util;

public class FpListItem {
    private int key;
    private int count = 0;

    private FpTreeNode first = null;
    private FpTreeNode last = null;

    public FpListItem(int key) {
        this.key = key;
    }
    public FpListItem(int key, int count) {
        this.key = key;
        this.count = count;
    }

    public Integer getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return key;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof FpListItem)
            return key == ((FpListItem)o).key;

        return false;
    }

    public void addNext(FpTreeNode nextNode) {
        if (first == null) {
            first = nextNode;
            last = nextNode;
            return;
        }
        last.setNextNode(nextNode);
        last = nextNode;
    }

    public FpTreeNode getFirst() {
        return first;
    }

    public void addCount() {
        ++count;
    }
    public void addCount(int additionalCount) {
        this.count += additionalCount;
    }
    public int getCount() {
        return count;
    }
}
