package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainScreenController {
    @FXML
    private ListView<String> klienciIndywidualniList, firmyList, samochodyList, samochodyZastepczeList, serwisyList, pracownicyList, stanyList, czesciList, akcjeList, fakturyList;

    @FXML
    private TextField akcjaId, opis, kwota, dataRozpoczecia, dataZakonczenia;

    @FXML
    private ChoiceBox<String> klientDropdown, samochodDropdown, pracownikDropdown, serwisDropdown, samochodZastepczyDropdown;

    @FXML
    private Button confirmButton, deleteButton;


    public void showKlienci() throws SQLException {
        ObservableList<String> klienciIndywidualni = FXCollections.observableArrayList();
        ResultSet resultSet = KlientService.getKlienci(true);
        while (resultSet.next()) {
            klienciIndywidualni.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        klienciIndywidualniList.setItems(klienciIndywidualni);
        ObservableList<String> klienciBiznesowi = FXCollections.observableArrayList();
        resultSet = KlientService.getKlienci(false);
        while (resultSet.next()) {
            klienciBiznesowi.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        firmyList.setItems(klienciBiznesowi);
        klienciIndywidualniList.getSelectionModel().clearSelection();
        firmyList.getSelectionModel().clearSelection();
    }

    public void addKlient() throws IOException, SQLException {
        openKlientEditModal(null, false);
    }

    public void modifyKlientIndywidualny() throws IOException, SQLException {
        if (klienciIndywidualniList.getSelectionModel().getSelectedItem() != null)
            openKlientEditModal(klienciIndywidualniList.getSelectionModel().getSelectedItem(), true);
    }

    public void modifyFirma() throws IOException, SQLException {
        if (firmyList.getSelectionModel().getSelectedItem() != null)
            openKlientEditModal(firmyList.getSelectionModel().getSelectedItem(), false);
    }

    private void openKlientEditModal(String selectedString, boolean indywidualny) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/klientEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja klienta");
        String id = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        loader.<KlientEditViewController>getController().setContext(id, indywidualny);
        stage.showAndWait();
        showKlienci();
    }


    public void showSamochody() throws SQLException {
        ObservableList<String> samochody = FXCollections.observableArrayList();
        ResultSet resultSet = SamochodService.getSamochody(false);
        while (resultSet.next()) {
            samochody.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4) +
                    ", " + resultSet.getString(5) + ", " + resultSet.getString(6) + ", " + resultSet.getString(7));
        }
        samochodyList.setItems(samochody);
        ObservableList<String> samochodyZastepcze = FXCollections.observableArrayList();
        resultSet = SamochodService.getSamochody(true);
        while (resultSet.next()) {
            samochodyZastepcze.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4) +
                    ", " + resultSet.getString(5) + ", " + resultSet.getString(6) + ", " + resultSet.getString(7) + ", " + resultSet.getString(8));
        }
        samochodyZastepczeList.setItems(samochodyZastepcze);
        samochodyList.getSelectionModel().clearSelection();
        samochodyZastepczeList.getSelectionModel().clearSelection();
    }

    public void addSamochod() throws IOException, SQLException {
        openSamochodEditModal(null, false);
    }

    public void modifySamochod() throws IOException, SQLException {
        if (samochodyList.getSelectionModel().getSelectedItem() != null)
            openSamochodEditModal(samochodyList.getSelectionModel().getSelectedItem(), false);
    }

    public void modifySamochodZastepczy() throws IOException, SQLException {
        if (samochodyZastepczeList.getSelectionModel().getSelectedItem() != null)
            openSamochodEditModal(samochodyZastepczeList.getSelectionModel().getSelectedItem(), true);
    }

    private void openSamochodEditModal(String selectedString, boolean zastepczy) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/samochodEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja samochodu");
        String id = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        loader.<SamochodEditViewController>getController().setContext(id, zastepczy);
        stage.showAndWait();
        showSamochody();
    }


    public void showSerwisy() throws SQLException {
        ObservableList<String> serwisy = FXCollections.observableArrayList();
        ResultSet resultSet = SerwisService.getSerwisy();
        while (resultSet.next()) {
            serwisy.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
        }
        serwisyList.setItems(serwisy);
        ObservableList<String> pracownicy = FXCollections.observableArrayList();
        resultSet = PracownikService.getPracownicy();
        while (resultSet.next()) {
            pracownicy.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        pracownicyList.setItems(pracownicy);
        ObservableList<String> stanyCzesci = FXCollections.observableArrayList();
        resultSet = StanCzesciService.getStanyCzesci();
        while (resultSet.next()) {
            stanyCzesci.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        stanyList.setItems(stanyCzesci);
        ObservableList<String> czesci = FXCollections.observableArrayList();
        resultSet = CzescService.getCzesci();
        while (resultSet.next()) {
            czesci.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
        }
        czesciList.setItems(czesci);
        serwisyList.getSelectionModel().clearSelection();
        pracownicyList.getSelectionModel().clearSelection();
        stanyList.getSelectionModel().clearSelection();
        czesciList.getSelectionModel().clearSelection();
    }

    public void addSerwis() throws IOException, SQLException {
        openSerwisEditModal(null);
    }

    public void modifySerwis() throws IOException, SQLException {
        if (serwisyList.getSelectionModel().getSelectedItem() != null)
            openSerwisEditModal(serwisyList.getSelectionModel().getSelectedItem());
    }

    private void openSerwisEditModal(String selectedString) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/serwisEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja serwisu");
        String id = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        loader.<SerwisEditViewController>getController().setContext(id);
        stage.showAndWait();
        showSerwisy();
    }

    public void selectSerwis() throws SQLException {
        String selectedString = serwisyList.getSelectionModel().getSelectedItem();
        if (selectedString != null) {
            String id = selectedString.substring(0, selectedString.indexOf(','));
            ObservableList<String> pracownicy = FXCollections.observableArrayList();
            ResultSet resultSet = PracownikService.getPracownicySerwisu(id);
            while (resultSet.next()) {
                pracownicy.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
            }
            pracownicyList.setItems(pracownicy);
            ObservableList<String> stanyCzesci = FXCollections.observableArrayList();
            resultSet = StanCzesciService.getStanyCzesciSerwisu(id);
            while (resultSet.next()) {
                stanyCzesci.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
            }
            stanyList.setItems(stanyCzesci);
            ObservableList<String> czesci = FXCollections.observableArrayList();
            resultSet = CzescService.getCzesciSerwisu(id);
            while (resultSet.next()) {
                czesci.add(resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
            }
            czesciList.setItems(czesci);
        }
    }

    public void addPracownik() throws IOException, SQLException {
        openPracownikEditModal(null);
    }

    public void modifyPracownik() throws IOException, SQLException {
        if (pracownicyList.getSelectionModel().getSelectedItem() != null)
            openPracownikEditModal(pracownicyList.getSelectionModel().getSelectedItem());
    }

    private void openPracownikEditModal(String selectedString) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/pracownikEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja pracownika");
        String id = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        loader.<PracownikEditViewController>getController().setContext(id);
        stage.showAndWait();
        showSerwisy();
    }

    public void addCzesc() throws IOException, SQLException {
        openCzescEditModal(null);
    }

    public void modifyCzesc() throws IOException, SQLException {
        if (czesciList.getSelectionModel().getSelectedItem() != null)
            openCzescEditModal(czesciList.getSelectionModel().getSelectedItem());
    }

    private void openCzescEditModal(String selectedString) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/czescEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja części");
        String id = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        loader.<CzescEditViewController>getController().setContext(id);
        stage.showAndWait();
        showSerwisy();
    }

    public void addStanCzesci() throws IOException, SQLException {
        openStanCzesciEditModal(null);
    }

    public void modifyStanCzesci() throws IOException, SQLException {
        if (stanyList.getSelectionModel().getSelectedItem() != null)
            openStanCzesciEditModal(stanyList.getSelectionModel().getSelectedItem());
    }

    private void openStanCzesciEditModal(String selectedString) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/stanCzesciEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja stanu części serwisu");
        String idSerwisu = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        String idCzesci = selectedString != null ? selectedString.substring(selectedString.indexOf(' '), selectedString.indexOf(',', selectedString.indexOf(',') + 1)) : null;
        loader.<StanCzesciEditViewController>getController().setContext(idSerwisu, idCzesci);
        stage.showAndWait();
        showSerwisy();
    }


    public void showAkcje() throws SQLException {
        deleteButton.setVisible(false);
        confirmButton.setText("Dodaj akcję serwisową");
        confirmButton.setOnAction((event) -> addAkcja());
        opis.clear();
        akcjaId.clear();
        kwota.clear();
        dataZakonczenia.clear();
        dataRozpoczecia.clear();

        ObservableList<String> akcje = FXCollections.observableArrayList();
        ObservableList<String> klienci = FXCollections.observableArrayList();
        ObservableList<String> samochody = FXCollections.observableArrayList();
        ObservableList<String> pracownicy = FXCollections.observableArrayList();
        ObservableList<String> serwisy = FXCollections.observableArrayList();
        ObservableList<String> samochodyZastepcze = FXCollections.observableArrayList();
        ObservableList<String> faktury = FXCollections.observableArrayList();

        ResultSet resultSet = KlientService.getKlienci(true);
        while (resultSet.next()) {
            klienci.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        resultSet = KlientService.getKlienci(false);
        while (resultSet.next()) {
            klienci.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        klientDropdown.setItems(klienci);

        resultSet = SamochodService.getSamochody(false);
        while (resultSet.next()) {
            samochody.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(4) + ", " + resultSet.getString(5));
        }
        samochodDropdown.setItems(samochody);

        resultSet = PracownikService.getPracownicy();
        while (resultSet.next()) {
            pracownicy.add(resultSet.getString(1) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        pracownikDropdown.setItems(pracownicy);

        resultSet = SerwisService.getSerwisy();
        while (resultSet.next()) {
            serwisy.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
        }
        serwisDropdown.setItems(serwisy);

        resultSet = SamochodService.getSamochody(true);
        while (resultSet.next()) {
            if (!resultSet.getString(8).equals("Nie"))
                samochodyZastepcze.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(4) + ", " + resultSet.getString(5));
        }
        samochodZastepczyDropdown.setItems(samochodyZastepcze);


        resultSet = AkcjaSerwisowaService.getAkcje();
        while (resultSet.next()) {
            akcje.add(resultSet.getString(1) + ", " + resultSet.getString(8).substring(0, resultSet.getString(8).indexOf(" ")) + ", " + resultSet.getString(9).substring(0, resultSet.getString(9).indexOf(" ")) + ", " + resultSet.getString(4));
        }
        akcjeList.setItems(akcje);

        resultSet = FakturaService.getFaktury();
        while (resultSet.next()) {
            faktury.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
        }
        fakturyList.setItems(faktury);

        akcjeList.getSelectionModel().clearSelection();
        fakturyList.getSelectionModel().clearSelection();
    }

    public void selectAkcja() throws SQLException {
        deleteButton.setVisible(true);
        confirmButton.setText("Zatwierdź modyfikacje");
        confirmButton.setOnAction((event) -> modifyAkcja());

        if (akcjeList.getSelectionModel().getSelectedItem() != null) {
            String selectedID = akcjeList.getSelectionModel().getSelectedItem().substring(0, akcjeList.getSelectionModel().getSelectedItem().indexOf(','));
            ResultSet resultSet = AkcjaSerwisowaService.getAkcja(selectedID);
            resultSet.next();
            akcjaId.setText(resultSet.getString(1));
            opis.setText(resultSet.getString(4));
            dataRozpoczecia.setText(resultSet.getString(8).substring(0, resultSet.getString(8).indexOf(" ")));
            dataZakonczenia.setText(resultSet.getString(9).substring(0, resultSet.getString(9).indexOf(" ")));
            for (String pracownik : pracownikDropdown.getItems())
                if (pracownik.contains(resultSet.getString(6)))
                    pracownikDropdown.getSelectionModel().select(pracownik);

            for (String klient : klientDropdown.getItems())
                if (klient.substring(0, klient.indexOf(',')).contains(resultSet.getString(3)))
                    klientDropdown.getSelectionModel().select(klient);

            for (String samochod : samochodDropdown.getItems())
                if (samochod.substring(0, samochod.indexOf(',')).contains(resultSet.getString(3)))
                    samochodDropdown.getSelectionModel().select(samochod);

            //Mozliwosc odebrania samochodu zastepczego
            ResultSet rs = SamochodService.getSamochod(true, resultSet.getString(2));
            if (rs.next()) {  //TODO Po update zaktualizowac status samochodu
                ObservableList<String> samochodyZastepcze = FXCollections.observableArrayList();
                samochodyZastepcze = samochodZastepczyDropdown.getItems();
                samochodyZastepcze.add("-");
                samochodyZastepcze.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(4) + ", " + rs.getString(5));
                samochodZastepczyDropdown.setItems(samochodyZastepcze);
            }

        }
    }

    public void addAkcja() {

    }

    public void modifyAkcja() {

    }

    public void deleteAkcja() {
    }

    public void modifyFaktura() {

    }

}
