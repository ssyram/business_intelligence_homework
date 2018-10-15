package database;

import fpalgorithm.fpgrowth.util.FpListItem;
import fpalgorithm.fpgrowth.util.Transaction;
import javafx.util.Pair;
import util.GlobalInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DatabaseOperator {

    static {
        try {
            ResultSet set = execute("select count(item_num) from transactions group by item_num;");
            set.next();
            GlobalInfo.item_type_amount = set.getInt(1);

            set = execute("select count(transaction_num) from transactions group by " +
                    "transaction_num");
            set.next();
            GlobalInfo.record_amount = set.getInt(1);

            GlobalInfo.total_support = (int)(GlobalInfo.record_amount * GlobalInfo.Supportive);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static final String GET_ALL_TRANSACTIONS = "select * from transactions group by transaction_num order by transaction_num, item_num desc;";

    public static List<Transaction> getTransactions() {
        List<Transaction> l = new ArrayList<>();
        try {
            ResultSet set = execute(GET_ALL_TRANSACTIONS);

            List<Integer> li = new ArrayList<>();
            int temp;
            set.next();
            temp = set.getInt(1);
            li.add(set.getInt(2));
            while (set.next()) {
                if (temp != set.getInt(1)) {
                    l.add(new Transaction(li));
                    li = new ArrayList<>();
                    temp = set.getInt(1);
                }
                li.add(set.getInt(2));
            }
            l.add(new Transaction(li));

            set.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }

    private static final String GET_ITEM_COUNT_SQL = "select item_num, count(*) as cnt " +
            "from transactions " +
            "group by item_num " +
            "where cnt > " +
            GlobalInfo.total_support +
            "order by cnt, item_num desc;";

    private static ResultSet execute(String sql) throws SQLException {
        Statement stmt = Connector.getConnection().createStatement();
        ResultSet set = stmt.executeQuery(sql);
        stmt.close();
        return set;
    }

    public static Map<Integer, Integer> getItemCountMap() {
        Map<Integer, Integer> r = new HashMap<>();

        try {
            ResultSet set = execute(GET_ITEM_COUNT_SQL);

            while (set.next()) {
                r.put(set.getInt(1), set.getInt(2));
            }

            set.close();
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

            set.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

//        r.sort(Comparator.comparing(FpListItem::getKey));
//        Collections.sort(r, (a, b) -> (a.getKey() - b.getKey()));

        return new Pair<>(r, rm);
    }

}
