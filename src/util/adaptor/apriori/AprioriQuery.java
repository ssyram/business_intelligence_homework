package util.adaptor.apriori;

import com.sun.istack.internal.Nullable;
import fpalgorithm.apriori.util.IQuerySupportive;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AprioriQuery implements IQuerySupportive {
    private Map<Integer, Set<Integer>> v;

    public AprioriQuery(Map<Integer, Set<Integer>> item_transactionsMap) {
        v = item_transactionsMap;
    }

    @Override
    public int quire(@Nullable Set<Integer> condition) {
        if (condition == null || condition.size() == 0)
            return 0;

        Set<Integer> rs;
        Iterator<Integer> it = condition.iterator();
        if ((rs = v.get(it.next())) == null)
            throw new RuntimeException("not a valid search item: it's not frequent");

        while (it.hasNext()) {
            Set<Integer> s = v.get(it.next());
            for (Iterator<Integer> i = rs.iterator(); i.hasNext(); )
                if (!s.contains(i.next())) i.remove();

            if (rs.size() == 0) return 0;
        }

        return rs.size();
    }
}
