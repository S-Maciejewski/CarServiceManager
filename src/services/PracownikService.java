package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;

public class PracownikService {

    public static void addPracownik(String imie, String nazwisko, String idSerwisu) {
        ConnectionManager.executeProcedure("{call WSTAW_PRACOWNIKA('" + imie + "', '" + nazwisko + "', '" + idSerwisu + "')}");
    }

    public static ResultSet getPracownicy() {
        return ConnectionManager.getStatementResultSet("select ID_PRACOWNIKA, ID_SERWISU, IMIE, NAZWISKO from PRACOWNIK");
    }

    public static ResultSet getPracownicyJoinSerwisy() {
        return ConnectionManager.getStatementResultSet("select ID_PRACOWNIKA, IMIE, NAZWISKO, NAZWA from PRACOWNIK natural join SERWIS");
    }

    public static ResultSet getPracownicySerwisu(String idSerwisu) {
        return ConnectionManager.getStatementResultSet("select ID_PRACOWNIKA, ID_SERWISU, IMIE, NAZWISKO from PRACOWNIK where ID_SERWISU = '" + idSerwisu + "'");
    }

    public static ResultSet getPracownik(String ID) {
        return ConnectionManager.getStatementResultSet("select ID_PRACOWNIKA, ID_SERWISU, IMIE, NAZWISKO from PRACOWNIK where ID_PRACOWNIKA = '" + ID + "'");
    }

    public static void deletePracownik(String ID) {
        ConnectionManager.executeStatement("delete from PRACOWNIK where ID_PRACOWNIKA = '" + ID + "'");
    }

    public static void updatePracownik(String ID, String imie, String nazwisko, String idSerwisu) {
        ConnectionManager.executeStatement("update PRACOWNIK set IMIE = '" + imie + "', NAZWISKO = '" + nazwisko + "', ID_SERWISU = '" + idSerwisu + "' where ID_PRACOWNIKA = '" + ID + "'");
    }
}
