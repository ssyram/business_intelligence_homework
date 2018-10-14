package database;

import fpgrowth.util.FpListItem;
import javafx.util.Pair;
import util.GlobalInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DatabaseOperator {

    private static final String GET_ITEM_COUNT_SQL = "select item_num, count(*) as cnt " +
            "from transactions " +
            "group by item_num " +
            "where cnt > " +
            GlobalInfo.Supportive + " * " +
            "(select count(transaction_num) from transactions)" +
            "order by cnt, item_num desc;";

    private static ResultSet execute(String sql) throws SQLException {
        Statement stmt = Connector.getConnection().createStatement();
        return stmt.executeQuery(sql);
    }

    public static Map<Integer, Integer> getItemCountMap() {
        Map<Integer, Integer> r = new HashMap<>();

        try {
            ResultSet set = execute(GET_ITEM_COUNT_SQL);

            while (set.next()) {
                r.put(set.getInt(1), set.getInt(2));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return r;
    }

    /**
     * HashMap<Integer, Integer> to record the exact order of a given item
     * when constructing the tree, every item in a transaction needs to be set in order
     * so that, by using this hash map, it can use a util to accelerate the compare process.
     */
    public static Pair<ArrayList<FpListItem>, HashMap<Integer, Integer>> getFpItemCount() {
        ArrayList<FpListItem> r = new ArrayList<>();
        HashMap<Integer, Integer> rm = new HashMap<>();
        int i = 0;

        try {
            ResultSet set = execute(GET_ITEM_COUNT_SQL);

            while (set.next()) {
                r.add(new FpListItem(set.getInt(1)));
                rm.put(set.getInt(1), i++);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

//        r.sort(Comparator.comparing(FpListItem::getKey));
//        Collections.sort(r, (a, b) -> (a.getKey() - b.getKey()));

        return new Pair<>(r, rm);
    }

}
