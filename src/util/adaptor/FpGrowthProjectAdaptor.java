package util.adaptor;

import database.DatabaseOperator;
import fpalgorithm.fsetgenerator.fpgrowth.Algorithm;
import fpalgorithm.fsetgenerator.fpgrowth.util.FpListItem;
import fpalgorithm.fsetgenerator.util.result.FrequentSet;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import fpalgorithm.fsetgenerator.fpgrowth.util.Transaction;
import javafx.util.Pair;
import util.GlobalInfo;

import java.util.*;

public class FpGrowthProjectAdaptor {
//    public static FrequentSetContainer runFpGrowth() {
//        return runFpGrowth(GlobalInfo.total_support);
//    }
    public static FrequentSetContainer runFpGrowth() {
        Pair<ArrayList<FpListItem>, HashMap<Integer, Integer>> database_info = DatabaseOperator.getFpItemCount();
        List<FpListItem> orderedItemSet = database_info.getKey();
        Map<Integer, Integer> item_orderMap = database_info.getValue();

        List<Transaction> transactions = DatabaseOperator.getTransactions();

        return Algorithm.calculateFrequentSets(orderedItemSet, transactions, (int) GlobalInfo.total_support,
                item_orderMap);
    }
}
