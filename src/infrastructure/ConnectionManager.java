package infrastructure;

import java.sql.*;
import java.util.Properties;


public class ConnectionManager {
    private static Connection connection;
    private static Statement statement;

    public static void connect(String username, String password) {
        System.out.println("Connecting to database...");
        connection = null;
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/" + "xe", properties);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select owner, table_name from all_tables where owner = '" + username + "'");
            System.out.println(username + "'s tables in schema:");
            while (rs.next()) {
                System.out.println(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getStatementResultSet(String SqlStatement) {
        try {
            return (ResultSet) statement.executeQuery(SqlStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Invalid SQL statement");
        }
        return null;
    }


}
