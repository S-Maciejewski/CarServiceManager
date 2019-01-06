package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.CzescService;
import services.SerwisService;
import services.StanCzesciService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StanCzesciEditViewController {
    @FXML
    private TextField ilosc;
    @FXML
    private Button delete, confirm;
    @FXML
    private Label errorMsg;
    @FXML
    private ChoiceBox<String> czescDropdown, serwisDropdown;

    public void setContext(String idSerwisu, String idCzesci) throws SQLException {
        ObservableList<String> czesci = FXCollections.observableArrayList();
        ResultSet resultSet = CzescService.getCzesci();
        while (resultSet.next()) {
            czesci.add(resultSet.getString(1) + ", " + resultSet.getString(2));
        }
        czescDropdown.setItems(czesci);
        ObservableList<String> serwisy = FXCollections.observableArrayList();
        resultSet = SerwisService.getSerwisy();
        while (resultSet.next()) {
            serwisy.add(resultSet.getString(1) + ", " + resultSet.getString(2));
        }
        serwisDropdown.setItems(serwisy);
        if (idSerwisu != null && idCzesci != null) {
            resultSet = StanCzesciService.getStanCzesci(idSerwisu, idCzesci);
            resultSet.next();
            ilosc.setText(resultSet.getString(3));
            for (String serwis : serwisy)
                if (serwis.contains(resultSet.getString(1)))
                    serwisDropdown.getSelectionModel().select(serwis);
            for (String czesc : czesci)
                if (czesc.contains(resultSet.getString(2)))
                    czescDropdown.getSelectionModel().select(czesc);
        } else {
            delete.setVisible(false);
            confirm.setOnAction((event) -> confirmNew());
        }
    }

    public void confirmChanges() {
        if (validate()) {
            StanCzesciService.updateStanCzesci(getIdSerwisu(), getIdCzesci(), ilosc.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void confirmNew() {
        if (validate()) {
            StanCzesciService.addStanCzesci(getIdSerwisu(), getIdCzesci(), ilosc.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void deleteRecord() {
        StanCzesciService.deleteStanCzesci(getIdSerwisu(), getIdCzesci());
        close();
    }

    private String getIdSerwisu() {
        return serwisDropdown.getSelectionModel().getSelectedItem().substring(0, serwisDropdown.getSelectionModel().getSelectedItem().indexOf(','));
    }

    private String getIdCzesci() {
        return czescDropdown.getSelectionModel().getSelectedItem().substring(0, serwisDropdown.getSelectionModel().getSelectedItem().indexOf(','));
    }

    private boolean validate() {
        if (ilosc.getText() != null) {
            if (!ilosc.getText().matches("^\\d+(\\.\\d{1,2})?$") || ilosc.getText().equals(""))
                return false;
        } else
            return false;
        if (serwisDropdown.getSelectionModel().getSelectedItem() == null)
            return false;
        if (czescDropdown.getSelectionModel().getSelectedItem() == null)
            return false;
        return true;
    }

    private void close() {
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

}
