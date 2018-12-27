package services;

import infrastructure.ConnectionManager;

public class ClientService {

    public static void addKlient(Boolean czyIndywidualny, String imieNazwa, String nazwiskoNip, String adres) {
        String typ = czyIndywidualny ? "IND" : "FIRMA";
        ConnectionManager.executeProcedure("{call WSTAW_KLIENTA('" + typ + "', '" + imieNazwa + "', '" + nazwiskoNip + "', '" + adres + "')}");
    }

}
