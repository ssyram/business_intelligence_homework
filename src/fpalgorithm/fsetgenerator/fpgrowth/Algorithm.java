package fpalgorithm.fsetgenerator.fpgrowth;

import fpalgorithm.fsetgenerator.util.CombinativelyIterableSet;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import fpalgorithm.fsetgenerator.util.result.FrequentSet;
import fpalgorithm.fsetgenerator.fpgrowth.util.FpListItem;
import fpalgorithm.fsetgenerator.fpgrowth.util.FpTreeNode;
import fpalgorithm.fsetgenerator.fpgrowth.util.Transaction;
import javafx.util.Pair;

import java.util.*;

public class Algorithm {

    /**
     * @param orderedItemList a list of ordered frequent items
     * @param transactions all transactions
     * @return a set of association rules which will contain all information that is interested
     */
    public static FrequentSetContainer calculateFrequentSets(
            List<FpListItem> orderedItemList,
            List<Transaction> transactions,
            int support_threshold
    ) {
        return calculateFrequentSets(orderedItemList, transactions, support_threshold, null);
    }

    /**
     *
     * @param orderedItemList a list of ordered frequent items, must be sorted in descending order
     * @param transactions all transactions
     * @param item_orderMap the map of item and corresponding order, which means the order in
     *                      orderedItemList, if null, it will be created automatically
     * @return a set of association rules which will contain all information that is interested
     */
    public static FrequentSetContainer calculateFrequentSets(
            List<FpListItem> orderedItemList,
            List<Transaction> transactions,
            int support_threshold,
            Map<Integer, Integer> item_orderMap
    ) {
        if (item_orderMap == null) {
            item_orderMap = new HashMap<>();
            for (int i = 0; i < orderedItemList.size(); ++i)
                item_orderMap.put(orderedItemList.get(i).getKey(), i);

            for (Transaction t: transactions)
                for (Integer i: t.getItemContent())
                    orderedItemList.get(item_orderMap.get(i)).addCount();

            orderedItemList.sort(Comparator.comparing(FpListItem::getCount));
        }
        FrequentSetContainer r = new FrequentSetContainer();

        // to be prepared, in order that it's not completed
        trimTransactions(transactions, item_orderMap);

        createTree(
                orderedItemList,
                transactions,
                item_orderMap
        );

        mining(r, orderedItemList, item_orderMap, new HashSet<>(), -1, support_threshold);

        return r;
    }

    private static void trimTransactions(List<Transaction> transactions, Map<Integer, Integer> item_orderMap) {
        for (Transaction t: transactions)
            for (Iterator<Integer> i = t.getItemContent().iterator(); i.hasNext(); )
                if (item_orderMap.get(i.next()) == null)
                    i.remove();
    }

    private static void mining(
            FrequentSetContainer r,
            List<FpListItem> orderedItemList,
            Map<Integer, Integer> item_orderMap,
            Set<Integer> postFixKeySet,
            int support_count,
            int support_threshold
    ) {
        // when it's the last element, add itself and return
        if (orderedItemList.isEmpty() && support_count >= support_threshold) {
            Set<Integer> s = new HashSet<>(postFixKeySet);
//            if (postFixKeySet.size() != 1)
//                throw new RuntimeException("the post fix key set of the last item should be have only one element.");
            r.add(new FrequentSet(s, support_count));
            return;
        }
        FpTreeNode root = orderedItemList.get(0).getFirst().getParentNode();
//        if (!root.isRoot())
//            throw new RuntimeException("not a valid orderedItemList");
        trimTree(orderedItemList, support_threshold, item_orderMap);
        if (isSingleBranch(root)) {
            Set<Integer> waitForCombineSet = new HashSet<>();
            for (FpTreeNode node = root.getOnlyChildNode(); node != null; node = node.getOnlyChildNode())
                waitForCombineSet.add(node.getItem_Num());

            combineElementsToFrequentSet(r, waitForCombineSet, postFixKeySet, support_count, orderedItemList, item_orderMap);
        }
        else
            for (int i = orderedItemList.size() - 1; i >= 0; --i) {
                if (postFixKeySet.size() != 0)
                    r.add(new FrequentSet(new HashSet<>(postFixKeySet), support_count));
                FpListItem item = orderedItemList.get(i);
                // get cpb and corresponding itemList, the itemList is already sorted
                Pair<List<Transaction>, List<FpListItem>> analyzedData = analyzePath(item);
                List<Transaction> transactions = analyzedData.getKey();
                // turn itemSet to itemList
                List<FpListItem> itemList = analyzedData.getValue();
//                // get itemList ordered
//                itemList.sort(
//                        (a, b) -> (item_orderMap.get(a.getKey()) - item_orderMap.get(b.getKey()))
//                );

                Map<Integer, Integer> nioMap = new HashMap<>();
                for (int j = 0; j < analyzedData.getValue().size(); ++j)
                    nioMap.put(itemList.get(j).getKey(), j);

                createTree(itemList, transactions, nioMap);
                postFixKeySet.add(item.getKey());
                // support_count < 0 means that it's first mining, pass item.getCount()
                // or, pass the less one
                mining(
                        r,
                        itemList,
                        nioMap,
                        postFixKeySet,
                        (support_count < 0 || support_count > item.getCount()) ? item.getCount()
                                : support_count,
                        support_threshold
                );
                postFixKeySet.remove(item.getKey());
            }
    }

    /**
     * @implNote use binary number to guide the generation of frequent set
     * @param r target set of element of type frequent set
     * @param waitForCombineSet the set of elements wait for combine
     * @param postFixKeySet the set of post fix elements
     * @param support_count the count for each of the set
     */
    private static void combineElementsToFrequentSet(
            FrequentSetContainer r,
            Set<Integer> waitForCombineSet,
            Set<Integer> postFixKeySet,
            int support_count,
            List<FpListItem> orderedItemList,
            Map<Integer, Integer> item_orderMap
    ) {
        // if larger than 31, it will not be able to access an array
        // and if it's calculated, it means the total amount will reach 2G
        // average set larger than 8B(2 int, which can't be easier to reach)
        // it will exceed the memory of all of my computer contemporarily
//        if (waitForCombineSet.size() > 31)
//            throw new RuntimeException("out of memory: the frequent set is too large.");
//        Integer[] dset = (Integer[]) waitForCombineSet.toArray();
//        for (int i = 1; i < (1 << dset.length); ++i) {
//            Set<Integer> ss = new HashSet<>();
//            for (int j = 0; j < dset.length; ++j) {
//                if (i < (1 << j)) break;
//                if ((i & (1 << j)) != 0)
//                    ss.add(dset[j]);
//            }
//
//            ss.addAll(postFixKeySet);
//            if (support_count < 0)
//                r.add(new FrequentSet(ss, minCountOfSS(ss, orderedItemList, item_orderMap)));
//            else
//                r.add(new FrequentSet(ss, support_count));
//        }
        CombinativelyIterableSet<Integer> cis = new CombinativelyIterableSet<>(waitForCombineSet);
        for (Set<Integer> s: cis) {
            int minCount = minCountOfSS(s, orderedItemList, item_orderMap);
            s.addAll(postFixKeySet);
            if (support_count < 0) {
                if (!s.isEmpty())
                    r.add(new FrequentSet(s, minCount));
            }
            else
                r.add(new FrequentSet(s, minCount > support_count ? support_count : minCount));
        }
    }

    /**
     * @return if ss is empty, return infinite -- maximum int
     */
    private static int minCountOfSS(
            Set<Integer> ss,
            List<FpListItem> orderedItemList,
            Map<Integer, Integer> item_orderMap
    ) {
        if (ss.size() == 0)
            return (int) (((long)1 << 31) - 1);
        boolean fixMin = false;
        int min = 0;
        for (int i: ss) {
            if (!fixMin) {
                min = i;
                fixMin = true;
                continue;
            }

            if (item_orderMap.get(i) > item_orderMap.get(min))
                min = i;
        }

        return orderedItemList.get(item_orderMap.get(min)).getCount();
    }

    private static boolean isSingleBranch(FpTreeNode node) {
        while (true) {
            if (node.getChildNodeCount() > 1) return false;
            if (node.getChildNodeCount() == 0) return true;
            node = node.getOnlyChildNode();
        }
    }

    private static void trimTree(List<FpListItem> orderedItemList, int support_threshold, Map<Integer, Integer> item_orderMap) {
        for (int i = orderedItemList.size() - 1; i >= 0; --i) {
            FpListItem item = orderedItemList.get(i);
            if (item.getCount() >= support_threshold)
                return;

            for (FpTreeNode node = item.getFirst(); node != null; node = node.getNext())
                node.eliminateFromTree();
            orderedItemList.remove(i);
            item_orderMap.remove(item.getKey());
        }
    }

    /**
     * to get the transactions needed
     * @param item
     * @return
     */
    private static Pair<List<Transaction>, List<FpListItem>> analyzePath(FpListItem item) {
        // the exact position in the new list
        Map<Integer, Integer> checkRs = new HashMap<>(); // use for checking if things should add to itemList
        int pos = 0;
        List<Transaction> rt = new ArrayList<>(); //
        List<FpListItem> itemList = new ArrayList<>(); // the new itemList
        for (FpTreeNode node = item.getFirst(); node != null; node = node.getNext()) {
            List<Integer> cpb = new ArrayList<>();
            for (FpTreeNode nnode = node.getParentNode(); !nnode.isRoot(); nnode = nnode.getParentNode()) {
                int item_num = nnode.getItem_Num();
                if (checkRs.get(item_num) == null) {
                    itemList.add(new FpListItem(item_num));
                    checkRs.put(item_num, pos++);
                }

                cpb.add(item_num);
                // because on a path, there is no such possibility of repeating a node
                itemList.get(checkRs.get(item_num)).addCount(node.getCount());
            }
            rt.add(new Transaction(cpb, node.getCount()));
        }

        itemList.sort((a, b) -> (b.getCount() - a.getCount()));

        return new Pair<>(rt, itemList);
    }

    // for that variables use in lambda should be final or effectively final
    // we can't create the corresponding item_orderMap in cpb mode
    // //     if item_orderMap is null, that means it's in cpb mode
    // //     which means that it is ordered
    private static void createTree(
            List<FpListItem> orderedItemList,
            List<Transaction> transactions,
            Map<Integer,Integer> item_orderMap
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
//            if (isUnordered)
// //                 get item order from item_orderMap and then, for that a larger one is at front
// //                 so, it's -(a.order() - b.order()) = b.order() - a.order()
// //                 a.order() == item_orderMap.get(a)
                // to cope with cpb mode which is descending in order, it should be
                // b.order() - a.order()
            transaction.getItemContent().sort(
                    (a, b) -> (item_orderMap.get(b) - item_orderMap.get(a))
            );

            putTransactionInTree(root, transaction, transaction.getItemContent().size() - 1,
                    orderedItemList, item_orderMap);
        }
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

//        // add total count
//        orderedItemList.get(
//                item_orderMap.get(nextNode.getItem_Num())
//        ).addCount(transaction.getCount());

        nextNode.addCount(transaction.getCount());

        putTransactionInTree(nextNode, transaction, count - 1, orderedItemList, item_orderMap);
    }

}
