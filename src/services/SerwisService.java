package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SerwisService {

    public static void addSerwis(String nazwa, String adres) {
        ConnectionManager.executeProcedure("{call WSTAW_SERWIS('" + nazwa + "', '" + adres + "')}");
    }

    public static ResultSet getSerwisy() {
        return ConnectionManager.getStatementResultSet("select ID_SERWISU, NAZWA, ADRES from SERWIS");
    }

    public static ResultSet getSerwis(String ID) {
        return ConnectionManager.getStatementResultSet("select ID_SERWISU, NAZWA, ADRES from SERWIS where ID_SERWISU = '" + ID + "'");
    }

    public static void deleteSerwis(String ID) throws SQLException {
        ConnectionManager.executeStatementWithErrorCallback("delete from SERWIS where ID_SERWISU = '" + ID + "'");
    }

    public static void updateSerwis(String ID, String nazwa, String adres) {
        ConnectionManager.executeStatement("update SERWIS set NAZWA = '" + nazwa + "', ADRES = '" + adres + "' where ID_SERWISU = '" + ID + "'");
    }
}
