package fpalgorithm.apriori;

import fpalgorithm.apriori.util.IQuerySupportive;
import fpalgorithm.util.CombinativelyIterableSet;
import fpalgorithm.util.result.FrequentSet;
import fpalgorithm.util.result.FrequentSetContainer;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Algorithm {
    public static FrequentSetContainer calculateFrequentSet(
            Map<Integer, Integer> item_countSet,
            IQuerySupportive query,
            int support_threshold
    ) {
        FrequentSetContainer r = new FrequentSetContainer();

        Set<FrequentSet> temp = new HashSet<>();

        for (Integer key: item_countSet.keySet()) {
            Set<Integer> s = new HashSet<>();
            s.add(key);
            FrequentSet fs = new FrequentSet(s, item_countSet.get(key));
            temp.add(fs);
        }

        // core, to joinIfJoinable if temp is joinable
        for (int round = 2; true; ++round) {
            FrequentSet[] at = (FrequentSet[]) temp.toArray(); // array temp
            r.addAll(temp);
            temp = new HashSet<>();
            for (int i = 0; i < at.length; ++i)
                for (int j = i + 1; j < at.length; ++j) {
                    FrequentSet s = joinTrimAndValidate(at[i], at[j], r, query, support_threshold);
                    if (s != null)
                        temp.add(s);
                }

            if (temp.size() == 0)
                break;
        }

        return r;
    }

    private static FrequentSet joinTrimAndValidate(
            FrequentSet sOne,
            FrequentSet sTwo,
            FrequentSetContainer trimAccordance,
            IQuerySupportive query,
            int support_threshold
    ) {
        Set<Integer> r;
        Pair<Set<Integer>, Set<Integer>> joinInfo = joinIfJoinable(sOne, sTwo);
        if (joinInfo == null)
            return null;

        if ((r = trimCheck(joinInfo, trimAccordance)) == null)
            return null;

        return supportivelyValidate(r, query, support_threshold);
    }

    private static FrequentSet supportivelyValidate(
            Set<Integer> r,
            IQuerySupportive query,
            int support_threshold
    ) {
        int support = query.quire(r);
        if (support > support_threshold)
            return new FrequentSet(r, support);

        return null;
    }

    private static Set<Integer> trimCheck(
            Pair<Set<Integer>, Set<Integer>> joinInfo,
            FrequentSetContainer trimAccordance
    ) {
        // wfcSet: wait for combine set
        Set<Integer> distinctSet = joinInfo.getKey(), wfcSet = joinInfo.getValue();
        if (wfcSet == null)
            return distinctSet;

        if (trimAccordance.find(distinctSet) == null) return null;

        CombinativelyIterableSet<Integer> cis = new CombinativelyIterableSet<>(wfcSet);
        for (Set<Integer> s: cis) {
            s.addAll(distinctSet);
            if (trimAccordance.find(s) == null) return null;
        }

        wfcSet.addAll(distinctSet);

        return wfcSet;
    }

    /**
     * @return null if not joinable, or a pair of distinct item set and wait for combine item set
     * pay attention that if there's no combine item, the second will be null
     */
    private static Pair<Set<Integer>, Set<Integer>> joinIfJoinable(
            FrequentSet sOne,
            FrequentSet sTwo
    ) {
        if (sOne.equals(sTwo))
            throw new RuntimeException("sOne cannot equals to sTwo");
        if (sOne.getSet().size() != sTwo.getSet().size())
            return null;

        Integer d = null;
        Set<Integer> waitForCombineItemSet = sOne.getSet().size() == 1 ? null : new HashSet<>();
        for (Integer i: sOne.getSet())
            if (sTwo.getSet().contains(i)) waitForCombineItemSet.add(i);
            else if (d == null) d = i;
            else return null;

        // as frequent set can't be equals, there is no need to check if d == null here.

        Set<Integer> distinctItemSet = new HashSet<>();
        distinctItemSet.add(d);

        for (Integer i: sTwo.getSet())
            if (!sOne.getSet().contains(i)) {
                distinctItemSet.add(i);
                break;
            }

        return new Pair<>(distinctItemSet, waitForCombineItemSet);
    }
}
