package utilities.constants;

import api.hotspotshield.v400.ViewGeneralSettings;
import utilities.Utilities;

/**
 * Created by Artur Spirin on 11/6/15.
 **/
public class Credentials {

    public static String eliteUsername(){
        if(ViewGeneralSettings.LAST_DOMAIN.contains("stage")) return stageEliteUsername;
        if(ViewGeneralSettings.LAST_DOMAIN.contains("dev")) return devEliteUsername;
        else return prodEliteUsername;
    }

    public static String elitePassword(){
        if(ViewGeneralSettings.LAST_DOMAIN.contains("stage")) return stageElitePassword;
        if(ViewGeneralSettings.LAST_DOMAIN.contains("dev")) return devElitePassword;
        else return prodElitePassword;
    }

    public static String freeUsername(){
        if(ViewGeneralSettings.LAST_DOMAIN.contains("stage")) return stageFreeUsername;
        if(ViewGeneralSettings.LAST_DOMAIN.contains("dev")) return devFreeUsername;
        else return prodFreeUsername;
    }

    public static String freePassword(){
        if(ViewGeneralSettings.LAST_DOMAIN.contains("stage")) return stageFreePassword;
        if(ViewGeneralSettings.LAST_DOMAIN.contains("dev")) return devFreePassword;
        else return prodFreePassword;
    }

    public static String limitUsername(){
        if(ViewGeneralSettings.LAST_DOMAIN.contains("stage")) return stageLimitUsername;
        if(ViewGeneralSettings.LAST_DOMAIN.contains("dev")) return devLimitUsername;
        else return prodLimitUsername;
    }

    public static String limitPassword(){
        if(ViewGeneralSettings.LAST_DOMAIN.contains("stage")) return stageLimitPassword;
        if(ViewGeneralSettings.LAST_DOMAIN.contains("dev")) return devLimitPassword;
        else return prodLimitPassword;
    }

    public static String wrongPassword(){
        return String.valueOf(Utilities.generateRandomNumber(999999, 10000));
    }

    public static String wrongUsername(){
        return String.valueOf(Utilities.generateRandomNumber(999999, 10000) + "wrongName");
    }

    private static final String

            //anchorfree07

        prodEliteUsername = "",
        prodElitePassword = "",
        prodFreeUsername = "",
        prodFreePassword = "",
        prodLimitUsername = "",
        prodLimitPassword = "",
        stageEliteUsername = "",
        stageElitePassword = "",
        stageFreeUsername = "",
        stageFreePassword = "",
        stageLimitUsername = "",
        stageLimitPassword = "",
        devEliteUsername= "",
        devElitePassword = "",
        devFreeUsername = "",
        devFreePassword = "",
        devLimitUsername = "",
        devLimitPassword = "";

    public static final String

        gmailUsername= "",
        gmailPassword = "",
        anchorfreeUsername = "",
        anchorfreePassword = "",
        anchorfreeHost = "",
        databaseUsername = "",
        databasePassword = "",
        databaseHost = "";

}
