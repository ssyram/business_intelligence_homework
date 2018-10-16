package fpalgorithm.util.result;

import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FrequentSetContainer implements Iterable<FrequentSet> {
    private HashMap<Long, Set<FrequentSet>> v = new HashMap<>();

    public void add(@Nullable FrequentSet set) {
        if (set == null)
            return;
        long l = 0;
        for (int i: set.getSet())
            l += i;

        // have the same meaning of:
//        if (v.get(l) == null)
//            v.put(l, new HashSet<>());
        v.computeIfAbsent(l, k -> new HashSet<>());

        v.get(l).add(set);
    }

    public void addAll(@Nullable Set<FrequentSet> set) {
        if (set == null)
            return;
        for (FrequentSet s: set)
            add(s);
    }

    public FrequentSet find(Set<Integer> itemSet) {
        long l = 0;
        for (int i: itemSet)
            l += i;

        Set<FrequentSet> set = v.get(l);
        FrequentSet target = null;
        if (set == null)
            return null;
        first: for (FrequentSet ns: set) {
            if (ns.getSet().size() != itemSet.size())
                continue;

            for (Integer i: ns.getSet())
                if (!itemSet.contains(i))
                    continue first;

            target = ns;
        }

        if (target == null)
            return null;

        return target;
    }

    @Override
    public Iterator<FrequentSet> iterator() {
        return new Iterator<FrequentSet>() {
            Set<Long> keys = v.keySet();
            Iterator<Long> key_it = keys.iterator();
            Iterator<FrequentSet> set_it = v.get(key_it.next()).iterator();

            @Override
            public boolean hasNext() {
                return (v.size() != 0) && (key_it.hasNext() || set_it.hasNext());
            }

            @Override
            public FrequentSet next() {
                if (!set_it.hasNext())
                    set_it = v.get(key_it.next()).iterator();
                return set_it.next();
            }
        };
    }
}
