package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
            SerwisService.updateSerwis(id.getText(), nazwa.getText(), adres.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void confirmNew() {
        if (validate()) {
            SerwisService.addSerwis(nazwa.getText(), adres.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void deleteRecord() {
        try {
            SerwisService.deleteSerwis(id.getText());
            close();
        } catch (SQLException e) {
            System.out.println("Foreign key constraint exception");
            errorMsg.setVisible(true);
            errorMsg.setText("Błąd - nie można usunąć rekordu, gdyż posiada on powiązania w innych tablicach");
        }
    }

    private boolean validate() {
        errorMsg.setText("Błąd - dane niezgodne z ograniczeniami");
        if (nazwa.getText() != null) {
            if (nazwa.getText().length() > 100 || nazwa.getText().equals(""))
                return false;
        } else
            return false;
        if (adres.getText() != null) {
            if (adres.getText().length() > 100 || adres.getText().equals(""))
                return false;
        } else
            return false;

        return true;
    }

    private void close() {
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

}
