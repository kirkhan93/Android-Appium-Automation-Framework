package utilities.constants;

import managers.DatabaseManager;
import utilities.Utilities;

/**
 * Created by Artur Spirin on 10/12/15.
 **/
public class DistributionList {

    public static final String[]

            ANDROID =
                    {
                    "secret <secret>@anchorfree.com",
                    "secret <secret>@anchorfree.com",
                    "secret <secret>@anchorfree.com"
                    },
            DEBUG =
                    {
                    "secret <secret>@anchorfree.com"
                    },
            ELITE =
                    {
                    "secret <secret>@anchorfree.com",
                    "secret <secret>@anchorfree.com",
                    "secret <secret>@anchorfree.com",
                    "secret <secret>@anchorfree.com"
                    },
            SERVER =
                    {
                    "secret <secret>@anchorfree.com",
                    "secret <secret>@anchorfree.com",
                    "secret <secret>@anchorfree.com"
                    };

    public static String[] getDistributionList(){
        if(!DatabaseManager.REQUEST_ID.equals(Utilities.DEBUG_ID)){
            switch (DatabaseManager.REQUEST_TYPE) {
                case "ANDROID_FUN": return ANDROID;
                case "ANDROID_SRV": return SERVER;
                case "ANDROID_SC": return ELITE;
                default: return DEBUG;
            }
        }else return DEBUG;
    }
}
