package database;

import java.sql.Connection;
import java.sql.DriverManager;

class Connector {
    private static Connection con;
    private static Connection connect() {
        try {
            Class.forName(ConnectInfo.driver);
            con = DriverManager.getConnection(ConnectInfo.url, ConnectInfo.username, ConnectInfo.password);
        }
        catch (Exception e) {
            System.out.println("not valid connection");
            e.printStackTrace();
        }

        return con;
    }

    static Connection getConnection() {
        if (con == null)
            con = connect();

        return con;
    }

    static void closeConnection() {
        try {
            con.close();
            con = null;
        }
        catch (Exception e) {
            System.out.println("cannot close connection");
            e.printStackTrace();
        }
    }
}
