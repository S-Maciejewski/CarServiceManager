package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.FakturaService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FakturaEditViewController {
    @FXML
    private TextField id, kwota, termin;
    @FXML
    private Button delete, confirm;
    @FXML
    private Label errorMsg;

    public void setContext(String ID) throws SQLException {
        ResultSet resultSet = FakturaService.getFaktura(ID);
        resultSet.next();
        id.setText(resultSet.getString(1));
        kwota.setText(resultSet.getString(2));
        termin.setText(resultSet.getString(3));
    }

    public void confirmChanges() {
        if (validate()) {
            FakturaService.updateFaktura(id.getText(), kwota.getText(), termin.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void deleteRecord() {
        try {
            FakturaService.deleteFaktura(id.getText());
            close();
        } catch (SQLException e) {
            System.out.println("Foreign key constraint exception");
            errorMsg.setVisible(true);
            errorMsg.setText("Błąd - nie można usunąć rekordu, gdyż posiada on powiązania w innych tablicach");
        }
    }

    private boolean validate() {
        errorMsg.setText("Błąd - dane niezgodne z ograniczeniami");

        if (kwota.getText() != null)
            if (!kwota.getText().matches("^\\d+(\\.\\d{1,2})?$") || kwota.getText().equals(""))
                return false;
        if (termin.getText() != null && !termin.getText().equals(""))
            if (!termin.getText().matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$"))
                return false;

        return true;
    }

    private void close() {
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

}
