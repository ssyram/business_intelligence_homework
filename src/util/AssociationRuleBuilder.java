package util;

import fpalgorithm.util.result.FrequentSetContainer;
import javafx.util.Pair;
import util.result.AssociationRule;
import fpalgorithm.util.result.FrequentSet;

import java.util.HashSet;
import java.util.Set;

public class AssociationRuleBuilder {
    /**
     * @implSpec 
     * the process of division is executed by the iterator of FrequentSet,
     * which returns a pair of left-right division set
     * and it can ensure that: in a single process of
     * iterating a specific FrequentSet whose content must not
     * be modified, it will return all and not repeated (of course, no swap)
     * two-division sets in the whole process
     */
    public static Set<AssociationRule> build(FrequentSetContainer frequentSets) {
        Set<AssociationRule> rules = new HashSet<>();

        for (FrequentSet fs: frequentSets) {
            for (Pair<Set<Integer>, Set<Integer>> left_right_pair: fs) {
                FrequentSet left = frequentSets.find(left_right_pair.getKey());
                FrequentSet right = frequentSets.find(left_right_pair.getValue());

                if (fs.getSupport_count() / left.getSupport_count() > GlobalInfo
                        .confidence_threshold)
                    rules.add(
                            new AssociationRule(
                                    left_right_pair.getKey(),
                                    left_right_pair.getValue(),
                                    fs.getSupport_count() / GlobalInfo.total_support,
                                    fs.getSupport_count() / left.getSupport_count()
                            )
                    );

                if (fs.getSupport_count() / right.getSupport_count() > GlobalInfo
                        .confidence_threshold)
                    rules.add(
                            new AssociationRule(
                                    left_right_pair.getValue(),
                                    left_right_pair.getKey(),
                                    fs.getSupport_count() / GlobalInfo.total_support,
                                    fs.getSupport_count() / right.getSupport_count()
                            )
                    );
            }
        }

        return rules;
    }
}
