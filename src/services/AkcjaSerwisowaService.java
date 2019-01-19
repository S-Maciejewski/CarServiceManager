package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AkcjaSerwisowaService {

    public static void addAkcja(String idSamochodu, String idKlienta, String opis, String kwota, String idPracownika, String idSamochoduZastepczego, String dataZakonczenia) {
        opis = opis != null ? opis : "null";
        kwota = kwota != null ? kwota : "0";
//        idPracownika = idPracownika != null ? idPracownika : "null";
//        idSamochoduZastepczego = idSamochoduZastepczego != null ? idSamochoduZastepczego : "null";
//        dataZakonczenia = dataZakonczenia != null ? dataZakonczenia : "null";

        String procedure = "{call DODAJ_AKCJE_SERWISOWA(id_samochodu => '" + idSamochodu + "', id_klienta => '" + idKlienta +
                "', opis => '" + opis + "', kwota => '" + kwota + "'";

        if (idPracownika != null)
            procedure += ", id_pracownika => '" + idPracownika + "'";
        if (idSamochoduZastepczego != null) {
            procedure += ", id_samochodu_zastepczego => '" + idSamochoduZastepczego + "'";
            SamochodService.toggleSamochodZastepczy(idSamochoduZastepczego);
        }

        if (dataZakonczenia != null)
            procedure += ", data_zakonczenia => '" + dataZakonczenia + "'";

        procedure += ")}";

        ConnectionManager.executeProcedure(procedure);

//        ConnectionManager.executeProcedure("{call DODAJ_AKCJE_SERWISOWA(id_samochodu => '" + idSamochodu + "', id_klienta => '" + idKlienta +
//                "', opis => '" + opis + "', kwota => '" + kwota + "', id_pracownika => '" + idPracownika + "', id_samochodu_zastepczego => '"
//                + idSamochoduZastepczego + "', data_zakonczenia => '" + dataZakonczenia + "')}");

    }

    public static ResultSet getAkcje() {
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ID_SAMOCHODU, ID_KLIENTA, OPIS, ID_FAKTURY, ID_PRACOWNIKA, ID_SAMOCHODU_ZASTEPCZEGO, DATA_ROZPOCZECIA, DATA_ZAKONCZENIA from AKCJA_SERWISOWA");
    }

    public static ResultSet getAkcja(String ID) {
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ID_SAMOCHODU, ID_KLIENTA, OPIS, ID_FAKTURY, ID_PRACOWNIKA, ID_SAMOCHODU_ZASTEPCZEGO, DATA_ROZPOCZECIA, DATA_ZAKONCZENIA from AKCJA_SERWISOWA where ID_AKCJI = '" + ID + "'");
    }

    public static ResultSet getAkcjeDetails() {
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ");
    }

    public static void deleteAkcja(String ID) {
        try {
            ResultSet rs = getAkcja(ID);
            rs.next();
            if (rs.getString(7) != null) {
                SamochodService.toggleSamochodZastepczy(rs.getString(7));
            }
        } catch (SQLException e) {
            System.out.println("Samochod zastepczy cannot be tooggled");
        }
        ConnectionManager.executeStatement("delete from AKCJA_SERWISOWA where ID_AKCJI = '" + ID + "'");
    }

    public static void updateAkcja(String ID, String idSamochodu, String idKlienta, String opis, String idFaktury, String idPracownika, String idSamochoduZastepczego, String dataZakonczenia) {
        ConnectionManager.executeStatement("update AKCJA_SERWISOWA set ID_SAMOCHODU = '" + idSamochodu + "', ID_KLIENTA = '" + idKlienta + "', OPIS = '" + opis + "', ID_FAKTURY = '" + idFaktury +
                "', ID_PRACOWNIKA = '" + idPracownika + "', ID_SAMOCHODU_ZASTEPCZEGO = '" + idSamochoduZastepczego + "', DATA_ZAKONCZENIA = '" + dataZakonczenia + "' where ID_AKCJI= '" + ID + "'");
    }

}
