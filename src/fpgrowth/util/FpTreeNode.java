package fpgrowth.util;

import java.util.ArrayList;

public class FpTreeNode {
    private int item_num; // -1 means null
    private int count;

    FpTreeNode parentNode;
    ArrayList<FpTreeNode> childNodes;

    FpTreeNode next; // for fp list
}
