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

public class PracownikEditViewController {
    @FXML
    private TextField id, imie, nazwisko;
    @FXML
    private Button delete, confirm;
    @FXML
    private Label errorMsg;
    @FXML
    private ChoiceBox<String> serwisDropdown;

    public void setContext(String ID) throws SQLException {
        ObservableList<String> serwisy = FXCollections.observableArrayList();
        ResultSet resultSet = SerwisService.getSerwisy();
        while (resultSet.next()) {
            serwisy.add(resultSet.getString(1) + ", " + resultSet.getString(2));
        }
        serwisDropdown.setItems(serwisy);
        if (ID != null) {
            resultSet = PracownikService.getPracownik(ID);
            resultSet.next();
            id.setText(resultSet.getString(1));
            imie.setText(resultSet.getString(3));
            nazwisko.setText(resultSet.getString(4));
            for (String serwis : serwisy)
                if (serwis.contains(resultSet.getString(2)))
                    serwisDropdown.getSelectionModel().select(serwis);
        } else {
            delete.setVisible(false);
            confirm.setOnAction((event) -> confirmNew());
        }


    }

    public void confirmChanges() {

    }

    public void confirmNew() {

    }

    public void deleteRecord() {

    }

    private void close() {
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

}
