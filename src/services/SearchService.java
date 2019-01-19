package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

// "Klienci", "Samochody", "Serwisy", "Pracownicy", "Części", "Stany części", "Faktury", "Akcje serwisowe"

public class SearchService {

    private static String[] words;

    public static ObservableList<String> getResults(String selectedTable, String pattern) throws SQLException {
        words = pattern.split("\\s+");
        selectedTable = selectedTable != null ? selectedTable : "";
        switch (selectedTable) {
            case "Klienci":
                return searchKlienci();
            case "Samochody":
                return searchSamochody();
            case "Serwisy":
                return searchSerwisy();
            case "Pracownicy":
                return searchPracownicy();
            case "Części":
                return searchCzesci();
            case "Stany części":
                return searchStany();
            case "Faktury":
                return searchFaktury();
            case "Akcje serwisowe":
                return searchAkcje();
            default:
                return globalSearch();
        }
    }

    private static ObservableList<String> genericSearch(ArrayList<String> records) {
        ObservableList<String> result = FXCollections.observableArrayList();
        for (String word : words)
            for (String record : records)
                if (record.toLowerCase().contains(word.toLowerCase()))
                    result.add(record);

        ArrayList<String> deDup = new ArrayList<>(new LinkedHashSet<>(result));
        result.clear();
        result.addAll(deDup);
        return result;
    }

    private static ObservableList<String> globalSearch() throws SQLException {
        ObservableList<String> globalResult = FXCollections.observableArrayList();
        globalResult.addAll(searchKlienci());
        globalResult.addAll(searchSamochody());
        globalResult.addAll(searchPracownicy());
        globalResult.addAll(searchCzesci());
        globalResult.addAll(searchStany());
        globalResult.addAll(searchFaktury());
        globalResult.addAll(searchAkcje());
        return globalResult;
    }

    private static ObservableList<String> searchKlienci() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = KlientService.getKlienci(true);
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4));

        rs = KlientService.getKlienci(true);
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4));

        return genericSearch(records);
    }

    private static ObservableList<String> searchSamochody() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = SamochodService.getSamochody(true);
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4) +
                    ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getString(7) + ", " + rs.getString(8));

        rs = SamochodService.getSamochody(false);
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4) +
                    ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getString(7));

        return genericSearch(records);
    }

    private static ObservableList<String> searchSerwisy() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = SerwisService.getSerwisy();
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3));

        return genericSearch(records);
    }

    private static ObservableList<String> searchPracownicy() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = PracownikService.getPracownicyJoinSerwisy();
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4));

        return genericSearch(records);
    }

    private static ObservableList<String> searchCzesci() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = CzescService.getCzesci();
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3));

        return genericSearch(records);
    }

    private static ObservableList<String> searchStany() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = StanCzesciService.getStanyCzesci();
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4));

        return genericSearch(records);
    }

    private static ObservableList<String> searchFaktury() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = FakturaService.getFaktury();
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3));

        return genericSearch(records);
    }

    private static ObservableList<String> searchAkcje() throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        ResultSet rs = AkcjaSerwisowaService.getAkcje();
        while (rs.next())
            records.add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4) +
                    ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getString(7) + ", " + rs.getString(8) + ", " + rs.getString(9));

        return genericSearch(records);
    }
}
