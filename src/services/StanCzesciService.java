package services;

import infrastructure.ConnectionManager;

import java.sql.ResultSet;

public class StanCzesciService {

    public static void addStanCzesci() {
//        ConnectionManager.executeStatement();
    }

    public static ResultSet getStanyCzesci() {
        return ConnectionManager.getStatementResultSet("select ID_SERWISU, ID_CZESCI, ILOSC, NAZWA from STAN_CZESCI natural join CZESC");
    }

    public static ResultSet getStanyCzesciSerwisu(String idSerwisu) {
        return ConnectionManager.getStatementResultSet("select ID_SERWISU, ID_CZESCI, ILOSC, NAZWA from STAN_CZESCI natural join CZESC where ID_SERWISU = '" + idSerwisu + "'");
    }

    public static ResultSet getStanCzesci(String idSerwisu, String idCzesci) {
        return ConnectionManager.getStatementResultSet("select ID_SERWISU, ID_CZESCI, ILOSC, NAZWA from STAN_CZESCI natural join CZESC where ID_SERWISU = '" + idSerwisu + "' and ID_CZESCI = '" + idCzesci + "'");
    }

    public static void deleteStanCzesci(String idSerwisu, String idCzesci) {
        ConnectionManager.executeStatement("delete from STAN_CZESCI where ID_SERWISU = '" + idSerwisu + "' and ID_CZESCI = '" + idCzesci + "'");
    }

    public static void updateStanCzesci(String idSerwisu, String idCzesci, String ilosc) {
        ConnectionManager.executeStatement("update STAN_CZESCI set ILOSC = '" + ilosc + "' where ID_SERWISU = '" + idSerwisu + "' and ID_CZESCI = '" + idCzesci + "'");
    }
}
