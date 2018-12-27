package infrastructure;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.ClientService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/mainScreen.fxml"));
        primaryStage.setTitle("CarServiceManager");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        ConnectionManager.connect("SEBA", "seba");
        ResultSet rs = ConnectionManager.getStatementResultSet("SELECT * FROM ALL_OBJECTS WHERE OBJECT_NAME IN ('WSTAW_KLIENTA')");
        while(rs.next()){
            System.out.println(rs.getString(2));
        }
        ClientService.addKlient(true, "Adam", "Nowak", "Polna 12");
//        ConnectionManager.closeAndCommit();
//        launch(args);
    }

}
