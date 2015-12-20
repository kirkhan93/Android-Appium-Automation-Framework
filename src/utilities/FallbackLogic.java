package utilities;

import managers.DatabaseManager;
import managers.QAManager;

import java.sql.SQLException;

/**
 * Created by Artur on 7/11/2015.
 */
public class FallbackLogic {

    public static final String

            EXPECTED_CUSTOM_ELITE_FALLBACK = "Custom -> Elite Fallback -> Free Fallback",
            EXPECTED_CUSTOM_FREE_FALLBACK = "Custom -> Free Fallback",
            EXPECTED_ELITE_FALLBACK = "Elite or Always or Always + DNS -> Free Custom Fallback",
            EXPECTED_FREE_FALLBACK = "Non-Elite or Always or Always + DNS -> Non-Http -> Dirty",
            EXPECTED_ALWAYS_ELITE_FALLBACK = "Always or Non-Http or Dirty",
            EXPECTED_ALWAYS_FREE_FALLBACK = "Non-Http -> Dirty";

    private static DatabaseManager autoDB = new DatabaseManager();

    public static String[] checkCustomEliteFallback(String IP, String host) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        String[] data = new String[2];
        String[] availableTypes = autoDB.checkAvailableIPTypes(host);
        String ipType = autoDB.getIPType(IP).trim();

        if(availableTypes[5].equals("true")){
            if(ipType.equals("custom")){
                QAManager.log.debug("Expecting custom. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting custom. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting custom. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[2].equals("true") || availableTypes[0].equals("true") || availableTypes[7].equals("true")){
            if(ipType.equals("elite") || ipType.equals("always") || ipType.equals("always + dns")){
                QAManager.log.debug("Expecting elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[3].equals("true") || availableTypes[0].equals("true") || availableTypes[7].equals("true")){
            if(ipType.equals("non-elite") || ipType.equals("always") || ipType.equals("always + dns")){
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[4].equals("true")){
            if(ipType.equals("non-http")){
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else{
            if(ipType.equals("dirty")){
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }
        }
        data[0] = ipType;
        data[1] = String.valueOf(true);
        return  data;
    }

    public static String[] checkCustomFreeFallback(String IP, String host) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        String[] data = new String[2];
        String[] availableTypes = autoDB.checkAvailableIPTypes(host);
        String ipType = autoDB.getIPType(IP);

        if(availableTypes[5].equals("true")){
            if(ipType.equals("custom")){
                QAManager.log.debug("Expecting custom. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting custom. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting custom. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[3].equals("true") || availableTypes[0].equals("true") || availableTypes[7].equals("true")){
            if(ipType.equals("non-elite") || ipType.equals("always") || ipType.equals("always + dns")){
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[4].equals("true")){
            if(ipType.equals("non-http")){
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else{
            if(ipType.equals("dirty")){
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }
        }
        data[0] = ipType;
        data[1] = String.valueOf(true);
        return  data;
    }

    public static String[] checkEliteFallback(String IP, String host) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        String[] data = new String[2];
        String[] availableTypes = autoDB.checkAvailableIPTypes(host);
        String ipType = autoDB.getIPType(IP);

        if(availableTypes[2].equals("true") || availableTypes[0].equals("true") || availableTypes[7].equals("true")){
            if(ipType.equals("elite") || ipType.equals("always") || ipType.equals("always + dns")){
                QAManager.log.debug("Expecting elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[3].equals("true") || availableTypes[0].equals("true") || availableTypes[7].equals("true")){
            if(ipType.equals("non-elite") || ipType.equals("always") || ipType.equals("always + dns")){
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[4].equals("true")){
            if(ipType.equals("non-http")){
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else{
            if(ipType.equals("dirty")){
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }
        }
        data[0] = ipType;
        data[1] = String.valueOf(true);
        return  data;
    }

    public static String[] checkFreeFallback(String IP, String host) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        String[] data = new String[2];
        String[] availableTypes = autoDB.checkAvailableIPTypes(host);
        String ipType = autoDB.getIPType(IP);

        if(availableTypes[3].equals("true") || availableTypes[0].equals("true") || availableTypes[7].equals("true")){
            if(ipType.equals("non-elite") || ipType.equals("always") || ipType.equals("always + dns")){
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-elite, always, always + dns. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else if(availableTypes[4].equals("true")){
            if(ipType.equals("non-http")){
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting non-http. Got IP type: " + ipType + " (" + IP + ")");
            }
        } else{
            if(ipType.equals("dirty")){
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }else{
                QAManager.log.debug("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
                throw new AssertionError("Expecting dirty. Got IP type: " + ipType + " (" + IP + ")");
            }
        }
        data[0] = ipType;
        data[1] = String.valueOf(true);
        return  data;
    }
}
