package util;

import database.DatabaseOperator;

public class GlobalInfo {

    static {
        DatabaseOperator.loadGlobalInfo(2.0/7, 0.5);
    }

    public static double Supportive;
    public static int total_support;
    public static int record_amount;
    public static int item_type_amount;
    public static double confidence_threshold;
}
