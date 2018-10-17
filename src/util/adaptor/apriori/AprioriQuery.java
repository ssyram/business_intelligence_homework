package util.adaptor.apriori;

import com.sun.istack.internal.Nullable;
import fpalgorithm.apriori.util.IQuerySupportive;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AprioriQuery implements IQuerySupportive {
    private Map<Integer, Set<Integer>> v;

    AprioriQuery(Map<Integer, Set<Integer>> item_transactionsMap) {
        v = item_transactionsMap;
    }

    @Override
    public int quire(@Nullable Set<Integer> condition, int threshold) {
        if (condition == null || condition.size() == 0)
            return 0;

//        Set<Integer> rs_temp;
        // v: item - transactions map
        Iterator<Integer> it = condition.iterator();
//        if ((rs_temp = v.get(it.next())) == null)
//            throw new RuntimeException("not a valid search item: it's not frequent");

        Set<Integer> rs = new HashSet<>(v.get(it.next()));

        while (it.hasNext()) {
            Set<Integer> s = v.get(it.next());
            for (Iterator<Integer> i = rs.iterator(); i.hasNext(); )
                if (!s.contains(i.next())) {
                    i.remove();
                    if (rs.size() < threshold) return -1;
                }

//            if (rs.size() < threshold) return -1;
        }

        return rs.size();
    }
}
