package database;

import fpgrowth.util.FpListItem;
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

    public static ArrayList<FpListItem> getFpItemCount() {
        ArrayList<FpListItem> r = new ArrayList<>();

        try {
            ResultSet set = execute(GET_ITEM_COUNT_SQL);

            while (set.next())
                r.add(new FpListItem(set.getInt(1), set.getInt(2)));

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

//        r.sort(Comparator.comparing(FpListItem::getKey));
//        Collections.sort(r, (a, b) -> (a.getKey() - b.getKey()));

        return r;
    }

}
