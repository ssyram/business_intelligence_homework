package fpalgorithm.fpgrowth.util;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FpTreeNode {
    private int item_num; // -1 means null
    private int count;

    private FpTreeNode parentNode;
    private Map<Integer, FpTreeNode> childNodes = new HashMap<>();

    private FpTreeNode next = null; // for fp list

    @Override
    public int hashCode() {
        return item_num;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FpTreeNode)
            return item_num == ((FpTreeNode) o).item_num;

        return false;
    }

    /**
     * only use this constructor when wants to create the first node
     * whose parentNode is null
     */
    public FpTreeNode() {
        item_num = -1;
        parentNode = null;
    }

    public int getCount() {
        return count;
    }
    public FpTreeNode getParentNode() {
        return parentNode;
    }
    public FpTreeNode getNext() {
        return next;
    }

    public int getChildNodeCount() {
        return childNodes.size();
    }
    public FpTreeNode getOnlyChildNode() {
        if (childNodes.size() == 0)
            return null;
        if (childNodes.size() != 1)
            throw new RuntimeException("it's not only child now");

        return childNodes.values().iterator().next();
    }

    /**
     * always not set parentNode to null
     * @param item_num the item num in this node
     * @param parentNode if it's null, it'll throw null pointer exception
     */
    public FpTreeNode(int item_num, @NotNull FpTreeNode parentNode) {
        this.item_num = item_num;
        this.parentNode = parentNode;
        parentNode.addChildNode(this);
    }

    public FpTreeNode findChildNode(int item_num) {
        return childNodes.get(item_num);
    }

    public void addCount(int additionalCount) {
        count += additionalCount;
    }

    public int getItem_Num() {
        return item_num;
    }

    void setNextNode(FpTreeNode nextNode) {
        next = nextNode;
    }
    private void addChildNode(FpTreeNode childNode) {
//        childNodes.computeIfPresent(childNode.item_num, (k, v) -> {throw new RuntimeException("already exists.");});
        if (childNodes.get(childNode.item_num) != null)
            throw new RuntimeException("child node with num: " + childNode.item_num + "already existed.");
        childNodes.put(childNode.item_num, childNode);
    }
    public boolean isRoot() {
        return item_num == -1;
    }

    public void eliminateFromTree() {
        parentNode.childNodes.remove(item_num);
        parentNode = null;
    }
}
