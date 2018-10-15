package fpalgorithm.util.result;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FrequentSet implements Iterable<Pair<Set<Integer>, Set<Integer>>> {
    private final Set<Integer> set;
    private final int support_count;

    public FrequentSet(Set<Integer> set, int support_count) {
        this.set = set;
        this.support_count = support_count;
    }

    Set<Integer> getSet() {
        return set;
    }

    public double getSupport_count() {
        return support_count;
    }

    /**
     * @implSpec
     * by observing, a two-division of a set is symmetric -- let
     * the elements in the whole set be an 0-1 array, each position represents a single element
     * so, a specific choosing of division means which position to be 1 (selected in left set),
     * or 0 (selected in right set), so, each pattern can be represented by a single binary number
     * at the same time, the two binary number whose addition (| operation) is all one
     * is effectively equivalent because left and right is effectively equivalent,
     * and, in the sense of number, it means when the two numbers' addition is
     * 2^(element_amount) - 1, they are effectively equivalent, which means a specific number's
     * effectively equivalent number is 2^(element_amount) - 1, so
     * when the number is less than (1 << (element_amount - 1)), it can get all of the distinct
     * division pattern without repetition.
     *
     * @return a kind of left-right division of this specific FrequentSet's set.
     * in the whole process of iterate, it will return all of the effective two-division
     * without repetition which here includes swap
     */
    @Override
    public Iterator<Pair<Set<Integer>, Set<Integer>>> iterator() {
        if (set.size() > 31)
            throw new RuntimeException("running out of memory.");
        return new Iterator<Pair<Set<Integer>, Set<Integer>>>() {
            /**
             * means contemporary set type
             */
            long type = 0;
            long max = (1 << (set.size() - 1));
            Integer[] s = (Integer[]) set.toArray();

            @Override
            public boolean hasNext() {
                return type == max;
            }

            @Override
            public Pair<Set<Integer>, Set<Integer>> next() {
                Set<Integer> small = new HashSet<>();
                Set<Integer> big = new HashSet<>();
                ++type;
                for (int i = 0; i < set.size() - 1; ++i)
                    if ((type ^ (1 << i)) != 0)
                        small.add(s[i]);
                    else
                        big.add(s[i]);

                return new Pair<>(small, big);
            }
        };
    }
}
