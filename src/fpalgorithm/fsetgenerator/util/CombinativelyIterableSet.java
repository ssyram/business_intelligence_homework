package fpalgorithm.fsetgenerator.util;

import com.sun.istack.internal.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CombinativelyIterableSet<T> implements Iterable<Set<T>> {
    private final Set<T> v;

    public CombinativelyIterableSet(@NotNull Set<T> set) {
        v = set;
    }

    public int size() {
        return v.size();
    }

    @Override
    public Iterator<Set<T>> iterator() {
        return new Iterator<Set<T>>() {
            Object[] va = v.toArray();
            long pattern = -1; // this means it will firstly return an empty set
            long max = (1 << va.length) - 1;
            @Override
            public boolean hasNext() {
                return pattern != max;
            }

            @Override
            public Set<T> next() {
                if (va.length > 31)
                    throw new RuntimeException("out of memory.");
                ++pattern;
                Set<T> s = new HashSet<>();
                for (int j = 0; j < va.length; ++j) {
                    long k = (1 << j);
                    if (j > pattern) break;
                    if ((pattern & k) != 0) s.add((T) va[j]);
                }
                return s;
            }
        };
    }
}
