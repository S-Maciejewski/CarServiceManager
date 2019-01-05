package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.SamochodService;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SamochodEditViewController {
    @FXML
    private TextField id, numRej, vin, marka, model, pojemnosc, rokProdukcji;
    @FXML
    private Button delete, confirm;
    @FXML
    private CheckBox czyZastepczy, czyWypozyczony;
    @FXML
    private Label errorMsg;

    public void setContext(String ID, boolean zastepczy) throws SQLException {
        if (ID != null) {
            czyZastepczy.setSelected(zastepczy);
            czyZastepczy.setDisable(true);
            ResultSet resultSet = SamochodService.getSamochod(zastepczy, ID);
            resultSet.next();
            id.setText(resultSet.getString(1));
            numRej.setText(resultSet.getString(2));
            vin.setText(resultSet.getString(3));
            marka.setText(resultSet.getString(4));
            model.setText(resultSet.getString(5));
            pojemnosc.setText(resultSet.getString(6));
            rokProdukcji.setText(resultSet.getString(7));
            if (zastepczy) {
                czyWypozyczony.setVisible(true);
                if (resultSet.getString(8).equals("Tak"))
                    czyWypozyczony.setSelected(true);
                else
                    czyWypozyczony.setSelected(false);
            }
        } else {
            delete.setVisible(false);
            confirm.setOnAction((event) -> confirmNew());
        }
    }

    public void confirmNew() {
        if (validate()) {
            SamochodService.addSamochod(czyZastepczy.isSelected(), numRej.getText(), vin.getText(), marka.getText(), model.getText(),
                    pojemnosc.getText(), rokProdukcji.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void confirmChanges() {
        if (validate()) {
            SamochodService.updateSamochod(id.getText(), czyZastepczy.isSelected(), numRej.getText(), vin.getText(), marka.getText(), model.getText(),
                    pojemnosc.getText(), rokProdukcji.getText(), czyWypozyczony.isSelected());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void deleteRecord() {
        SamochodService.deleteSamochod(czyZastepczy.isSelected(), id.getText());
        close();
    }

    public void evalueateCzyWypozyczony() {
        if (!czyZastepczy.isSelected()) {
            czyWypozyczony.setSelected(false);
            czyWypozyczony.setVisible(false);
        } else {
            czyWypozyczony.setVisible(true);

        }
    }

    private boolean validate() {

        if (numRej.getText().length() > 8 || numRej.getText() == null)
            return false;
        if (vin.getText().length() != 17)
            return false;
        if(model.getText() != null)
            if (model.getText().length() > 100)
                return false;
        if(marka.getText() != null)
            if(marka.getText().length() > 100)
                return false;
        if(pojemnosc.getText() != null && !pojemnosc.getText().equals(""))
            if(!pojemnosc.getText().matches("^\\d+(\\.\\d{1,2})?$"))
                return false;
        if(rokProdukcji.getText() != null && !rokProdukcji.getText().equals(""))
            if(!rokProdukcji.getText().matches("\\d{4}"))
                return false;
        return true;
    }

    private void close() {
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

}
