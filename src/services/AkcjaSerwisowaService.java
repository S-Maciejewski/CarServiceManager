package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AkcjaSerwisowaService {

    public static void addAkcja(String idSamochodu, String idKlienta, String opis, String kwota, String idPracownika, String idSamochoduZastepczego, String dataZakonczenia) {
        opis = opis != null ? opis : "null";
        kwota = kwota != null ? kwota : "0";

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


    }

    public static ResultSet getAkcje() {
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ID_SAMOCHODU, ID_KLIENTA, OPIS, ID_FAKTURY, ID_PRACOWNIKA, ID_SAMOCHODU_ZASTEPCZEGO, to_char(DATA_ROZPOCZECIA,'YYYY-MM-DD'), to_char(DATA_ZAKONCZENIA,'YYYY-MM-DD') from AKCJA_SERWISOWA");
    }

    public static ResultSet getAkcja(String ID) {
        return ConnectionManager.getStatementResultSet("select ID_AKCJI, ID_SAMOCHODU, ID_KLIENTA, OPIS, ID_FAKTURY, ID_PRACOWNIKA, ID_SAMOCHODU_ZASTEPCZEGO, to_char(DATA_ROZPOCZECIA,'YYYY-MM-DD'), to_char(DATA_ZAKONCZENIA,'YYYY-MM-DD') from AKCJA_SERWISOWA where ID_AKCJI = '" + ID + "'");
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

    public static void updateAkcja(String ID, String idSamochodu, String idKlienta, String opis, String kwota, String idPracownika, String idSamochoduZastepczego, String dataZakonczenia) {
        opis = opis != null ? opis : "null";

        String statement = "update AKCJA_SERWISOWA set ID_SAMOCHODU = '" + idSamochodu + "', ID_KLIENTA = '" + idKlienta + "', OPIS = '" + opis + "'";
        if (idPracownika != null)
            statement += ", ID_PRACOWNIKA = '" + idPracownika + "'";
        if (idSamochoduZastepczego != null) {
            statement += ", ID_SAMOCHODU_ZASTEPCZEGO = '" + idSamochoduZastepczego + "'";   //Toggled in controller
        } else {
            statement += ", ID_SAMOCHODU_ZASTEPCZEGO = null";
        }
        if (dataZakonczenia != null)
            statement += ", DATA_ZAKONCZENIA = to_date('" + dataZakonczenia + "', 'YYYY-MM-DD')";

        if(kwota != null){
            try {
                ResultSet rs = getAkcja(ID);
                rs.next();
                String idFaktury = rs.getString(5);
                FakturaService.updateFakturaKwota(idFaktury, kwota);
            } catch (Exception e){
                System.out.println("Error updating Faktura in updateAkcja");
            }
        }

        statement += " where ID_AKCJI = '" + ID + "'";

        ConnectionManager.executeStatement(statement);
    }

}
