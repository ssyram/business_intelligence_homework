package database;

import java.io.*;

class ConnectInfo {

    static String driver = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/business_intelligence_database";
    static String username = "root"; // if necessary, edit here too
    static String password = ""; //edit here

    static {
        File file = new File("src/database/password");

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            password = reader.readLine();
            System.out.println(password);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
