package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.CzescService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CzescEditViewController {
    @FXML
    private TextField id, nazwa, cena;
    @FXML
    private Button delete, confirm;
    @FXML
    private Label errorMsg;

    public void setContext(String ID) throws SQLException {
        if (ID != null) {
            ResultSet resultSet = CzescService.getCzesc(ID);
            resultSet.next();
            id.setText(resultSet.getString(1));
            nazwa.setText(resultSet.getString(2));
            cena.setText(resultSet.getString(3));
        } else {
            delete.setVisible(false);
            confirm.setOnAction((event) -> confirmNew());
        }
    }

    public void confirmChanges() {
        if (validate()) {
            CzescService.updateCzesc(id.getText(), nazwa.getText(), cena.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void confirmNew() {
        if (validate()) {
            CzescService.addCzesc(nazwa.getText(), cena.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void deleteRecord() {
        try {
            CzescService.deleteCzesc(id.getText());
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
        if (cena.getText() != null) {
            if (!cena.getText().matches("^\\d+(\\.\\d{1,2})?$") || cena.getText().equals(""))
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
