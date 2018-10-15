package fpgrowth;

import database.DatabaseOperator;
import fpgrowth.util.FpListItem;
import fpgrowth.util.FrequentSetContainer;
import fpgrowth.util.Transaction;
import javafx.util.Pair;
import util.result.FrequentSet;

import java.util.*;

public class ProjectAdaptor {
    FrequentSetContainer runFpGrowth() {
        Pair<ArrayList<FpListItem>, HashMap<Integer, Integer>> database_info = DatabaseOperator.getFpItemCount();
        List<FpListItem> orderedItemSet = database_info.getKey();
        Map<Integer, Integer> item_orderMap = database_info.getValue();

        List<Transaction> transactions = DatabaseOperator.getTransactions();

        return Algorithm.calculateRules(orderedItemSet, transactions, item_orderMap);
    }
}
