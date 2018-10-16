package fpalgorithm.apriori.enhance.util;

import com.sun.istack.internal.NotNull;
import fpalgorithm.apriori.util.UtilFuncs;
import fpalgorithm.fpgrowth.util.Transaction;
import fpalgorithm.util.result.FrequentSet;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EnhancedFrequentSet extends FrequentSet {
    private Set<Integer> transactionSet;

    public EnhancedFrequentSet(@NotNull Set<Integer> itemSet, @NotNull Set<Integer> transactionSet) {
        super(itemSet, transactionSet.size());
        this.transactionSet = transactionSet;
    }

    public static EnhancedFrequentSet join(
            @NotNull EnhancedFrequentSet sOne,
            @NotNull EnhancedFrequentSet sTwo
    ) {
        if (sOne.getSet().size() != sTwo.getSet().size() || sOne.getSet().size() == 0)
            return null;

        Pair<Set<Integer>, Set<Integer>> info = UtilFuncs.joinIfJoinable(sOne.getSet(), sTwo.getSet());

        if (info == null) return null;

        Set<Integer> ts = new HashSet<>(sOne.transactionSet);

        for (Iterator<Integer> it = ts.iterator(); it.hasNext(); )
            if (!sTwo.getSet().contains(it.next())) it.remove();

        if (ts.size() == 0) return null;

        info.getKey().addAll(info.getValue());
        return new EnhancedFrequentSet(info.getKey(), ts);
    }
}
