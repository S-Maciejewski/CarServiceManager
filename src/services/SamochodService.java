package services;

import infrastructure.ConnectionManager;

public class SamochodService {

    public static void addSamochod(String numRej, String vin, String marka, String model, String pojemosc, String rokProdukcji) {
        marka = marka != null ? marka : "null";
        model = model != null ? model : "null";
        pojemosc = pojemosc != null ? pojemosc : "null";
        rokProdukcji = rokProdukcji != null ? rokProdukcji : "null";
        ConnectionManager.executeProcedure("");    //TODO procedura dodawania w bazie
    }

}
