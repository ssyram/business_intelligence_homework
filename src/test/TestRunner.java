package test;

import database.DatabaseOperator;
import util.Runner;

import java.io.*;
import java.sql.SQLException;

public class TestRunner {

    public static void runRulesGenerator(int testSampleNum) {
        
    }

    public static void runApriori(int testSampleNum) {
        try {
            generateSampleTransactions(testSampleNum);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Runner.runApriori(true, false);
    }

    public static void runFpGrowth(int testSampleNum) {
        try {
            generateSampleTransactions(testSampleNum);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Runner.runFpGrowth(true, false);
    }

    private static void generateSampleTransactions(int fileNum) throws IOException {
        File file = new File("samples/_" + fileNum + ".txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s;
        StringBuilder builder = new StringBuilder("insert into transactions values(");
        int bsl = builder.length();

        DatabaseOperator.emptyDatabase();

        DatabaseOperator.startInsert();

        for (int i = 0; (s = reader.readLine()) != null; ++i) {
            String[] ss = s.split(" ");
            builder.append(i).append(", ");

            int sl = builder.length();

            for (String str: ss) {
                int item = str.charAt(0);
                builder.append(item).append(");");
//                System.out.println(builder.toString());
                try {
                    DatabaseOperator.continueInsert(builder.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                builder.delete(sl, builder.length());
            }

            builder.delete(bsl, builder.length());
        }

        reader.close();
        DatabaseOperator.endInsert();
    }
}
