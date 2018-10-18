package fpalgorithm.fsetgenerator.util.result;

import com.sun.istack.internal.Nullable;

import java.util.*;

public class FrequentSetContainer implements Iterable<FrequentSet> {
    // level one: addition, level two: multiplication
    private HashMap<Long, HashMap<Long, Set<FrequentSet>>> v = new HashMap<>();

    public void add(@Nullable FrequentSet set) {
        if (set == null)
            return;
        long l = 0;
        long m = 1;
        for (int i: set.getSet()) {
            l += i;
            m *= i;
        }

        if (l == (48 + 49 +51) && m == (48 * 49 * 51))
            System.out.println("now");

        // have the same meaning of:
//        if (v.get(l) == null)
//            v.put(l, new HashSet<>());
//        v.computeIfAbsent(l, k -> {
//            HashMap<Long, Set<FrequentSet>> n = new HashMap<>();
//
//            long m = 1;
//            for (int i: set.getSet())
//                m *= i;
//
//            Set<FrequentSet> fss = new HashSet<>();
//            fss.add(set);
//
//            n.put(m, fss);
//
//            return n;
//        });
        v.computeIfAbsent(l, k -> new HashMap<>());

        v.get(l).computeIfAbsent(m, k -> new HashSet<>());

        v.get(l).get(m).add(set);
    }

    public int size() {
        int i = 0;
        for (Long l: v.keySet())
            for (Long ll: v.get(l).keySet())
                i += v.get(l).get(ll).size();

        return i;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (FrequentSet set: this)
            builder.append(set.toString()).append("\n");

        builder.append("size: ").append(size()).append(".");

        return builder.toString();
    }

    public void addAll(@Nullable Set< ? extends FrequentSet> set) {
        if (set == null)
            return;
        for (FrequentSet s: set)
            add(s);
    }

    public FrequentSet find(Set<Integer> itemSet) {
        long l = 0;
        long m = 1;
        for (int i: itemSet) {
            l += i;
            m *= i;
        }

        Set<FrequentSet> set;
        try {
            set = v.get(l).get(m);
        } catch (Exception e) {
            return null;
        }

        if (set == null)
            return null;

//        FrequentSet target = null;
        first: for (FrequentSet ns: set) {
            if (ns.getSet().size() != itemSet.size())
                continue;

            for (Integer i: ns.getSet())
                if (!itemSet.contains(i))
                    continue first;

//            target = ns;

            return ns;
        }

        return null;

//        if (target == null)
//            return null;

//        return target;
    }

    @Override
    public Iterator<FrequentSet> iterator() {
//      HashMap<Long, HashMap<Long, Set<FrequentSet>>>
        return new Iterator<FrequentSet>() {
//            Set<Long> additions = v.keySet();
            Iterator<Long> a_it = v.keySet().iterator();
            long cache = a_it.next();
//            Set<Long> multiplications = v.get(cache).keySet();
            Iterator<Long> m_it = v.get(cache).keySet().iterator();
            Iterator<FrequentSet> set_it = v.get(cache).get(m_it.next()).iterator();

            @Override
            public boolean hasNext() {
                return (v.size() != 0) &&
                        (a_it.hasNext() || m_it.hasNext() || set_it.hasNext());
            }

            @Override
            public FrequentSet next() {
                if (!set_it.hasNext())
                    if (m_it.hasNext())
                        set_it = v.get(cache).get(m_it.next()).iterator();
                    else {
                        cache = a_it.next();
//                        multiplications = v.get(cache).keySet();
                        m_it = v.get(cache).keySet().iterator();
                        set_it = v.get(cache).get(m_it.next()).iterator();
                    }

                return set_it.next();
            }
        };
    }
}
