package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.*;

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

}
