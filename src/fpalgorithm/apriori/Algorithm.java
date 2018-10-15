package fpalgorithm.apriori;

import fpalgorithm.util.result.FrequentSet;
import fpalgorithm.util.result.FrequentSetContainer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Algorithm {
    public static FrequentSetContainer calculateFrequentSet(
            Map<Integer, Integer> item_countSet
    ) {
        FrequentSetContainer r = new FrequentSetContainer();

        Set<FrequentSet> temp = new HashSet<>();

        for (Integer key: item_countSet.keySet()) {
            Set<Integer> s = new HashSet<>();
            s.add(key);
            temp.add(new FrequentSet(s, item_countSet.get(key)));
        }

        return r;
    }
}
