package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.PracownikService;
import services.SerwisService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SerwisEditViewController {
    @FXML
    private TextField id, nazwa, adres;
    @FXML
    private Button delete, confirm;
    @FXML
    private Label errorMsg;

    public void setContext(String ID) throws SQLException {
        if (ID != null) {
            ResultSet resultSet = SerwisService.getSerwis(ID);
            resultSet.next();
            id.setText(resultSet.getString(1));
            nazwa.setText(resultSet.getString(2));
            adres.setText(resultSet.getString(3));
        } else {
            delete.setVisible(false);
            confirm.setOnAction((event) -> confirmNew());
        }
    }

    public void confirmChanges() {
        if (validate()) {

            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void confirmNew() {
        if (validate()) {

            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void deleteRecord() {
        SerwisService.deleteSerwis(id.getText());
    }

    private boolean validate() {

        return true;
    }

    private void close() {
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

}
