package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;

public class AkcjaSerwisowaService {

    public static void addAkcja(String idSamochodu, String idKlienta, String opis, String kwota, String idPracownika, String idSamochoduZastepczego, String dataZakonczenia) {
        idSamochoduZastepczego = idSamochoduZastepczego != null ? idSamochoduZastepczego : "null";
        dataZakonczenia = dataZakonczenia != null ? dataZakonczenia : "null";
        opis = opis != null ? opis : "null";
        kwota = kwota != null ? kwota : "null";
        idPracownika = idPracownika != null ? idPracownika : "null";
        ConnectionManager.executeProcedure("{call DODAJ_AKCJE_SERWISOWA(id_samochodu => '" + idSamochodu + "', id_klienta => '" + idKlienta +
                "', opis => '" + opis + "', kwota => '" + kwota + "', id_pracownika => '" + idPracownika + "', id_samochodu_zastepczego => '"
                + idSamochoduZastepczego + "', data_zakonczenia => '" + dataZakonczenia + "')}");
    }

    public static ResultSet getAkcje() {
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ID_SAMOCHODU, ID_KLIENTA, OPIS, ID_FAKTURY, ID_PRACOWNIKA, ID_SAMOCHODU_ZASTEPCZEGO, DATA_ROZPOCZECIA, DATA_ZAKONCZENIA from AKCJA_SERWISOWA");
    }

    public static ResultSet getAkcja(String ID) {
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ID_SAMOCHODU, ID_KLIENTA, OPIS, ID_FAKTURY, ID_PRACOWNIKA, ID_SAMOCHODU_ZASTEPCZEGO, DATA_ROZPOCZECIA, DATA_ZAKONCZENIA from AKCJA_SERWISOWA where ID_AKCJI = '" + ID + "'");
    }

    public static ResultSet getAkcjeDetails(){
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ");
    }

    public static void deleteAkcja(String ID) {
        ConnectionManager.executeStatement("delete from AKCJA_SERWISOWA where ID_AKCJI = '" + ID + "'");
    }

    public static void updateAkcja(String ID, String idSamochodu, String idKlienta, String opis, String idFaktury, String idPracownika, String idSamochoduZastepczego, String dataZakonczenia) {
        ConnectionManager.executeStatement("update AKCJA_SERWISOWA set ID_SAMOCHODU = '" + idSamochodu + "', ID_KLIENTA = '" + idKlienta + "', OPIS = '" + opis + "', ID_FAKTURY = '" + idFaktury +
                "', ID_PRACOWNIKA = '" + idPracownika + "', ID_SAMOCHODU_ZASTEPCZEGO = '" + idSamochoduZastepczego + "', DATA_ZAKONCZENIA = '" + dataZakonczenia + "' where ID_AKCJI= '" + ID + "'");
    }

}
