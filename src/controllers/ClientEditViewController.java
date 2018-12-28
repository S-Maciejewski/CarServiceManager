package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ClientService;


public class ClientEditViewController {
    @FXML
    private TextField id, imieNazwa, nazwiskoNIP, adres;
    @FXML
    private Button delete, confirm;
    @FXML
    private CheckBox czyIndywidualny;
    @FXML
    private Label errorMsg;

    public void setContext(boolean add) {
        if (add) {
            delete.setVisible(false);
            confirm.setOnAction((event) -> confirmNew());
        }
    }

    public void confirmNew() {
        if (validate()) {
            ClientService.addKlient(czyIndywidualny.isSelected(), imieNazwa.getText(), nazwiskoNIP.getText(), adres.getText());
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }

    public void confirmChanges() {
        if (validate()) {
            //TODO update relacji w serwisie
            close();
        } else {
            errorMsg.setVisible(true);
        }
    }


    private boolean validate() {
        if (adres.getText().length() > 100)
            return false;
        if (czyIndywidualny.isSelected()) {
            if (imieNazwa.getText().length() > 20)
                return false;
            if (nazwiskoNIP.getText().length() > 40)
                return false;
        } else {
            if (imieNazwa.getText().length() > 100)
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
