package utilities;

import api.android.Android;
import managers.QAManager;
import managers.SystemManager;
import org.apache.log4j.Level;

import java.util.regex.Matcher;

/**
 * Created by Artur Spirin on 11/18/15.
 **/
public class Curl extends Utilities{

    private static String content;

    public static boolean isAvailable(){
        return Android.getDeviceOsVersion() < 500 && !SystemManager.executeCommand("adb shell /data/local/tmp/curl --version").toLowerCase().contains("not found");
    }

    public Curl get(String website) {

        content = SystemManager.executeCommand("adb shell /data/local/tmp/curl -L -k --max-time 5 --insecure " + website);
        QAManager.log.debug("Curl output: " + content);
        return this;
    }

    public String getIpAddress(Boolean IPv4) {

        QAManager.log.setLevel(Level.ALL);
        String IP;
        Matcher matcher;
        if(IPv4) matcher = IPV4_PATTERN.matcher(content);
        else matcher = IPV6_PATTERN.matcher(content);
        if (matcher.find()) {
            IP = matcher.group();
            QAManager.log.debug("Extracted IP Address: " + IP);
        }else{
            matcher = IPV4_PATTERN.matcher(content);
            if(!IPv4 && matcher.find()){
                IP = matcher.group();
                QAManager.log.debug("Extracted IPv4 Address instead of IPv6: " + IP);
                throw new AssertionError("Expected IPv6 but got IPv4: " + IP);
            }
            else QAManager.log.debug("Failed to extract IP from <" + content + ">");
            IP = "";
        }
        return  IP;
    }

    public String getContent(){
        return content;
    }

    public String getContentRssHeader(){
        String data = "";
        try{
            data = content.substring(content.indexOf(">"), content.indexOf("</h1>")).replaceAll(">","").trim();
        }catch (StringIndexOutOfBoundsException e){
            return data.trim();
        }
        return data;
    }
}
