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

            closeExecute(set);
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

            closeExecute(set);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }

    private static final String SELECT_FREQUENT_ITEM_TRANSACTION_MAP =
            "select item_num, transaction_num" +
                    "from transactions" +
                    "where item_num in (" +
                        "select item_num" +
                        "from (" +
                            "select item_num, count(*) as cnt" +
                            "from transactions" +
                            "group by item_num" +
                            "having cnt > " + GlobalInfo.total_support +
                        ")" +
                    ")" +
                    "order by item_num desc;";

    /**
     * @return a pair of frequent item set and a map of frequent item and corresponding set of
     * transactions that contains the specific frequent item
     */
    public static Map<Integer, Set<Integer>> getFrequentItem_TransactionMap() {
        Map<Integer, Set<Integer>> item_transactionsMap = new HashMap<>();

        try {
            ResultSet set = execute(SELECT_FREQUENT_ITEM_TRANSACTION_MAP);

            set.next();
            int temp = set.getInt(1);
            item_transactionsMap.put(temp, new HashSet<>());
            item_transactionsMap.get(temp).add(set.getInt(2));

            while (set.next()) {
                if (set.getInt(1) != temp) {
                    temp = set.getInt(1);
                    item_transactionsMap.put(temp, new HashSet<>());
                }

                item_transactionsMap.get(temp).add(set.getInt(2));
            }

            closeExecute(set);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return item_transactionsMap;
    }

    private static void closeExecute(ResultSet set) {
        try {
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeStatement();
    }

    private static final String GET_ITEM_COUNT_SQL = "select item_num, count(*) as cnt " +
            "from transactions " +
            "group by item_num " +
            "having cnt > " + GlobalInfo.total_support +
            "order by cnt, item_num desc;";

    private static Statement executeStatement;

    private static ResultSet execute(String sql) throws SQLException {
        executeStatement = Connector.getConnection().createStatement();

        return  executeStatement.executeQuery(sql);
    }

    private static void closeStatement() {
        try {
            executeStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Statement insertStmt;

    public static void startInsert() {
        try {
            insertStmt = Connector.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void continueInsert(String sql) {
        try {
            insertStmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void endInsert() {
        try {
            insertStmt.close();
            Connector.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Integer> getItemCountMap() {
        Map<Integer, Integer> r = new HashMap<>();

        try {
            ResultSet set = execute(GET_ITEM_COUNT_SQL);

            while (set.next()) {
                r.put(set.getInt(1), set.getInt(2));
            }

            closeExecute(set);
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

            closeExecute(set);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

//        r.sort(Comparator.comparing(FpListItem::getKey));
//        Collections.sort(r, (a, b) -> (a.getKey() - b.getKey()));

        return new Pair<>(r, rm);
    }

}
