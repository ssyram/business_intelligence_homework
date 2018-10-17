package fpalgorithm.fsetgenerator.apriori;

import fpalgorithm.fsetgenerator.apriori.util.IQuerySupportive;
import fpalgorithm.fsetgenerator.apriori.util.UtilFuncs;
import fpalgorithm.fsetgenerator.util.CombinativelyIterableSet;
import fpalgorithm.fsetgenerator.util.result.FrequentSet;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
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
        for (; true; ) {
            FrequentSet[] at = temp.toArray(new FrequentSet[0]); // array temp
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
        Pair<Set<Integer>, Set<Integer>> joinInfo = UtilFuncs.joinIfJoinable(sOne.getSet(), sTwo.getSet());
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
        // if it's lower than threshold, it returns -1
        // or it returns the actual support number of r
        int support = query.quire(r, support_threshold);
        if (support > 0)
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

        CombinativelyIterableSet<Integer> cis = new CombinativelyIterableSet<>(wfcSet);
        for (Set<Integer> s: cis) {
            if (s.size() == cis.size())
                break;
            s.addAll(distinctSet);
            if (trimAccordance.find(s) == null) return null;
        }

        wfcSet.addAll(distinctSet);

        return wfcSet;
    }
}
