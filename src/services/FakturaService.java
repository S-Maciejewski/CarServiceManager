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
        return ConnectionManager.getStatementResultSet("select ID_FAKTURY, KWOTA, TERMIN_PLATNOSCI from FAKTURA where ID_FAKTURY = '" + ID + "'");
    }

    public static void deleteFaktura(String ID) throws SQLException {
        ConnectionManager.executeStatementWithErrorCallback("delete from FAKTURA where ID_FAKTURY = '" + ID + "'");
    }

    public static void updateFaktura(String ID, String kwota, String terminPlatnosci) {
        ConnectionManager.executeStatement("update FAKTURA set KWOTA = '" + kwota+ "', TERMIN_PLATNOSCI = '" + terminPlatnosci + "' where ID_FAKTURY = '" + ID + "'");
    }
}
