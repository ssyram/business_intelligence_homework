package fpgrowth;

import fpgrowth.result.AssociationRule;
import fpgrowth.util.FpListItem;
import fpgrowth.util.Transaction;

import java.util.*;

public class Algorithm {

    /**
     * @param orderedItemList a list of ordered frequent items
     * @param itemOrderedTransactions all transactions
     * @return a set of association rules which will contain all information that is interested
     */
    public static List<AssociationRule> calculateRules(
            List<FpListItem> orderedItemList,
            List<Transaction> itemOrderedTransactions
    ) {
        return calculateRules(orderedItemList, itemOrderedTransactions, null);
    }

    /**
     *
     * @param orderedItemList a list of ordered frequent items
     * @param itemOrderedTransactions all transactions
     * @param item_orderMap the map of item and corresponding order, which means the order in
     *                      orderedItemList
     * @return a set of association rules which will contain all information that is interested
     */
    public static List<AssociationRule> calculateRules(
            List<FpListItem> orderedItemList,
            List<Transaction> itemOrderedTransactions,
            Map<Integer, Integer> item_orderMap
    ) {
        if (item_orderMap == null) {
            item_orderMap = new HashMap<>();
            for (int i = 0; i < orderedItemList.size(); ++i)
                item_orderMap.put(orderedItemList.get(i).getKey(), i);
        }
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();

        List<AssociationRule> r = new ArrayList<>();

        return r;
    }
}
