package controllers;

import infrastructure.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainScreenController {
    @FXML
    private ListView<String> klienciIndywidualniList, firmyList;

    // Można to robić za pomocą TableView, ale jest dużo roboty z CellValueFactory (albo PropertyValueFactory) i trzeba by pewnie porobić klasy dla każdej encji
    // Dodatkowo nazwy kolumn trzeba by wyciągać z tabel systemowych (nie indeksowalne, okropnie długie przeszukiwanie) albo hardkodować
    public void showKlienci() throws SQLException {
        ObservableList<String> klienciIndywidualni = FXCollections.observableArrayList();
        ResultSet resultSet = ConnectionManager.getStatementResultSet("select ID_KLIENTA, IMIE, NAZWISKO, ADRES from KLIENT natural join KLIENT_INDYWIDUALNY");
        while (resultSet.next()) {
            klienciIndywidualni.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        klienciIndywidualniList.setItems(klienciIndywidualni);
        ObservableList<String> klienciBiznesowi = FXCollections.observableArrayList();  // Nie można reużyć poprzedniej listy, bo jest Observable, czyli asynchronicznie się aktualizuje
        resultSet = ConnectionManager.getStatementResultSet("select ID_KLIENTA, NAZWA, NIP, ADRES from KLIENT natural join FIRMA");
        while (resultSet.next()) {
            klienciBiznesowi.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        firmyList.setItems(klienciBiznesowi);
    }

    public void addKlient() throws IOException, SQLException {
        openKlientEditModal(true, null, false);
    }

    public void modifyKlientIndywidualny() throws IOException, SQLException {
        openKlientEditModal(false, klienciIndywidualniList.getSelectionModel().getSelectedItem(), true);
    }

    public void modifyFirma() throws IOException, SQLException {
        openKlientEditModal(false, firmyList.getSelectionModel().getSelectedItem(), false);
    }

    public void openKlientEditModal(boolean add, String ID, boolean indywidualny) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/klientEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja klienta");
        loader.<KlientEditViewController>getController().setContext(add, ID.substring(0, ID.indexOf(',')), indywidualny);
        stage.showAndWait();
        showKlienci();
    }

}
