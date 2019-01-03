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
            System.out.println("Unable to connect");
        }
    }

    public static void closeAndCommit() {
        try {
//            connection.commit();  //TODO zdecydować czy auto-commit powinien być włączony
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to commit and close connection");
        }
    }

    public static ResultSet getStatementResultSet(String SqlStatement) {
        try {
            return statement.executeQuery(SqlStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Invalid SQL statement");
        }
        return null;
    }

    public static void executeProcedure(String SqlStatement) {
        try {
            CallableStatement callableStatement = connection.prepareCall(SqlStatement);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Invalid SQL statement");
        }
    }

    public static void executeStatement(String SqlStatement) {
        try {
            statement.executeQuery(SqlStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Invalid SQL statement");
        }
    }
}
