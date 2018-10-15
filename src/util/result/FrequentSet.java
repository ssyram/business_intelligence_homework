package util.result;

import java.util.Set;

public class FrequentSet {
    private final Set<Integer> set;
    private final int support_count;

    public FrequentSet(Set<Integer> set, int support_count) {
        this.set = set;
        this.support_count = support_count;
    }

    public Set<Integer> getSet() {
        return set;
    }

    public int getSupport_count() {
        return support_count;
    }
}
