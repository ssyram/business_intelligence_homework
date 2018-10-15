package fpalgorithm.fpgrowth.util;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

public class FpTreeNode {
    private int item_num; // -1 means null
    private int count;

    private FpTreeNode parentNode;
    private ArrayList<FpTreeNode> childNodes;

    private FpTreeNode next = null; // for fp list

    /**
     * only use this constructor when wants to create the first node
     * whose parentNode is null
     */
    public FpTreeNode() {
        item_num = -1;
        parentNode = null;
    }

    public int getItem_num() {
        return item_num;
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
        if (childNodes == null)
            return 0;

        return childNodes.size();
    }
    public FpTreeNode getOnlyChildNode() {
        if (childNodes == null)
            return null;
        if (childNodes.size() != 1)
            throw new RuntimeException("it's not only child now");

        return childNodes.get(0);
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
        for (FpTreeNode node: childNodes)
            if (node.item_num == item_num)
                return node;

        return null;
    }

    public void addCount() {
        ++count;
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
        if (childNodes == null)
            childNodes = new ArrayList<>();
        childNodes.add(childNode);
    }
    public boolean isRoot() {
        return item_num == -1;
    }

    public void eliminateFromTree() {
        parentNode.childNodes.remove(this);
        parentNode = null;
    }
}
