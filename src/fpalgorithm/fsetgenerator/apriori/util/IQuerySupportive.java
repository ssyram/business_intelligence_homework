package fpalgorithm.fsetgenerator.apriori.util;

import java.util.Set;

@FunctionalInterface
public interface IQuerySupportive {

    /**
     * @return if lower than threshold, return -1
     */
    int quire(Set<Integer> condition, int threshold);
}
