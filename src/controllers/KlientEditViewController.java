package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.KlientService;

import java.sql.ResultSet;
import java.sql.SQLException;


public class KlientEditViewController {
    @FXML
    private TextField id, imieNazwa, nazwiskoNIP, adres;
    @FXML
    private Button delete, confirm;
    @FXML
    private CheckBox czyIndywidualny;
    @FXML
    private Label errorMsg;

    public void setContext(String ID, boolean indywidualny) throws SQLException {
        if (ID != null) {
            czyIndywidualny.setSelected(indywidualny);
            czyIndywidualny.setDisable(true);
            ResultSet resultSet = KlientService.getKlient(indywidualny, ID);
            resultSet.next();
            id.setText(resultSet.getString(1));
            imieNazwa.setText(resultSet.getString(2));
            nazwiskoNIP.setText(resultSet.getString(3));
            adres.setText(resultSet.getString(4));
        } else {
            delete.setVisible(false);
            confirm.setOnAction((event) -> confirmNew());
        }
    }

    public void confirmNew() {
        if (validate()) {
            KlientService.addKlient(czyIndywidualny.isSelected(), imieNazwa.getText(), nazwiskoNIP.getText(), adres.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void confirmChanges() {
        if (validate()) {
            KlientService.updateKlient(id.getText(), czyIndywidualny.isSelected(), imieNazwa.getText(), nazwiskoNIP.getText(), adres.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void deleteKlient() {
        KlientService.deleteKlient(czyIndywidualny.isSelected(), id.getText());
        close();
    }

    private boolean validate() {
        if (adres.getText().length() > 100 || adres.getText() == null)
            return false;
        if (czyIndywidualny.isSelected()) {
            if (imieNazwa.getText().length() > 20 || imieNazwa.getText() == null)
                return false;
            if (nazwiskoNIP.getText().length() > 40 || nazwiskoNIP.getText() == null)
                return false;
        } else {
            if (imieNazwa.getText().length() > 100 || imieNazwa.getText() == null)
                return false;
            if (nazwiskoNIP.getText().length() != 10)
                return false;
        }

        return true;
    }

    private void close() {
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

}
