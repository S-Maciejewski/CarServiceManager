package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("CarServiceManager");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        launch(args);
        String user = "SEBA";
        String password = "seba";

        Connection connection = null;
        Properties properties = new Properties();
        properties.put("user", user);
        properties.put("password", password);
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/" + "xe", properties);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
