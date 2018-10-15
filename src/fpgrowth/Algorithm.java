package fpgrowth;

import fpgrowth.result.AssociationRule;
import fpgrowth.result.FrequentSet;
import fpgrowth.util.FpListItem;
import fpgrowth.util.FpTreeNode;
import fpgrowth.util.Transaction;
import javafx.util.Pair;
import util.GlobalInfo;

import java.util.*;

public class Algorithm {

    /**
     * @param orderedItemList a list of ordered frequent items
     * @param transactions all transactions
     * @return a set of association rules which will contain all information that is interested
     */
    public static Set<FrequentSet> calculateRules(
            List<FpListItem> orderedItemList,
            List<Transaction> transactions
    ) {
        return calculateRules(orderedItemList, transactions, null);
    }

    /**
     *
     * @param orderedItemList a list of ordered frequent items
     * @param transactions all transactions
     * @param item_orderMap the map of item and corresponding order, which means the order in
     *                      orderedItemList
     * @return a set of association rules which will contain all information that is interested
     */
    public static Set<FrequentSet> calculateRules(
            List<FpListItem> orderedItemList,
            List<Transaction> transactions,
            Map<Integer, Integer> item_orderMap
    ) {
        if (item_orderMap == null) {
            item_orderMap = new HashMap<>();
            for (int i = 0; i < orderedItemList.size(); ++i)
                item_orderMap.put(orderedItemList.get(i).getKey(), i);
        }
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();

        Set<FrequentSet> r = new HashSet<>();

        FpTreeNode root = createTree(orderedItemList,
                transactions, item_orderMap, true);

        mining(r, orderedItemList, item_orderMap);

        return r;
    }

    private static void mining(
            Set<FrequentSet> r,
            List<FpListItem> orderedItemList,
            Map<Integer, Integer> item_orderMap
    ) {
        for (int i = orderedItemList.size() - 1; i >= 0; --i) {
            FpListItem item = orderedItemList.get(i);
            // get cpb and corresponding itemSet
            Pair<List<Transaction>, List<FpListItem>> analyzedData = analyzePath(item);
            List<Transaction> transactions = analyzedData.getKey();
            // turn itemSet to itemList
            List<FpListItem> itemList = analyzedData.getValue();
            // get itemList ordered
            itemList.sort(
                    (a, b) -> (item_orderMap.get(a.getKey()) - item_orderMap.get(b.getKey()))
            );

            Map<Integer, Integer> nioMap = new HashMap<>();
            for (int j = 0; j < analyzedData.getValue().size(); ++j)
                nioMap.put(itemList.get(i).getKey(), i);

            FpTreeNode treeRoot = createTree(itemList, transactions, nioMap, false);
            Set<Integer> postFixKeySet = new HashSet<>();
            postFixKeySet.add(item.getKey());
            miningTree(r, treeRoot, itemList, postFixKeySet);
        }
    }

    private static void miningTree(Set<FrequentSet> r, FpTreeNode treeRoot, List<FpListItem> orderedItemList, Set<Integer> postFixKeySet) {
        trimTree(orderedItemList);
        if (isSingleBranchOrNull(treeRoot)) {

        }
    }

    private static void trimTree(List<FpListItem> orderedItemList) {
        for (int i = orderedItemList.size() - 1; i >= 0; --i) {
            FpListItem item = orderedItemList.get(i);
            if (item.getCount() > GlobalInfo.total_support)
                return;

            for (FpTreeNode node = item.getFirst(); node != null; node = node.getNext())
                node.eliminateFromTree();
            orderedItemList.remove(i);
        }
    }

    private static Pair<List<Transaction>, List<FpListItem>> analyzePath(FpListItem item) {
        Set<Integer> checkRs = new HashSet<>();
        List<Transaction> rt = new ArrayList<>();
        List<FpListItem> rs = new ArrayList<>();
        for (FpTreeNode node = item.getFirst(); node != null; node = node.getNext()) {
            List<Integer> cpb = new ArrayList<>();
            for (FpTreeNode nnode = node.getParentNode(); !nnode.isRoot(); nnode = nnode.getParentNode()) {
                int item_num = nnode.getItem_Num();
                if (checkRs.add(item_num))
                    rs.add(new FpListItem(item_num));

                cpb.add(item_num);
            }
            rt.add(new Transaction(cpb, node.getCount()));
        }

        return new Pair<>(rt, rs);
    }

    // for that variables use in lambda should be final or effectively final
    // we can't create the corresponding item_orderMap in cpb mode
    // //     if item_orderMap is null, that means it's in cpb mode
    // //     which means that it is ordered
    private static FpTreeNode createTree(
            List<FpListItem> orderedItemList,
            List<Transaction> transactions,
            Map<Integer,Integer> item_orderMap,
            boolean isUnordered
    ) {
//        boolean isUnordered = true; //it means there is no need to order the items in transactions
//        if (orderedItemList == null) {
//            orderedItemList = new ArrayList<>();
//            isUnordered = false;
//        }
//        boolean isUnordered = true;
//        if (item_orderMap == null) {
//            isUnordered = false;
//            item_orderMap = new HashMap<>();
//            for (int i = 0; i < orderedItemList.size(); ++i)
//                item_orderMap.put(orderedItemList.get(i).getKey(), i);
//        }

        FpTreeNode root = new FpTreeNode();
        for (Transaction transaction: transactions) {
            if (isUnordered)
                // get item order from item_orderMap and then, for that a larger one is at front
                // so, it's -(a.order() - b.order()) = b.order() - a.order()
                // a.order() == item_orderMap.get(a)
                // but in order to cope with cpb mode which is ascending in count, it should be
                // a.order() - b.order()
                transaction.getItemContent().sort(
                        (a, b) -> (item_orderMap.get(a) - item_orderMap.get(b))
                );

            putTransactionInTree(root, transaction, transaction.getItemContent().size(),
                    orderedItemList, item_orderMap);
        }

        return root;
    }

    private static void putTransactionInTree(
            FpTreeNode node,
            Transaction transaction,
            int count,
            List<FpListItem> orderedItemList,
            Map<Integer,Integer> item_orderMap // use to date back the orderedItemList, always corresponding to orderedItemList
    ) {
        if (count < 0)
            return;

        FpTreeNode nextNode = node.findChildNode(transaction.getContentAt(count));
        // if not exist initialize the new node
        if (nextNode == null) {
            // create a node with parentNode,
            // it will add itself to the childNode list of this parent node
            nextNode = new FpTreeNode(transaction.getContentAt(count), node);
            FpListItem item = orderedItemList.get(item_orderMap.get(nextNode.getItem_Num()));
            item.addNext(nextNode);
        }

        // add total count
        orderedItemList.get(
                item_orderMap.get(transaction.getContentAt(count))
        ).addCount(transaction.getCount());

        nextNode.addCount(transaction.getCount());
        putTransactionInTree(nextNode, transaction, count - 1, orderedItemList, item_orderMap);
    }

}
