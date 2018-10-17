package util.adaptor.apriori;

import database.DatabaseOperator;
import fpalgorithm.fsetgenerator.apriori.Algorithm;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import util.GlobalInfo;

import java.util.Map;
import java.util.Set;

public class AprioriProjectAdaptor {
    public static FrequentSetContainer runApriori() {
        Map<Integer, Set<Integer>> ftMap =
                DatabaseOperator.getFrequentItem_TransactionMap();

        Map<Integer, Integer> frequentMap =
                DatabaseOperator.getItemCountMap();

        AprioriQuery query = new AprioriQuery(ftMap);

        return Algorithm.calculateFrequentSet(frequentMap, query, GlobalInfo.total_support);
    }
}
