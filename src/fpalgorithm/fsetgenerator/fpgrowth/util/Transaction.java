package fpalgorithm.fsetgenerator.fpgrowth.util;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private List<Integer> itemContent;
    private int count = 1;

    public Transaction(List<Integer> content) {
        itemContent = content;
    }
    public Transaction(List<Integer> content, int count) {
        itemContent = content;
        this.count = count;
    }

    public List<Integer> getItemContent() {
        if (itemContent == null)
            itemContent = new ArrayList<>();
        return itemContent;
    }

    public int getCount() {
        return count;
    }

    public int getContentAt(int pos) {
        return itemContent.get(pos);
    }
}
