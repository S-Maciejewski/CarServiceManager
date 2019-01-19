package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SamochodService {

    public static void addSamochod(boolean czyZastepczy, String numRej, String vin, String marka, String model, String pojemosc, String rokProdukcji) {
        marka = marka != null ? marka : "null";
        model = model != null ? model : "null";
        pojemosc = pojemosc != null ? pojemosc : "null";
        rokProdukcji = rokProdukcji != null ? rokProdukcji : "null";
        if (!czyZastepczy)
            ConnectionManager.executeProcedure("{call WSTAW_SAMOCHOD('" + numRej + "', '" + vin + "', '" + marka + "', '" + model + "', '" + pojemosc + "', '" + rokProdukcji + "')}");
        else
            ConnectionManager.executeProcedure("{call WSTAW_SAMOCHOD('" + numRej + "', '" + vin + "', '" + marka + "', '" + model + "', '" + pojemosc + "', '" + rokProdukcji + "', 'ZASTEPCZY')}");
    }

    public static ResultSet getSamochody(boolean czyZastepczy) {
        return czyZastepczy ?
                ConnectionManager.getStatementResultSet("select ID_SAMOCHODU, NUMER_REJESTRACYJNY, VIN, MARKA, MODEL, POJEMNOSC, ROK_PRODUKCJI, CZY_WYPOZYCZONY from SAMOCHOD_ZASTEPCZY left outer join SAMOCHOD on ID_SAMOCHODU_ZASTEPCZEGO = ID_SAMOCHODU") :
                ConnectionManager.getStatementResultSet("select ID_SAMOCHODU, NUMER_REJESTRACYJNY, VIN, MARKA, MODEL, POJEMNOSC, ROK_PRODUKCJI from SAMOCHOD left join SAMOCHOD_ZASTEPCZY on ID_SAMOCHODU_ZASTEPCZEGO = ID_SAMOCHODU where ID_SAMOCHODU_ZASTEPCZEGO is null");

    }

    public static ResultSet getSamochod(boolean czyZastepczy, String ID) {
        return czyZastepczy ?
                ConnectionManager.getStatementResultSet("select ID_SAMOCHODU, NUMER_REJESTRACYJNY, VIN, MARKA, MODEL, POJEMNOSC, ROK_PRODUKCJI, CZY_WYPOZYCZONY from SAMOCHOD_ZASTEPCZY left outer join SAMOCHOD on ID_SAMOCHODU_ZASTEPCZEGO = ID_SAMOCHODU where ID_SAMOCHODU = '" + ID + "'") :
                ConnectionManager.getStatementResultSet("select ID_SAMOCHODU, NUMER_REJESTRACYJNY, VIN, MARKA, MODEL, POJEMNOSC, ROK_PRODUKCJI from SAMOCHOD where ID_SAMOCHODU = '" + ID + "'");
    }

    public static void deleteSamochod(boolean czyZastepczy, String ID) {
        if (czyZastepczy)
            ConnectionManager.executeStatement("delete from SAMOCHOD_ZASTEPCZY where ID_SAMOCHODU_ZASTEPCZEGO = '" + ID + "'");
        ConnectionManager.executeStatement("delete from SAMOCHOD where ID_SAMOCHODU = '" + ID + "'");
    }

    public static void updateSamochod(String ID, boolean czyZastepczy, String numRej, String vin, String marka, String model, String pojemosc, String rokProdukcji, boolean czyWypozyczony) {
        String wypozyczony = czyWypozyczony ? "Tak" : "Nie";
        if (pojemosc != null)
            pojemosc = pojemosc.equals("") ? "null" : pojemosc;
        else
            pojemosc = "null";
        if (rokProdukcji != null)
            rokProdukcji = rokProdukcji.equals("") ? "null" : rokProdukcji;
        else
            rokProdukcji = "null";
        ConnectionManager.executeStatement("update SAMOCHOD set NUMER_REJESTRACYJNY = '" + numRej + "', VIN = '" + vin + "', MARKA = '" + marka + "', MODEL = '" + model + "', POJEMNOSC = " + pojemosc +
                ", ROK_PRODUKCJI = " + rokProdukcji + " where ID_SAMOCHODU = '" + ID + "'");
        if (czyZastepczy) {
            ConnectionManager.executeStatement("update SAMOCHOD_ZASTEPCZY set CZY_WYPOZYCZONY = '" + wypozyczony + "' where ID_SAMOCHODU_ZASTEPCZEGO = '" + ID + "'");
        }
    }

    public static void toggleSamochodZastepczy(String ID) {
        ResultSet rs = getSamochod(true, ID);
        try {
            rs.next();
            if (rs.getString(8).equals("Nie"))
                ConnectionManager.executeStatement("update SAMOCHOD_ZASTEPCZY set CZY_WYPOZYCZONY = 'Tak' where ID_SAMOCHODU_ZASTEPCZEGO = '" + ID + "'");
            else
                ConnectionManager.executeStatement("update SAMOCHOD_ZASTEPCZY set CZY_WYPOZYCZONY = 'Nie' where ID_SAMOCHODU_ZASTEPCZEGO = '" + ID + "'");
        } catch (SQLException e) {
            System.out.println("Samochod zastepczy not found");
        }
    }

}
