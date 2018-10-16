package fpalgorithm.apriori.enhance;

import fpalgorithm.apriori.enhance.util.EnhancedFrequentSet;
import fpalgorithm.util.result.FrequentSetContainer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Algorithm {
    public static FrequentSetContainer calculateFrequentSet(
            Map<Integer, Set<Integer>> item_transactionsMap,
            int support_threshold
    ) {
        FrequentSetContainer r = new FrequentSetContainer();
        EnhancedFrequentSet.support_threshold = support_threshold;

        Set<EnhancedFrequentSet> temp = new HashSet<>();

        for (Integer i: item_transactionsMap.keySet())
            temp.add(
                    new EnhancedFrequentSet(
                            i, item_transactionsMap.get(i)
                    )
            );

        for ( ; ; ) {
            EnhancedFrequentSet[] at = (EnhancedFrequentSet[]) temp.toArray();
            r.addAll(temp);

            temp = new HashSet<>();
            for (int i = 0; i < at.length; ++i)
                for (int j = i + 1; j < at.length; ++j) {
                    EnhancedFrequentSet set = EnhancedFrequentSet.join(at[i], at[j]);
                    if (set != null)
                        temp.add(set);
                }

            if (temp.size() == 0)
                break;
        }

        return r;
    }

}
