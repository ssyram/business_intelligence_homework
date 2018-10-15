package util.adaptor;

import database.DatabaseOperator;
import frequentpatternalgorithm.fpgrowth.Algorithm;
import frequentpatternalgorithm.fpgrowth.util.FpListItem;
import frequentpatternalgorithm.util.result.FrequentSetContainer;
import frequentpatternalgorithm.fpgrowth.util.Transaction;
import javafx.util.Pair;
import util.GlobalInfo;

import java.util.*;

public class FpGrowthProjectAdaptor {
    FrequentSetContainer runFpGrowth() {
        Pair<ArrayList<FpListItem>, HashMap<Integer, Integer>> database_info = DatabaseOperator.getFpItemCount();
        List<FpListItem> orderedItemSet = database_info.getKey();
        Map<Integer, Integer> item_orderMap = database_info.getValue();

        List<Transaction> transactions = DatabaseOperator.getTransactions();

        return Algorithm.calculateRules(orderedItemSet, transactions, GlobalInfo.total_support,
                item_orderMap);
    }
}
