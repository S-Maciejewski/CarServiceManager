package services;

import infrastructure.ConnectionManager;

public class ClientService {

    public static void addKlient(Boolean czyIndywidualny, String imieNazwa, String nazwiskoNip, String adres) {
        String typ = czyIndywidualny ? "IND" : "FIRMA";
//        System.out.println("execute WSTAW_KLIENTA('" + typ + "', '" + imieNazwa + "', '" + nazwiskoNip + "', '" + adres + "')");
//        ConnectionManager.executeProcedure("execute WSTAW_KLIENTA('" + typ + "', '" + imieNazwa + "', '" + nazwiskoNip + "', '" + adres + "')");

        ConnectionManager.executeProcedure("{call WSTAW_KLIENTA('" + typ + "', '" + imieNazwa + "', '" + nazwiskoNip + "', '" + adres + "')}");

    }

}
