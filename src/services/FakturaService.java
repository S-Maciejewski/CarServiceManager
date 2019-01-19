package services;

import infrastructure.ConnectionManager;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FakturaService {

    public static void addFaktura(String kwota, String terminPlatnosci) {
        ConnectionManager.executeProcedure("{call WSTAW_FAKTURE('" + kwota + ", " + terminPlatnosci + "')}");
    }

    public static ResultSet getFaktury() {
        return ConnectionManager.getStatementResultSet("select ID_FAKTURY, KWOTA, TERMIN_PLATNOSCI from FAKTURA");
    }

    public static ResultSet getFaktura(String ID) {
        return ConnectionManager.getStatementResultSet("select ID_FAKTURY, KWOTA, to_char(TERMIN_PLATNOSCI,'YYYY-MM-DD') from FAKTURA where ID_FAKTURY = '" + ID + "'");
    }

    public static void deleteFaktura(String ID) throws SQLException {
        ConnectionManager.executeStatementWithErrorCallback("delete from FAKTURA where ID_FAKTURY = '" + ID + "'");
    }

    public static void updateFaktura(String ID, String kwota, String terminPlatnosci) {
        ConnectionManager.executeStatement("update FAKTURA set KWOTA = '" + kwota+ "', TERMIN_PLATNOSCI = to_date('" + terminPlatnosci + "', 'YYYY-MM-DD') where ID_FAKTURY = '" + ID + "'");
    }

    public static void updateFakturaKwota(String ID, String kwota) {
        ConnectionManager.executeStatement("update FAKTURA set KWOTA = '" + kwota+ "' where ID_FAKTURY = '" + ID + "'");
    }
}
