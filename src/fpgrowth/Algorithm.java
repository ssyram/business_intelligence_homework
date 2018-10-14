package fpgrowth;

import fpgrowth.result.AssociationRule;
import fpgrowth.util.FpListItem;
import fpgrowth.util.FpTreeNode;
import fpgrowth.util.Transaction;
import javafx.util.Pair;

import java.util.*;

public class Algorithm {

    /**
     * @param orderedItemList a list of ordered frequent items
     * @param transactions all transactions
     * @return a set of association rules which will contain all information that is interested
     */
    public static Set<AssociationRule> calculateRules(
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
    public static Set<AssociationRule> calculateRules(
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

        Set<AssociationRule> r = new HashSet<>();

        Pair<FpTreeNode, List<FpListItem>> treeInfoPair = createTree(orderedItemList,
                transactions, item_orderMap);

        mining(r, treeInfoPair);

        return r;
    }

    private static Pair<FpTreeNode,List<FpListItem>> createTree(
            List<FpListItem> orderedItemList,
            List<Transaction> transactions,
            Map<Integer,Integer> item_orderMap
    ) {
        boolean isUnordered = true; //it means there is no need to order the items in transactions
        if (orderedItemList == null) {
            orderedItemList = new ArrayList<>();
            isUnordered = false;
        }

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
                    orderedItemList, isUnordered);
        }

        return new Pair<>(root, orderedItemList);
    }

    @unfinished
    private static void putTransactionInTree(
            FpTreeNode node,
            Transaction transaction,
            int count,
            List<FpListItem> orderedItemList,
            boolean isNormal
    ) {
    }

    @unfinished
    private static void mining(
            Set<AssociationRule> r,
            Pair<FpTreeNode,List<FpListItem>> treeInfoPair
    ) {
    }


}
