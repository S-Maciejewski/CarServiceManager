package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;

public class KlientService {

    public static void addKlient(Boolean czyIndywidualny, String imieNazwa, String nazwiskoNip, String adres) {
        String typ = czyIndywidualny ? "IND" : "FIRMA";
        ConnectionManager.executeProcedure("{call WSTAW_KLIENTA('" + typ + "', '" + imieNazwa + "', '" + nazwiskoNip + "', '" + adres + "')}");
    }

    public static ResultSet getKlient(Boolean czyIndywidualny, String ID) {
        return czyIndywidualny ? ConnectionManager.getStatementResultSet("select ID_KLIENTA, IMIE, NAZWISKO, ADRES from KLIENT natural join KLIENT_INDYWIDUALNY where ID_KLIENTA='" + ID + "'") :
                ConnectionManager.getStatementResultSet("select ID_KLIENTA, NAZWA, NIP, ADRES from KLIENT natural join FIRMA where ID_KLIENTA='" + ID + "'");
    }

}
