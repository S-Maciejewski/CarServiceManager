package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.KlientService;
import services.PracownicyService;
import services.SamochodService;
import services.SerwisService;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainScreenController {
    @FXML
    private ListView<String> klienciIndywidualniList, firmyList, samochodyList, samochodyZastepczeList, serwisyList, pracownicyList, stanyList, czesciList;

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
        openKlientEditModal(true, null, false);
    }

    public void modifyKlientIndywidualny() throws IOException, SQLException {
        if (klienciIndywidualniList.getSelectionModel().getSelectedItem() != null)
            openKlientEditModal(false, klienciIndywidualniList.getSelectionModel().getSelectedItem(), true);
    }

    public void modifyFirma() throws IOException, SQLException {
        if (firmyList.getSelectionModel().getSelectedItem() != null)
            openKlientEditModal(false, firmyList.getSelectionModel().getSelectedItem(), false);
    }

    public void openKlientEditModal(boolean add, String selectedString, boolean indywidualny) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/klientEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja klienta");
        String id = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        loader.<KlientEditViewController>getController().setContext(add, id, indywidualny);
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
        openSamochodEditModal(true, null, false);
    }

    public void modifySamochod() throws IOException, SQLException {
        if (samochodyList.getSelectionModel().getSelectedItem() != null)
            openSamochodEditModal(false, samochodyList.getSelectionModel().getSelectedItem(), false);
    }

    public void modifySamochodZastepczy() throws IOException, SQLException {
        if (samochodyZastepczeList.getSelectionModel().getSelectedItem() != null)
            openSamochodEditModal(false, samochodyZastepczeList.getSelectionModel().getSelectedItem(), true);
    }

    public void openSamochodEditModal(boolean add, String selectedString, boolean zastepczy) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/samochodEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modyfikacja klienta");
        String id = selectedString != null ? selectedString.substring(0, selectedString.indexOf(',')) : null;
        loader.<SamochodEditViewController>getController().setContext(add, id, zastepczy);
        stage.showAndWait();
        showSamochody();
    }

    public void showSerwisy() throws SQLException{
        ObservableList<String> serwisy = FXCollections.observableArrayList();
        ResultSet resultSet = SerwisService.getSerwisy();
        while (resultSet.next()) {
            serwisy.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
        }
        serwisyList.setItems(serwisy);
        ObservableList<String> pracownicy = FXCollections.observableArrayList();
        resultSet = PracownicyService.getPracownicy();
        while (resultSet.next()) {
            pracownicy.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
        }
        pracownicyList.setItems(pracownicy);
    }

    public void addSerwis(){

    }

    public void selectSerwis() throws SQLException {
        String selectedString = serwisyList.getSelectionModel().getSelectedItem();
        if(selectedString != null){
            String id = selectedString.substring(0, selectedString.indexOf(','));
            ObservableList<String> pracownicy = FXCollections.observableArrayList();
            ResultSet resultSet = PracownicyService.getPracownicySerwisu(id);
            while (resultSet.next()) {
                pracownicy.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4));
            }
            pracownicyList.setItems(pracownicy);
        }
    }

    public void modifySerwis(){

    }

    public void addPracownik(){

    }

    public void modifyPracownik(){

    }

    public void addStanCzesci(){

    }

    public void modifyStanCzesci(){

    }

    public void addCzesc(){

    }

    public void modifyCzesc(){

    }
}
