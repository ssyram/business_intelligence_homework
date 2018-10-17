package database;

import java.io.*;

class ConnectInfo {

    static String driver = "com.mysql.cj.jdbc.Driver";
    static String url = "";
    static String username = "";
    static String password = "";

    static {
        File file = new File("src/database/database_info");

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            url = "jdbc:" + reader.readLine() + "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
            username = reader.readLine();
            password = reader.readLine();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
