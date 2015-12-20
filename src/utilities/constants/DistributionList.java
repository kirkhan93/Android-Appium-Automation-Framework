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
                    "Artur Spirin <a.spirin@anchorfree.com>",
                    "Andrey Smyshlyaev <asmyshlyaev@anchorfree.com>",
                    "Alexey Dubovkin <adubovkin@anchorfree.com>",
                    "Dmitri Molchanov <dmolchanov@anchorfree.com>"
                    },
            DEBUG =
                    {
                    "Artur Spirin <a.spirin@anchorfree.com>"
                    },
            ELITE =
                    {
                    "Artur Spirin <a.spirin@anchorfree.com>",
                    "Alexander Ilyin <ailyin@anchorfree.com>",
                    "Konstantin Samoray <k.samoray@anchorfree.com>",
                    "Pavel Smolin <psmolin@anchorfree.com>"
                    },
            SERVER =
                    {
                    "Artur Spirin <a.spirin@anchorfree.com>"
                    //"Viacheslav Nizametdinov <vnizametdinov@anchorfree.com>",
                    //"Serjan Satimov <ssatimov@anchorfree.com>"
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
