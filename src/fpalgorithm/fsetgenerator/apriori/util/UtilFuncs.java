package fpalgorithm.fsetgenerator.apriori.util;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class UtilFuncs {

    /**
     * @return null if not joinable, or a pair of distinct item set and wait for combine item set
     * pay attention that if there's no combine item, the second will be null
     */
    public static <T> Pair<Set<T>, Set<T>> joinIfJoinable(
            Set<T> sOne,
            Set<T> sTwo
    ) {
        if (sOne.equals(sTwo))
            throw new RuntimeException("sOne cannot equals to sTwo");
        if (sOne.size() != sTwo.size())
            return null;

        T d = null;
        Set<T> waitForCombineItemSet = sOne.size() == 1 ? null : new HashSet<>();
        for (T i: sOne)
            if (sTwo.contains(i)) waitForCombineItemSet.add(i);
            else if (d == null) d = i;
            else return null;

        // as frequent set can't be equals, there is no need to check if d == null here.

        Set<T> distinctItemSet = new HashSet<>();
        distinctItemSet.add(d);

        for (T i: sTwo)
            if (!sOne.contains(i)) {
                distinctItemSet.add(i);
                break;
            }

        return new Pair<>(distinctItemSet, waitForCombineItemSet);
    }
}
